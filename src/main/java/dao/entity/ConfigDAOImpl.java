package dao.entity;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: czy-dell
 * Date: 11-5-30
 * Time: ÏÂÎç8:12
 * To change this template use File | Settings | File Templates.
 */
public class ConfigDAOImpl implements ConfigDAO, InitializingBean {
    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public ConfigDO getConfigByName(String name) {
        String sql = "select * from config where alias=?";
        ConfigDO config = null;
        List perList = this.jdbcTemplate.queryForList(sql, new Object[]{name});
        if(perList.size() == 1) {
            config = new ConfigDO();
            Map map = (Map) perList.get(0);
            config.setId(Long.valueOf(String.valueOf(map.get("id"))));
            config.setName((String)map.get("name"));
            config.setConfig((Integer) map.get("config"));
            config.setMappingPath((String) map.get("mapping_path"));
        }
        return config;
    }

    @Override
    public boolean addConfig(ConfigDO configDO) {
        String sql = "insert into config (alias, name, config, mapping_path) values (?,?,?,?)";
        try {
            if (this.jdbcTemplate.update(sql, new Object[]{configDO.getAlias(), configDO.getName(), configDO.getConfig(), configDO.getMappingPath()}) > 0) {
                ConfigDO existConfigDO = getConfigByName(configDO.getAlias());
                configDO.setId(existConfigDO.getId());
                return true;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    @Override
    public boolean updateConfig(ConfigDO configDO) {
        String sql = "update config set name=?, config=?, mapping_path=? where id=? and alias=?";
        try {
            this.jdbcTemplate.update(sql, new Object[]{configDO.getName(), configDO.getConfig(), configDO.getMappingPath(), configDO.getId(), configDO.getAlias()});
            return true;
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        int configExist = jdbcTemplate.queryForInt("SELECT COUNT(*) FROM sqlite_master where type=\'table\' and name=?", new Object[]{"config"});
        //create table
        if(configExist == 0) {
            jdbcTemplate.execute("CREATE TABLE \"config\" (\"id\" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , \"alias\" VARCHAR NOT NULL  UNIQUE , \"name\" VARCHAR NOT NULL , \"config\" INTEGER NOT NULL  DEFAULT 5, \"mapping_path\" VARCHAR)");
            jdbcTemplate.execute("CREATE  INDEX \"main\".\"idx_alias\" ON \"config\" (\"alias\" ASC)");
        }
    }
}
