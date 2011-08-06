package web.filter;

import common.PersonConfig;
import common.tools.CookieUtils;
import common.tools.RandomString;
import dao.UserDAO;
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
public class LoginFilter implements Filter {

    private CookieUtils cookieUtils;

    private PersonConfigHandler personConfigHandler;

    private UserDAO userDAO;

    public void setCookieUtils(CookieUtils cookieUtils) {
        this.cookieUtils = cookieUtils;
    }

    public void setPersonConfigHandler(PersonConfigHandler personConfigHandler) {
        this.personConfigHandler = personConfigHandler;
    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        String remoteHost = request.getRemoteAddr();
        String querySring = request.getQueryString();
        Map<String, UserDO> userCache = personConfigHandler.getUserCache();
        String guid = null;
        request.setAttribute("isAfterLocalCombo", false);

        //local combo set pcname
        if (querySring != null && querySring.indexOf("guid") != -1) {
            Matcher matc = Pattern.compile("(?<=guid=)[^?&]+").matcher(querySring);

            if (matc.find()) {
                guid = matc.group();
                request.setAttribute("isAfterLocalCombo", true);
            }
        }

        if (cookieUtils.hasCookie(request.getCookies(), CookieUtils.DEFAULT_KEY) || guid != null) {
            if (guid == null) {
                // has visited
                guid = cookieUtils.getCookie(request.getCookies(), CookieUtils.DEFAULT_KEY).getValue();
            }

            if(guid.equals("")) {
                guid = getGuid();
            }
            /**
             * 有cookie -> 查找cache user -> 找到 ok，返回
             * 有cookie -> 没找到cache user -> 查询db -> 查到，同步ip，没查到，创建一个用户，写入ip
             */
            if (!personConfigHandler.getUserCache().containsKey(guid)) {
                // get user from cache
                UserDO personInfo = this.userDAO.getPersonInfoByGUID(guid);
                if (personInfo != null) {
                    userCache.put(guid, personInfo);
                    request.getSession().setAttribute(request.getSession().getId(), guid);
                    if (!remoteHost.equals(personInfo.getHostName())) {
                        if (this.userDAO.updateHostName(personInfo.getId(), remoteHost, personInfo.getHostName())) {
                            personInfo.setHostName(remoteHost);
                        }
                    }
                } else {
                    System.out.println("has guid ["+guid+"] can't find user[" + remoteHost + "] and create new user");
                    //没查到就创建新用户
                    //构造个人配置
                    personInfo = new UserDO();
                    personInfo.setHostName(remoteHost);
                    personInfo.setGuid(guid);
                    boolean op = userDAO.createNewUser(personInfo);
                    if (op) {
                        userCache.put(guid, personInfo);
                        request.getSession().setAttribute(request.getSession().getId(), guid);
                    }
                }
            }
        } else {
            guid = getGuid();
            //ip sync
            UserDO personInfo = this.userDAO.getPersonInfo(remoteHost);
            if (personInfo != null) {
                System.out.println("no guid and find user[" + remoteHost + "] and sync guid");
                guid = personInfo.getGuid();
                if (guid == null || "".equals(guid)) {
                    guid = getGuid();
                    personInfo.setGuid(guid);
                    if (this.userDAO.updateHostName(personInfo.getId(), remoteHost, personInfo.getHostName())) {
                        personInfo.setHostName(remoteHost);
                    }
                }
                userCache.put(guid, personInfo);
                request.getSession().setAttribute(request.getSession().getId(), guid);
            } else {
                System.out.println("no guid,can't find user[" + remoteHost + "] and create new user");
                //没查到就创建新用户
                //构造个人配置
                personInfo = new UserDO();
                personInfo.setHostName(remoteHost);
                personInfo.setGuid(guid);
                boolean op = userDAO.createNewUser(personInfo);
                if (op) {
                    userCache.put(guid, personInfo);
                    request.getSession().setAttribute(request.getSession().getId(), guid);
                }
            }
            for (String domain : CookieUtils.domains) {
                Cookie cookie = cookieUtils.addCookie(CookieUtils.DEFAULT_KEY, guid, domain);
                if (cookie != null) {
                    response.addCookie(cookie);
                }
            }
        }
        request.setAttribute("guid", guid);
        if((Boolean)request.getAttribute("isCombo")) {
            request.getRequestDispatcher("/combo").forward(request, response);
            return;
        }
        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {
        if (cookieUtils == null) {
            WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
            setCookieUtils((CookieUtils) context.getBean("cookieUtils"));
            setPersonConfigHandler((PersonConfigHandler) context.getBean("personConfigHandler"));
            setUserDAO((UserDAO) context.getBean("userDAO"));
        }
    }

    private String getGuid() {
        return RandomString.getRandomString(30);
    }

}
