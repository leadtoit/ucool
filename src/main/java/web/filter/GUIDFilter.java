package web.filter;

import common.ConfigCenter;
import common.tools.CookieUtils;
import common.tools.RandomString;
import dao.UserDAO;
import dao.entity.UrlBase;
import dao.entity.UserDO;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import web.handler.impl.PersonConfigHandler;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 保障把所有user都放到cache里供后续调用
 *
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 2010-9-24 18:26:00
 */
public class GUIDFilter implements Filter {

    private CookieUtils cookieUtils;

    private PersonConfigHandler personConfigHandler;

    private UserDAO userDAO;

    private ConfigCenter configCenter;

    public void setCookieUtils(CookieUtils cookieUtils) {
        this.cookieUtils = cookieUtils;
    }

    public void setPersonConfigHandler(PersonConfigHandler personConfigHandler) {
        this.personConfigHandler = personConfigHandler;
    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void setConfigCenter(ConfigCenter configCenter) {
        this.configCenter = configCenter;
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        String remoteHost = request.getRemoteAddr();
        String querySring = request.getQueryString();
        Map<String, UserDO> userCache = personConfigHandler.getUserCache();
        Map<String/*ip*/, UserDO> ipCache = personConfigHandler.getIpCache();
        
        UrlBase urlBase = new UrlBase(request.getRequestURL().toString());
        String curDomain = urlBase.getHost();
        boolean isInCookieDomain = configCenter.getUcoolCookieDomain().contains(curDomain);
        String guid = null;
        boolean needIpSync = false;
        boolean needPushCookie = false;

        /**
         * 有两个步骤
         * 1、取正确guid
         * 2、根据正确guid取正确用户配置
         */

        /*这里是本地combo的guid处理*/
        request.setAttribute("isAfterLocalCombo", false);
        //local combo set pcname
        if (querySring != null && querySring.indexOf("guid") != -1) {
            Matcher matc = Pattern.compile("(?<=guid=)[^?&]+").matcher(querySring);

            if (matc.find()) {
                guid = matc.group();
                request.setAttribute("isAfterLocalCombo", true);
            }
        }

        if(guid == null) {
            //session中的值最快
            Object uid = request.getSession().getAttribute(request.getSession().getId());
            if (uid != null) {
                guid = uid.toString();
                /**
                 * 当cookie被清除后，理论上应该取这个值，不排除取不到的可能
                 */
            }
        }

        if(guid == null && isInCookieDomain) {
            if (cookieUtils.hasCookie(request.getCookies(), CookieUtils.DEFAULT_KEY)) {
                guid = cookieUtils.getCookie(request.getCookies(), CookieUtils.DEFAULT_KEY).getValue();
                /**
                 * 在这里有可能是session失效了，也有可能是ip变换了
                 */
                needIpSync = true;
                request.getSession().setAttribute(request.getSession().getId(), guid);
            }
        }

        /**
         * 走到这里还没有guid，就默认作为旧用户处理，也有可能是cookie被删和session失效的用户
         */
        if(guid == null) {
            guid = oldUserSync(remoteHost, ipCache, userCache);
            if(guid != null) {
                request.getSession().setAttribute(request.getSession().getId(), guid);
                needPushCookie = true;
            }
        }

        /**
         * 这里是真正的新用户
         */
        if(guid == null) {
            guid = getGuid();
            request.getSession().setAttribute(request.getSession().getId(), guid);
            needPushCookie = true;
        }

        request.setAttribute("guid", guid);

        /**
         * begin sync
         * 1、查找user cache
         * 2、查db
         * 3、创建用户
         */
        if (!userCache.containsKey(guid)) {
            // get user from cache
            UserDO personInfo = this.userDAO.getPersonInfoByGUID(guid);
            if (personInfo != null) {
                userCache.put(guid, personInfo);
            } else {
                //构造个人配置
                personInfo = new UserDO();
                personInfo.setHostName(remoteHost);
                personInfo.setGuid(guid);
                boolean op = userDAO.createNewUser(personInfo);
                if (op) {
                    userCache.put(guid, personInfo);
                }
            }
        } else {

        }


        if (needPushCookie) {
            System.out.println("ip sync success, another brower has push guid");
            if (isInCookieDomain) {
                pushCookie(response, guid);
            }
        }

        //ip同步
        if(needIpSync) {
            syncRemoteHost(userCache.get(guid), remoteHost, ipCache, userCache);
        }
        
        if ((Boolean) request.getAttribute("isCombo")) {
            request.getRequestDispatcher("/combo").forward(request, response);
            return;
        }

        chain.doFilter(req, resp);
    }

    private void pushCookie(HttpServletResponse response, String guid) {
        for (String domain : CookieUtils.domains) {
            Cookie cookie = cookieUtils.addCookie(CookieUtils.DEFAULT_KEY, guid, domain);
            if (cookie != null) {
                response.addCookie(cookie);
            }
        }
    }

    public void init(FilterConfig config) throws ServletException {
        if (cookieUtils == null) {
            WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
            setCookieUtils((CookieUtils) context.getBean("cookieUtils"));
            setPersonConfigHandler((PersonConfigHandler) context.getBean("personConfigHandler"));
            setUserDAO((UserDAO) context.getBean("userDAO"));
            setConfigCenter((ConfigCenter) context.getBean("configCenter"));
        }
    }

    private String getGuid() {
        return RandomString.getRandomString(30);
    }

    //同步ip
    private boolean syncRemoteHost(UserDO personInfo, String newRemoteHost, Map<String, UserDO> ipCache, Map<String, UserDO> userCache) {
        if(newRemoteHost.equals("127.0.0.1")) {
            return true;
        }

        if (newRemoteHost.equals(personInfo.getHostName())) {
            return true;
        }

        if (this.userDAO.updateHostName(personInfo.getId(), newRemoteHost, personInfo.getHostName())) {
            System.out.println("remoteHost changed, update ip to " + newRemoteHost);
            personInfo.setHostName(newRemoteHost);

            ipCache.put(newRemoteHost, personInfo);
            userCache.put(personInfo.getGuid(), personInfo);
            return true;
        }
        return false;
    }

    /**
     * 对那些没有guid的用户进行同步，并创建guid
     *
     *
     * @param remoteHost
     * @param ipCache
     * @param userCache
     * @return guid
     */
    private String oldUserSync(String remoteHost, Map<String, UserDO> ipCache, Map<String, UserDO> userCache) {
        UserDO oldUser = null;
        if(ipCache.containsKey(remoteHost)) {
            oldUser = ipCache.get(remoteHost);
        } else {
            oldUser = userDAO.getPersonInfo(remoteHost);
        }

        if(oldUser == null) {
            return null;
        } else {
            if(oldUser.getGuid() == null || oldUser.getGuid().equals("")) {
                //到这里才说明的确是旧用户
                oldUser.setGuid(getGuid());
                if (this.userDAO.updateGUID(oldUser.getId(), oldUser.getGuid(), "")) {
                    System.out.println("old user " + oldUser.getId() + ":set new guid!");
                }
            } else {
                //到这里说明是session失效并且cookie被删了的
            }

            ipCache.put(remoteHost, oldUser);
            userCache.put(oldUser.getGuid(), oldUser);
        }

        return oldUser.getGuid();
    }

}
