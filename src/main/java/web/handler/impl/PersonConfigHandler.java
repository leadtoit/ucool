package web.handler.impl;

import common.ConfigCenter;
import common.PersonConfig;
import common.tools.CookieUtils;
import dao.UserDAO;
import dao.entity.UserDO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 10-12-10 下午10:58
 */
public class PersonConfigHandler {
    private UserDAO userDAO;

    private ConfigCenter configCenter;

    private CookieUtils cookieUtils;

    private Map<String, UserDO> userCache = new HashMap<String, UserDO>();

    private Map<String, String> ipCache = new HashMap<String, String>();

    public void setConfigCenter(ConfigCenter configCenter) {
        this.configCenter = configCenter;
    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public Map<String, UserDO> getUserCache() {
        return userCache;
    }

    public void setCookieUtils(CookieUtils cookieUtils) {
        this.cookieUtils = cookieUtils;
    }

    public Map<String, String> getIpCache() {
        return ipCache;
    }

    /**
     * Method doHandler ...
     *
     * @param request of type HttpServletRequest
     * @return PersonConfig
     * @throws IOException when
     * @throws ServletException when
     */
    public PersonConfig doHandler(HttpServletRequest request)
            throws IOException, ServletException {
        String guid  = (String) request.getAttribute("guid");

        // get user from cache
        UserDO personInfo = userCache.get(guid);
        // 这里自从有了filter之后基本上是不会取不到的
        if(personInfo == null) {
            personInfo = this.userDAO.getPersonInfoByGUID(guid);
            if(personInfo != null) {
                userCache.put(guid, personInfo);
                System.out.println("map has size:" + userCache.size());
            }
        }

        //构造个人配置
        PersonConfig personConfig = new PersonConfig();
        personConfig.setConfigCenter(configCenter);
        if (personInfo != null) {
            personConfig.setUserDO(personInfo);
        }
        return personConfig;
    }

}
