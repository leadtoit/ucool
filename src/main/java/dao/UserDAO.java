package dao;

import dao.entity.UserDO;

/**
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 10-12-3 ÉÏÎç12:16
 */
public interface UserDAO {
    /**
     * Method getPersonDirectory ...
     *
     * @param hostName of type String
     * @return String
     */
    UserDO getPersonInfo(String hostName);

    boolean createNewUser(UserDO userDO);

    boolean updateDir(Long userId, String newDir, String oldDir);

    boolean updateConfig(Long userId, int newConfig, int srcConfig);

    boolean updateMappingPath(Long id, String mappingPath, String srcMappingPath);

    UserDO getPersonInfoByGUID(String guid);

    boolean updateHostName(Long userId, String newHostName, String srcHostName);

    boolean updateGUID(Long userId, String guid, String oldguid);
}
