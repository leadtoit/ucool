package common;

import dao.entity.UserDO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 10-12-11 上午12:21
 */
public class PersonConfig {

    private UserDO userDO;

    private ConfigCenter configCenter;

    public void setConfigCenter(ConfigCenter configCenter) {
        this.configCenter = configCenter;
    }

    // 如果新用户第二次过来，这个值就为 true了，这样就不用重复查询数据库了
    private boolean newUser = false;

    public boolean isUcoolAssetsDebug() {
        if(personConfigValid()) {
            return this.userDO.isEnableDebug();
        } else {
            return configCenter.isUcoolAssetsDebug();
        }
    }

    public void setUcoolAssetsDebug(boolean enableDebug) {
        this.userDO.setEnableDebug(enableDebug);
    }

    public boolean isPrepub() {
        if(personConfigValid()) {
            return this.userDO.isEnablePrepub();
        } else {
            return configCenter.isPrepub();
        }
    }

    public void setPrepub(boolean enablePrepub) {
        this.userDO.setEnablePrepub(enablePrepub);
    }

    public boolean isEnableAssets() {
        if(personConfigValid()) {
            return this.userDO.isEnableAssets();
        } else {
            return configCenter.isEnableAssets();
        }
    }

    public void setEnableAssets(boolean enableAssets) {
        this.userDO.setEnableAssets(enableAssets);
    }

    public String getUcoolCacheRoot() {
        if(personConfigValid()) {
            return userDO.getName() + "/" + configCenter.getUcoolCacheRoot();
        } else {
            return configCenter.getUcoolCacheRoot();
        }
    }

    public String getUcoolAssetsRoot() {
        if(personConfigValid()) {
            return configCenter.getUcoolAssetsRoot() + "/" + userDO.getName();
        } else {
            return configCenter.getUcoolAssetsRoot();
        }
    }

    /**
     * 获取用户的根目录
     * 只有绑定了的用户才会有返回
     * @return
     */
    public String getUserRootDir() {
        if(personConfigValid()) {
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
     * 是否启用本地combo
     * @return
     */
    public boolean isEnableLocalCombo() {
        return personConfigValid() && this.userDO.isEnableLocalCombo();
    }

    public void setEnableLocalCombo(boolean enableLocalCombo) {
        this.userDO.setEnableLocalCombo(enableLocalCombo);
    }

    /**
     * 判断是否是新人
     * 这里的新人有2种可能：
     * 1、真正的新人，没有任何目录的绑定
     * 2、老用户，但是取消了绑定
     * @return
     */
    public boolean isNewUser() {
        return newUser;
    }

    public void setNewUser(boolean newUser) {
        this.newUser = newUser;
    }

    /**
     * 得到配置字符串
     *
     * @return the configString (type String) of this PersonConfig object.
     */
    public String getConfigString() {
        // get userDO
        StringBuffer sb = new StringBuffer();
        sb.append(userDO.getId()).append(":");
        sb.append(userDO.getHostName()).append(":");
        sb.append(userDO.getName()).append(":");
        sb.append(userDO.getConfig()).append(":");
        sb.append(this.isNewUser());
        return sb.toString();
    }

    /**
     * 是否满足个人配置的校验，后期相当重要的方法，所有取值前都要走一遍
     * 
     * @return boolean
     */
    public boolean personConfigValid() {
        return userDO!= null && !isNewUser() && userDO.getName()!= null && !userDO.getName().isEmpty();
    }
}
