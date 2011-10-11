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
public class UserFilter implements Filter {

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

        String guid = null;

        /*这里是本地combo的guid处理*/
        request.setAttribute("isAfterLocalCombo", false);
        //local combo set pcname
        if (querySring != null && querySring.indexOf("guid") != -1) {
            Matcher matc = Pattern.compile("(?<=guid=)[^?&]+").matcher(querySring);

            if (matc.find()) {
                guid = matc.group();
                request.setAttribute("isAfterLocalCombo", true);
            }
        } else {
            guid = remoteHost;
        }
        
        if (!userCache.containsKey(remoteHost)) {
            // get user from cache
            UserDO personInfo = this.userDAO.getPersonInfo(guid);
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

        if ((Boolean) request.getAttribute("isCombo")) {
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
            setConfigCenter((ConfigCenter) context.getBean("configCenter"));
        }
    }

    private String getGuid() {
        return RandomString.getRandomString(30);
    }

    //同步ip
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

            userCache.put(personInfo.getGuid(), personInfo);
            return true;
        }
        return false;
    }
}
