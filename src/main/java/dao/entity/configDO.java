package dao.entity;

/**
 * ≈‰÷√DO
 *
 * Created by IntelliJ IDEA.
 * User: czy-dell
 * Date: 11-5-30
 * Time: œ¬ŒÁ8:07
 * To change this template use File | Settings | File Templates.
 */
public class ConfigDO {
    private Long id = 0L;

    private String alias = "";

    private String name = "";

    private int config = 5;

    /**
     * ”≥…‰¬∑æ∂
     */
    private String mappingPath = "";

    private String ip = "127.0.0.1";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getConfig() {
        return config;
    }

    public void setConfig(int config) {
        this.config = config;
    }

    public String getMappingPath() {
        return mappingPath;
    }

    public void setMappingPath(String mappingPath) {
        this.mappingPath = mappingPath;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
