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
 * ���ϰ�����user���ŵ�cache�﹩��������
 *
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 2010-9-24 18:26:00
 * @deprecated 
 */
@Deprecated
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
//        Map<String/*ip*/, UserDO> ipCache = personConfigHandler.getIpCache();
        
        UrlBase urlBase = new UrlBase(request.getRequestURL().toString());
        String curDomain = urlBase.getHost();
        boolean isInCookieDomain = configCenter.getUcoolCookieDomain().contains(curDomain);
        String guid = null;
        boolean needIpSync = false;
        boolean needPushCookie = false;
        boolean isAfterLocalCombo = false;

        /**
         * ����������
         * 1��ȡ��ȷguid
         * 2��������ȷguidȡ��ȷ�û�����
         */

        /*�����Ǳ���combo��guid����*/
        request.setAttribute("isAfterLocalCombo", false);
        //local combo set pcname
        if (querySring != null && querySring.contains("guid")) {
            Matcher matc = Pattern.compile("(?<=guid=)[^?&]+").matcher(querySring);

            if (matc.find()) {
                guid = matc.group();
                isAfterLocalCombo = true;
                request.setAttribute("isAfterLocalCombo", isAfterLocalCombo);
            }
        }

        if(guid == null) {
            //session�е�ֵ���
            Object uid = request.getSession().getAttribute(request.getSession().getId());
            if (uid != null) {
                guid = uid.toString();
                /**
                 * ��cookie�������������Ӧ��ȡ���ֵ�����ų�ȡ�����Ŀ���
                 */
            }
        }

        if(guid == null && isInCookieDomain) {
            if (cookieUtils.hasCookie(request.getCookies(), CookieUtils.DEFAULT_KEY)) {
                guid = cookieUtils.getCookie(request.getCookies(), CookieUtils.DEFAULT_KEY).getValue();
                /**
                 * �������п�����sessionʧЧ�ˣ�Ҳ�п�����ip�任��
                 */
                needIpSync = true;
                //����ʧЧ��session
                request.getSession().invalidate();
                request.getSession(true).setAttribute(request.getSession().getId(), guid);
            }
        }

        /**
         * �ߵ����ﻹû��guid����Ĭ����Ϊ���û�����Ҳ�п�����cookie��ɾ��sessionʧЧ���û�
         */
//        if(guid == null) {
//            guid = oldUserSync(remoteHost, ipCache, userCache);
//            if(guid != null) {
//                request.getSession().setAttribute(request.getSession().getId(), guid);
//                needPushCookie = true;
//            }
//        }

        /**
         * ���������������û�
         */
        if(guid == null) {
            guid = getGuid();
            request.getSession().setAttribute(request.getSession().getId(), guid);
            needPushCookie = true;
        }

        request.setAttribute("guid", guid);

        /**
         * begin sync
         * 1������user cache
         * 2����db
         * 3�������û�
         */
        if (!userCache.containsKey(guid)) {
            // get user from cache
            UserDO personInfo = this.userDAO.getPersonInfoByGUID(guid);
            if (personInfo != null) {
                userCache.put(guid, personInfo);
            } else {
                //�����������
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

        //ipͬ��
        if(needIpSync) {
            syncRemoteHost(userCache.get(guid), remoteHost, userCache, isAfterLocalCombo);
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

    //ͬ��ip
    private boolean syncRemoteHost(UserDO personInfo, String newRemoteHost, Map<String, UserDO> userCache, boolean afterLocalCombo) {
        if(newRemoteHost.equals("127.0.0.1") && afterLocalCombo) {
            return true;
        }

        if (newRemoteHost.equals(personInfo.getHostName())) {
            return true;
        }

        if (this.userDAO.updateHostName(personInfo.getId(), newRemoteHost, personInfo.getHostName())) {
            System.out.println("remoteHost changed, update ip to " + newRemoteHost);
            personInfo.setHostName(newRemoteHost);

            //ÿ�θı�hostʱ��Ҫ�Ѿɵ�ipcacheʧЧ��������Ļ����ڴ������
//            if(ipCache.containsKey(personInfo.getHostName())
//                    && ipCache.get(personInfo.getHostName()).getGuid().equals(personInfo.getGuid())) {
//                //����ȷʵ�����guid��ip cache��ɾ��
//                ipCache.remove(personInfo.getHostName());
//            }
//
//            ipCache.put(newRemoteHost, personInfo);
            userCache.put(personInfo.getGuid(), personInfo);
            return true;
        }
        return false;
    }

    /**
     * ����Щû��guid���û�����ͬ����������guid
     *
     *
     * @param remoteHost
     * @param ipCache
     * @param userCache
     * @return guid
     */
    private String oldUserSync(String remoteHost, Map<String, UserDO> ipCache, Map<String, UserDO> userCache) {
        UserDO oldUser;
        if(ipCache.containsKey(remoteHost)) {
            oldUser = ipCache.get(remoteHost);
        } else {
            oldUser = userDAO.getPersonInfo(remoteHost);
        }

        if(oldUser == null) {
            return null;
        } else {
            if(oldUser.getGuid() == null || oldUser.getGuid().equals("")) {
                //�������˵����ȷ�Ǿ��û�
                oldUser.setGuid(getGuid());
                if (this.userDAO.updateGUID(oldUser.getId(), oldUser.getGuid(), "")) {
                    System.out.println("old user " + oldUser.getId() + ":set new guid!");
                }
            } else {
                //������˵����sessionʧЧ����cookie��ɾ�˵�
            }

            ipCache.put(remoteHost, oldUser);
            userCache.put(oldUser.getGuid(), oldUser);
        }

        return oldUser.getGuid();
    }

}
