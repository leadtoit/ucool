package dao;

import dao.entity.UserDO;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 10-12-3 上午12:16
 */
public class UserDAOImpl implements UserDAO, InitializingBean {
    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override public UserDO getPersonInfo(String hostName) {
        String sql = "select * from user where host_name=?";
        UserDO user = null;
        List perList = this.jdbcTemplate.queryForList(sql, new Object[]{hostName});
        if(perList.size() == 1) {
            user = new UserDO();
            Map map = (Map) perList.get(0);
            user.setId(Long.valueOf(String.valueOf(map.get("id"))));
            user.setHostName((String) map.get("host_name"));
            user.setName((String)map.get("name"));
            user.setConfig((Integer) map.get("config"));
        }
        return user;
    }

    @Override public boolean createNewUser(UserDO userDO) {
        String sql = "insert into user (host_name, name, config) values (?,?,?)";
        try {
            if (this.jdbcTemplate.update(sql, new Object[]{userDO.getHostName(), userDO.getName(), userDO.getConfig()}) > 0) {
                UserDO newUser = getPersonInfo(userDO.getHostName());
                userDO.setId(newUser.getId());
                return true;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    @Override
    public boolean updateDir(Long userId, String newDir, String oldDir) {
        if(newDir.equals(oldDir)) {
            return true;
        }
        String sql = "update user set name=? where id=? and name=?";
        try {
            this.jdbcTemplate.update(sql, new Object[]{newDir, userId, oldDir});
            return true;
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    @Override
    public boolean updateConfig(Long userId, int newConfig, int srcConfig) {
        if (newConfig == srcConfig) {
            //这里return true不知道会不会有什么问题
            return true;
        }
        try {
            if (jdbcTemplate.update("update user set config=? where id=? and config=?", new Object[]{newConfig, userId, srcConfig}) > 0) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        int userExist = jdbcTemplate.queryForInt("SELECT COUNT(*) FROM sqlite_master where type=\'table\' and name=?", new Object[]{"user"});
        //create table
        if(userExist == 0) {
            jdbcTemplate.execute("CREATE TABLE \"user\" (\"id\" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , \"host_name\" VARCHAR NOT NULL  UNIQUE , \"name\" VARCHAR NOT NULL , \"config\" INTEGER NOT NULL  DEFAULT 5)");
            jdbcTemplate.execute("CREATE  INDEX \"main\".\"idx_hostname\" ON \"user\" (\"host_name\" ASC)");
        }
    }
}
