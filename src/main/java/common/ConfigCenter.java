package common;

import tools.HttpTools;
import org.springframework.beans.factory.InitializingBean;

import java.util.Date;
import java.util.Map;
import java.util.Properties;

/**
 * �����������õĿ����ļ���������������config.properties
 *
 * @author <a href="mailto:zhangting@taobao.com">��ͦ</a>
 * @since 2010-9-20 13:32:35
 */
public class ConfigCenter implements InitializingBean {

    /**
     * ���������������ļ��в����ڣ�ֻ��Ϊ�˷�������ļ��������Ŀ¼����·��
     */
    private String webRoot;

    /**
     * �������ʱ��
     */
    private Date lastCleanTime;

    /**
     * ���������ļ�����������ЩĿ¼�µ�assets��debugģʽĬ����ȡxx.js/css������source
     */
    private String[] ucoolAssetsDebugCorrectStrings;

    /**
     * ������������������ЩĿ¼�µ�assets��debugģʽĬ��ʹ�øñ���
     */
    private String[] ucoolAssetsEncodingCorrectStrings;

    /**
     * ��ǰ�Ƿ���Ԥ��ip
     */
    private boolean isPrepub;

    /**
     * �Ƿ�ʹ�ñ��ص�assets
     */
    private boolean isEnableAssets = true;
    
    /**
     * ����Ϊ�����ļ�ʹ��
     */
    //daily������
    private String ucoolDailyDomain;
    //��������
    private String ucoolOnlineDomain;
    //daily ip
    private String ucoolDailyIp;
    //����ip
    private String ucoolOnlineIp;
    //Ԥ��ip
    private String ucoolPrepubIp;
    //combo�ķָ�����
    private String ucoolComboDecollator;

    //�Ƿ���assest���Զ�����
    private String ucoolAssetsAutoClean;
    //�Ƿ���cacheĿ¼���Զ�����
    private String ucoolCacheAutoClean;
    //cacheĿ¼��������
    private String ucoolCacheCleanPeriod;
    //assetsĿ¼��������
    private String ucoolAssetsCleanPeriod;

    //cache��Ŀ¼
    private String ucoolCacheRoot;
    //assetsĿ¼
    private String ucoolAssetsRoot;

    //�Ƿ���assets���Թ���
    private String ucoolAssetsDebug;
    //����debugʱ�����ļ���
    private String ucoolAssetsDebugCorrect;
    //����debugʱ��������
    private String ucoolAssetsEncodingCorrect;

    // ucool proxy��ip
    private String ucoolProxyIp;

    private String ucoolAssetsDirectoryPrefix;

    // ucool�Ŀͻ��˴���˿�
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
                StringBuilder realKey = new StringBuilder();
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
     * ����pz.jsp��css����
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
