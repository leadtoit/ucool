package dao;

import dao.entity.ConfigDO;

/**
 * Created by IntelliJ IDEA.
 * User: czy-dell
 * Date: 11-5-30
 * Time: обнГ8:09
 * To change this template use File | Settings | File Templates.
 */
public interface ConfigDAO {

    ConfigDO getConfigByName(String name);

    boolean addConfig(ConfigDO configDO);

    boolean updateConfig(ConfigDO configDO);
}
