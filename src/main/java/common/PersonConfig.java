package common;

import dao.entity.UserDO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 10-12-11 ����12:21
 */
public class PersonConfig {

    private UserDO userDO;

    private ConfigCenter configCenter;

    public void setConfigCenter(ConfigCenter configCenter) {
        this.configCenter = configCenter;
    }

    // ������û��ڶ��ι��������ֵ��Ϊ true�ˣ������Ͳ����ظ���ѯ���ݿ���
    @Deprecated
    private boolean newUser = false;

    public boolean isUcoolAssetsDebug() {
        if(!isNewUser()) {
            return this.userDO.isEnableDebug();
        } else {
            return configCenter.isUcoolAssetsDebug();
        }
    }

    public void setUcoolAssetsDebug(boolean enableDebug) {
        this.userDO.setEnableDebug(enableDebug);
    }

    public boolean isPrepub() {
        if(!isNewUser()) {
            return this.userDO.isEnablePrepub();
        } else {
            return configCenter.isPrepub();
        }
    }

    public void setPrepub(boolean enablePrepub) {
        this.userDO.setEnablePrepub(enablePrepub);
    }

    public boolean isEnableAssets() {
        if(!isNewUser()) {
            return this.userDO.isEnableAssets();
        } else {
            return configCenter.isEnableAssets();
        }
    }

    public void setEnableAssets(boolean enableAssets) {
        this.userDO.setEnableAssets(enableAssets);
    }

    public String getUcoolCacheRoot() {
        if(isAdvanced()) {
            return userDO.getName() + "/" + configCenter.getUcoolCacheRoot();
        } else {
            return configCenter.getUcoolCacheRoot();
        }
    }

    public String getUcoolAssetsRoot() {
        if(isAdvanced()) {
            return configCenter.getUcoolAssetsRoot() + "/" + userDO.getName();
        } else {
            return configCenter.getUcoolAssetsRoot();
        }
    }

    /**
     * ��ȡ�û��ĸ�Ŀ¼
     * ֻ�а��˵��û��Ż��з���
     * @return
     */
    public String getUserRootDir() {
        if(isAdvanced()) {
            Pattern pat = Pattern.compile("^[^/]+");
            Matcher mat = pat.matcher(userDO.getName());
            if (mat.find()) {
                 return "/" + mat.group();
            }
        }
        return "/";
    }

    public UserDO getUserDO() {
        return userDO;
    }

    public void setUserDO(UserDO userDO) {
        this.userDO = userDO;
    }

    /**
     * �Ƿ����ñ���combo
     * @return
     */
    public boolean isEnableLocalCombo() {
        return userDO!= null && !isNewUser() && this.userDO.isEnableLocalCombo();
    }

    public void setEnableLocalCombo(boolean enableLocalCombo) {
        this.userDO.setEnableLocalCombo(enableLocalCombo);
    }

    /**
     * �Ƿ����ñ���ӳ��
     * @return
     */
    public boolean isEnableLocalMapping() {
        return userDO!= null && !isNewUser() && this.userDO.isEnableLocalMapping();
    }

    public void setEnableLocalMapping(boolean enableLocalMapping) {
        this.userDO.setEnableLocalMapping(enableLocalMapping);
    }

    /**
     * �ж��Ƿ�������
     * �����������2�ֿ��ܣ�
     * 1�����������ˣ�û���κ�Ŀ¼�İ�
     * 2�����û�������ȡ���˰�
     * @return
     */
    @Deprecated
    public boolean isNewUser() {
        return newUser;
    }

    @Deprecated
    public void setNewUser(boolean newUser) {
        this.newUser = newUser;
    }

    /**
     * �õ������ַ���
     *
     * @return the configString (type String) of this PersonConfig object.
     */
    @Deprecated
    public String getConfigString() {
        // get userDO
        StringBuilder sb = new StringBuilder();
        sb.append(userDO.getId()).append(":");
        sb.append(userDO.getHostName()).append(":");
        sb.append(userDO.getName()).append(":");
        sb.append(userDO.getConfig()).append(":");
        sb.append(this.isNewUser());
        return sb.toString();
    }

    /**
     * �Ƿ�����������õ�У�飬�����൱��Ҫ�ķ���������ȡֵǰ��Ҫ��һ��
     * 
     * @return boolean
     * @deprecated 
     */
    @Deprecated
    public boolean personConfigValid() {
        return userDO!= null && !isNewUser() && userDO.getName()!= null && !userDO.getName().isEmpty();
    }

    /**
     * �Ƿ���ʹ�ø߼�����
     * @return
     */
    public boolean isAdvanced() {
        return userDO!= null && userDO.getName()!= null && !userDO.getName().isEmpty();
    }

}
