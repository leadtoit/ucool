package common.tools;

import common.ConfigCenter;
import common.PersonConfig;

/**
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 10-11-4 下午10:39
 */
public class UrlTools {

    private ConfigCenter configCenter;

    public void setConfigCenter(ConfigCenter configCenter) {
        this.configCenter = configCenter;
    }

    /**
     * 把请求的在配置文件中的所有域名换成ip
     *
     *
     * @param url      of type String
     * @param isOnline
     * @param personConfig
     * @return String
     */
    public String urlFilter(String url, boolean isOnline, PersonConfig personConfig) {
        if(personConfig == null) {
            if (url.indexOf("127.0.0.1") != -1) {
                return url.replace("127.0.0.1", configCenter.getUcoolOnlineIp());
            }
            if (url.indexOf("localhost") != -1) {
                return url.replace("localhost", configCenter.getUcoolOnlineIp());
            }
            if (url.indexOf("u.taobao.net") != -1) {
                return url.replace("u.taobao.net", configCenter.getUcoolOnlineIp());
            }
            for (String d : configCenter.getUcoolOnlineDomain().split(HttpTools.filterSpecialChar(","))) {
                if (url.indexOf(d) != -1) {
                    return url.replace(d, configCenter.getUcoolOnlineIp());
                }
            }
            for (String d : configCenter.getUcoolDailyDomain().split(HttpTools.filterSpecialChar(","))) {
                if (url.indexOf(d) != -1) {
                    return url.replace(d, configCenter.getUcoolOnlineIp());
                }
            }
            return url;
        }
        /**
         * 防止定位到本地导致自循环
         * 还有一种可能是直接访问本地内网ip，这个没法子
         */
        if (url.indexOf("127.0.0.1") != -1) {
            return url.replace("127.0.0.1", getUsefullIp(personConfig));
        }
        if (url.indexOf("localhost") != -1) {
            return url.replace("localhost", getUsefullIp(personConfig));
        }
        if (url.indexOf("u.taobao.net") != -1) {
            return url.replace("u.taobao.net", getUsefullIp(personConfig));
        }
        if (isOnline) {
            for (String d : configCenter.getUcoolOnlineDomain().split(HttpTools.filterSpecialChar(","))) {
                if (url.indexOf(d) != -1) {
                    if (personConfig.isPrepub()) {
                        if (url.indexOf("?") != -1) {
                            url += "&env=prepub";
                        } else {
                            url += "?env=prepub";
                        }
                    }
                    return url.replace(d, getUsefullIp(personConfig));
                }
            }
        } else {
            for (String d : configCenter.getUcoolDailyDomain().split(HttpTools.filterSpecialChar(","))) {
                if (url.indexOf(d) != -1) {
                    if (url.indexOf("?") != -1) {
                        url += "&env=daily";
                    } else {
                        url += "?env=daily";
                    }
                    return url.replace(d, configCenter.getUcoolDailyIp());
                }
            }
        }
        return url;
    }

    /**
     *  根据当前配置获取有效的ip
     *
     * @return the usefullIp (type String) of this UrlTools object.
     */
    private String getUsefullIp(PersonConfig personConfig) {
        return personConfig.isPrepub() ? configCenter.getUcoolPrepubIp() : configCenter.getUcoolOnlineIp();
    }

    /**
     * TODO 抽象出来
     *
     * @param filePath
     * @param fullUrl
     * @return
     */
    public String debugMode(String filePath, String fullUrl) {
        if (filePath.indexOf("-min") != -1) {
            filePath = filePath.replace("-min", "");
        } else {
            if (filePath.indexOf(".source.") != -1) {
                return filePath;
            }
            //在这里使用配置的文件作特殊处理
            for (String filterString : configCenter.getUcoolAssetsDebugCorrectStrings()) {
                if (fullUrl.indexOf(filterString) != -1) {
                    return filePath;
                }
            }
            if (filePath.endsWith(".css")) {
                filePath = filePath.replace(".css", ".source.css");
            } else {
                filePath = filePath.replace(".js", ".source.js");
            }
        }
        return filePath;
    }

    public static String getParam(String realUrl) {
        int pos = realUrl.lastIndexOf("?");
        if(pos != -1)
            return realUrl.substring(realUrl.lastIndexOf("?"));
        else
            return "";
    }
}
