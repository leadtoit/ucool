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

/**
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 10-12-10 ����10:58
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
        String guid  = (String) request.getAttribute("guid");

        // get user from cache
        UserDO personInfo = userCache.get(guid);
        // �����Դ�����filter֮��������ǲ���ȡ������
        if(personInfo == null) {
            personInfo = this.userDAO.getPersonInfo(guid);
            if(personInfo != null) {
                userCache.put(guid, personInfo);
                System.out.println("map has size:" + userCache.size());
            }
        }

        //�����������
        PersonConfig personConfig = new PersonConfig();
        personConfig.setConfigCenter(configCenter);
        if (personInfo != null) {
            personConfig.setUserDO(personInfo);
        }
        return personConfig;
    }

}
