package web.handler.impl;

import common.ConfigCenter;
import common.PersonConfig;
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

    private Map<String, UserDO> userCache = new HashMap<String, UserDO>();

    public void setConfigCenter(ConfigCenter configCenter) {
        this.configCenter = configCenter;
    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public Map<String, UserDO> getUserCache() {
        return userCache;
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
        // 0.6版本后直接去取ip了
        String remoteHost = request.getRemoteAddr();
        String querySring = request.getQueryString();
        String pcname = null;
        String guid = null;
        if(querySring != null && querySring.indexOf("guid") != -1) {
            Matcher matc = Pattern.compile("(?<=guid=)[^?&]+").matcher(querySring);

            if (matc.find()) {
                pcname = matc.group();
            }
        }

        //本地combo二次请求的时候机器名只能这样带过来
        if (pcname != null) {
            guid = pcname.toString();
        }

        // get user from cache
        UserDO personInfo = userCache.get(guid);
        // 这里自从有了filter之后基本上是不会取不到的
        if(personInfo == null) {
            personInfo = this.userDAO.getPersonInfoByGUID(guid);
            if(personInfo != null) {
                userCache.put(guid, personInfo);
                request.getSession().setAttribute(request.getSession().getId(), guid);
                System.out.println("map has size:" + userCache.size());
            }
        }

        //构造个人配置
        PersonConfig personConfig = new PersonConfig();
        personConfig.setConfigCenter(configCenter);
        if (personInfo != null) {
            personConfig.setUserDO(personInfo);
        } else {
            personConfig.setUserDO(new UserDO());
            personConfig.getUserDO().setHostName(remoteHost);
            personConfig.getUserDO().setGuid(guid);
            //没在数据库查询到数据，肯定是新人
            personConfig.setNewUser(true);
        }
        return personConfig;
    }

}
