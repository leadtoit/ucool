package common;

import common.tools.HttpTools;
import org.springframework.beans.factory.InitializingBean;

import java.util.Date;
import java.util.Map;
import java.util.Properties;

/**
 * 包含所有配置的开关文件，具体参数含义见config.properties
 *
 * @author <a href="mailto:zhangting@taobao.com">张挺</a>
 * @since 2010-9-20 13:32:35
 */
public class ConfigCenter implements InitializingBean {

    /**
     * 以下配置在属性文件中不存在，只是为了方便查找文件而保存根目录绝对路径
     */
    private String webRoot;

    /**
     * 最近清理时间
     */
    private Date lastCleanTime;

    /**
     * 用于特殊文件名纠正，这些目录下的assets的debug模式默认先取xx.js/css而不加source
     */
    private String[] ucoolAssetsDebugCorrectStrings;

    /**
     * 用于特殊编码纠正，这些目录下的assets的debug模式默认使用该编码
     */
    private String[] ucoolAssetsEncodingCorrectStrings;

    /**
     * 当前是否走预发ip
     */
    private boolean isPrepub;

    /**
     * 是否使用本地的assets
     */
    private boolean isEnableAssets = true;
    
    /**
     * 以下为配置文件使用
     */
    //daily的域名
    private String ucoolDailyDomain;
    //线上域名
    private String ucoolOnlineDomain;
    //daily ip
    private String ucoolDailyIp;
    //线上ip
    private String ucoolOnlineIp;
    //预发ip
    private String ucoolPrepubIp;
    //combo的分隔符号
    private String ucoolComboDecollator;

    //是否开启assest的自动清理
    private String ucoolAssetsAutoClean;
    //是否开启cache目录的自动清理
    private String ucoolCacheAutoClean;
    //cache目录清理周期
    private String ucoolCacheCleanPeriod;
    //assets目录清理周期
    private String ucoolAssetsCleanPeriod;

    //cache根目录
    private String ucoolCacheRoot;
    //assets目录
    private String ucoolAssetsRoot;

    //是否开启assets调试功能
    private String ucoolAssetsDebug;
    //用于debug时纠正文件名
    private String ucoolAssetsDebugCorrect;
    //用于debug时纠正编码
    private String ucoolAssetsEncodingCorrect;

    // ucool proxy的ip
    private String ucoolProxyIp;

    private String ucoolAssetsDirectoryPrefix;

    // ucool的客户端代理端口
    private String ucoolProxyClientPort;

    private String ucoolCookieDomain;

    public String getWebRoot() {
        return webRoot;
    }

    public void setWebRoot(String webRoot) {
        this.webRoot = webRoot;
    }

    public boolean isPrepub() {
        return isPrepub;
    }

    public void setPrepub(boolean prepub) {
        isPrepub = prepub;
    }

    public boolean isEnableAssets() {
        return isEnableAssets;
    }

    public void setEnableAssets(boolean enableAssets) {
        isEnableAssets = enableAssets;
    }

    public String[] getUcoolAssetsDebugCorrectStrings() {
        return ucoolAssetsDebugCorrectStrings;
    }

    public void setUcoolAssetsDebugCorrectStrings(String[] ucoolAssetsDebugCorrectStrings) {
        this.ucoolAssetsDebugCorrectStrings = ucoolAssetsDebugCorrectStrings;
    }

    public String[] getUcoolAssetsEncodingCorrectStrings() {
        return ucoolAssetsEncodingCorrectStrings;
    }

    public void setUcoolAssetsEncodingCorrectStrings(String[] ucoolAssetsEncodingCorrectStrings) {
        this.ucoolAssetsEncodingCorrectStrings = ucoolAssetsEncodingCorrectStrings;
    }

    public String getUcoolDailyDomain() {
        return ucoolDailyDomain;
    }

    public void setUcoolDailyDomain(String ucoolDailyDomain) {
        this.ucoolDailyDomain = ucoolDailyDomain;
    }

    public String getUcoolOnlineDomain() {
        return ucoolOnlineDomain;
    }

    public void setUcoolOnlineDomain(String ucoolOnlineDomain) {
        this.ucoolOnlineDomain = ucoolOnlineDomain;
    }

    public String getUcoolDailyIp() {
        return ucoolDailyIp;
    }

    public void setUcoolDailyIp(String ucoolDailyIp) {
        this.ucoolDailyIp = ucoolDailyIp;
    }

    public String getUcoolOnlineIp() {
        return ucoolOnlineIp;
    }

    public void setUcoolOnlineIp(String ucoolOnlineIp) {
        this.ucoolOnlineIp = ucoolOnlineIp;
    }

    public String getUcoolAssetsAutoClean() {
        return ucoolAssetsAutoClean;
    }

    public void setUcoolAssetsAutoClean(String ucoolAssetsAutoClean) {
        this.ucoolAssetsAutoClean = ucoolAssetsAutoClean;
    }

    public String getUcoolCacheAutoClean() {
        return ucoolCacheAutoClean;
    }

    public void setUcoolCacheAutoClean(String ucoolCacheAutoClean) {
        this.ucoolCacheAutoClean = ucoolCacheAutoClean;
    }

    public String getUcoolCacheCleanPeriod() {
        return ucoolCacheCleanPeriod;
    }

    public void setUcoolCacheCleanPeriod(String ucoolCacheCleanPeriod) {
        this.ucoolCacheCleanPeriod = ucoolCacheCleanPeriod;
    }

    public String getUcoolAssetsCleanPeriod() {
        return ucoolAssetsCleanPeriod;
    }

    public void setUcoolAssetsCleanPeriod(String ucoolAssetsCleanPeriod) {
        this.ucoolAssetsCleanPeriod = ucoolAssetsCleanPeriod;
    }

    public String getUcoolCacheRoot() {
        return "/" + ucoolCacheRoot;
    }

    public void setUcoolCacheRoot(String ucoolCacheRoot) {
        this.ucoolCacheRoot = ucoolCacheRoot;
    }

    public String getUcoolAssetsRoot() {
        return "/" + ucoolAssetsRoot;
    }

    public boolean isUcoolAssetsDebug() {
        return ucoolAssetsDebug.equals("true");
    }

    public void setUcoolAssetsDebug(boolean ucoolAssetsDebug) {
        this.ucoolAssetsDebug = String.valueOf(ucoolAssetsDebug);
    }

    public void setUcoolAssetsRoot(String ucoolAssetsRoot) {
        this.ucoolAssetsRoot = ucoolAssetsRoot;
    }

    @Deprecated
    public String getUcoolAssetsDebug() {
        return ucoolAssetsDebug;
    }
    @Deprecated
    public void setUcoolAssetsDebug(String ucoolAssetsDebug) {
        this.ucoolAssetsDebug = ucoolAssetsDebug;
    }

    public Date getLastCleanTime() {
        return lastCleanTime;
    }

    public void setLastCleanTime(Date lastCleanTime) {
        this.lastCleanTime = lastCleanTime;
    }

    public String getUcoolComboDecollator() {
        return ucoolComboDecollator;
    }

    public void setUcoolComboDecollator(String ucoolComboDecollator) {
        this.ucoolComboDecollator = ucoolComboDecollator;
    }

    public String getUcoolAssetsDebugCorrect() {
        return ucoolAssetsDebugCorrect;
    }

    public void setUcoolAssetsDebugCorrect(String ucoolAssetsDebugCorrect) {
        this.ucoolAssetsDebugCorrect = ucoolAssetsDebugCorrect;
    }

    public String getUcoolPrepubIp() {
        return ucoolPrepubIp;
    }

    public void setUcoolPrepubIp(String ucoolPrepubIp) {
        this.ucoolPrepubIp = ucoolPrepubIp;
    }

    public String getUcoolAssetsEncodingCorrect() {
        return ucoolAssetsEncodingCorrect;
    }

    public void setUcoolAssetsEncodingCorrect(String ucoolAssetsEncodingCorrect) {
        this.ucoolAssetsEncodingCorrect = ucoolAssetsEncodingCorrect;
    }

    public String getUcoolProxyIp() {
        return ucoolProxyIp;
    }

    public void setUcoolProxyIp(String ucoolProxyIp) {
        this.ucoolProxyIp = ucoolProxyIp;
    }

    public String getUcoolAssetsDirectoryPrefix() {
        return ucoolAssetsDirectoryPrefix;
    }

    public void setUcoolAssetsDirectoryPrefix(String ucoolAssetsDirectoryPrefix) {
        this.ucoolAssetsDirectoryPrefix = ucoolAssetsDirectoryPrefix;
    }

    public String getUcoolProxyClientPort() {
        return ucoolProxyClientPort;
    }

    public void setUcoolProxyClientPort(String ucoolProxyClientPort) {
        this.ucoolProxyClientPort = ucoolProxyClientPort;
    }

    public String getUcoolCookieDomain() {
        return ucoolCookieDomain;
    }

    public void setUcoolCookieDomain(String ucoolCookieDomain) {
        this.ucoolCookieDomain = ucoolCookieDomain;
    }

    /**
     * Method afterPropertiesSet ...
     *
     * @throws Exception when
     * @author zhangting
     * @since 2010-9-20 13:38:17
     */
    @Override public void afterPropertiesSet() throws Exception {
        Properties properties = new Properties();
        properties.load(this.getClass().getResourceAsStream("/config.properties"));
        if (!properties.isEmpty()) {
            for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                String key = entry.getKey().toString();
                String[] keySplits = key.split(HttpTools.filterSpecialChar("."));
                StringBuffer realKey = new StringBuffer();
                for (String keySplit : keySplits) {
                    realKey.append(keySplit.toUpperCase().substring(0, 1)).append(keySplit.substring(1));
                }

                //refect to set value
                this.getClass().getDeclaredMethod("set" + realKey.toString(), String.class)
                        .invoke(this, properties.get(key));
            }
        } else {
            //log
        }
    }


    /**
     * 用于pz.jsp的css调用
     *
     * @param state of type String
     * @return String
     */
    public String getStateStringStyle(String state) {
        return state.equals("true")?"switch-open":"switch-close";
    }

    public String getStateStyle(boolean status) {
        return status ?"switch-open":"switch-close";
    }

    public static void main(String[] args) throws Exception {
        ConfigCenter configCenter = ConfigCenter.class.newInstance();
        configCenter.afterPropertiesSet();
    }
}
