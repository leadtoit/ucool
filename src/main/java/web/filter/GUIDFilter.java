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
        Map<String/*ip*/, String/*guid*/> ipCache = personConfigHandler.getIpCache();
        
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
            if (uid == null) {
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
            }
        }

        /**
         * get from ip
         * 如果在ipCache才找到说明是没有cookie，需要push cookie
         */
        if(guid == null && ipCache.containsKey(remoteHost)) {
            guid = ipCache.get(remoteHost);
            needPushCookie = true;
        }

        if(guid == null) {
            guid = getGuid();
        }

        request.setAttribute("guid", guid);
        request.getSession().setAttribute(request.getSession().getId(), guid);

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

            if(isInCookieDomain) {
                pushCookie(response, guid);
            }
        } else {
            if(needPushCookie) {
                System.out.println("ip sync success, another brower has push guid");
                if(isInCookieDomain) {
                    pushCookie(response, guid);
                }
            }
        }
        //ip同步
        if(needIpSync) {
            syncRemoteHost(userCache.get(guid), remoteHost, ipCache);
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
    private boolean syncRemoteHost(UserDO personInfo, String newRemoteHost, Map<String, String> ipCache) {
        if(newRemoteHost.equals("127.0.0.1")) {
            return true;
        }

        ipCache.put(newRemoteHost, personInfo.getGuid());
        
        if (newRemoteHost.equals(personInfo.getHostName())) {
            return true;
        }

        if (this.userDAO.updateHostName(personInfo.getId(), newRemoteHost, personInfo.getHostName())) {
            System.out.println("remoteHost changed, update ip to " + newRemoteHost);
            personInfo.setHostName(newRemoteHost);
            return true;
        }
        return false;
    }

}
