package common.tools;

import common.ConfigCenter;
import common.PersonConfig;
import tools.HttpTools;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

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
            if (url.contains("127.0.0.1")) {
                return url.replace("127.0.0.1", configCenter.getUcoolOnlineIp());
            }
            if (url.contains("localhost")) {
                return url.replace("localhost", configCenter.getUcoolOnlineIp());
            }
            if (url.contains("u.taobao.net")) {
                return url.replace("u.taobao.net", configCenter.getUcoolOnlineIp());
            }
            for (String d : configCenter.getUcoolOnlineDomain().split(HttpTools.filterSpecialChar(","))) {
                if (url.contains(d)) {
                    return url.replace(d, configCenter.getUcoolOnlineIp());
                }
            }
            for (String d : configCenter.getUcoolDailyDomain().split(HttpTools.filterSpecialChar(","))) {
                if (url.contains(d)) {
                    return url.replace(d, configCenter.getUcoolOnlineIp());
                }
            }

            return url;
        }
        /**
         * 防止定位到本地导致自循环
         * 还有一种可能是直接访问本地内网ip，这个没法子
         */
        if (url.contains("127.0.0.1")) {
            return url.replace("127.0.0.1", getUsefullIp(personConfig));
        }
        if (url.contains("localhost")) {
            return url.replace("localhost", getUsefullIp(personConfig));
        }
        if (url.contains("u.taobao.net")) {
            return url.replace("u.taobao.net", getUsefullIp(personConfig));
        }
        if (isOnline) {
            for (String d : configCenter.getUcoolOnlineDomain().split(HttpTools.filterSpecialChar(","))) {
                if (url.contains(d)) {
                    if (personConfig.isPrepub()) {
                        if (url.contains("?")) {
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
                if (url.contains(d)) {
                    if (url.contains("?")) {
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
        if (filePath.contains("-min")) {
            filePath = filePath.replace("-min", "");
        } else {
            if (filePath.contains(".source.")) {
                return filePath;
            }
            //在这里使用配置的文件作特殊处理
            for (String filterString : configCenter.getUcoolAssetsDebugCorrectStrings()) {
                if (fullUrl.contains(filterString)) {
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

    public static String getCharset(BufferedInputStream bis) {
        String charset = "GBK";
        byte[] first3Bytes = new byte[3];
        bis.mark(0);
        try {
            boolean checked = false;
            int read = bis.read(first3Bytes, 0, 3);
            if (read == -1) return charset;
            if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
                charset = "UTF-16LE";
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xFE && first3Bytes[1] == (byte) 0xFF) {
                charset = "UTF-16BE";
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xEF && first3Bytes[1] == (byte) 0xBB && first3Bytes[2] == (byte) 0xBF) {
                charset = "UTF-8";
                checked = true;
            }
            bis.reset();
            if (!checked) {
                //    int len = 0;
                int loc = 0;

                while ((read = bis.read()) != -1) {
                    loc++;
                    if(loc > 1000)break;
                    if (read >= 0xF0) break;
                    if (0x80 <= read && read <= 0xBF) // 单独出现BF以下的，也算是GBK
                        break;
                    if (0xC0 <= read && read <= 0xDF) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) // 双字节 (0xC0 - 0xDF) (0x80
                            // - 0xBF),也可能在GB编码内
                            continue;
                        else break;
                    } else if (0xE0 <= read && read <= 0xEF) {// 也有可能出错，但是几率较小
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) {
                            read = bis.read();
                            if (0x80 <= read && read <= 0xBF) {
                                charset = "UTF-8";
                                break;
                            } else break;
                        } else break;
                    }
                }
                //System.out.println( loc + " " + Integer.toHexString( read ) );
            }
//            bis.close();
            bis.reset();
        } catch (Exception e) {
//            e.printStackTrace();
        }

        return charset;
    }

    /**
     * 读取流中前面的字符，看是否有bom，如果有bom，将bom头先读掉丢弃
     *
     * @param in
     * @return
     * @throws IOException
     */
    public static InputStream removeBom(InputStream in) throws IOException {

        PushbackInputStream testin = new PushbackInputStream(in);
        int ch = testin.read();
        if (ch != 0xEF) {
            testin.unread(ch);
        } else if ((ch = testin.read()) != 0xBB) { // if ch==0xef
            testin.unread(ch);
            testin.unread(0xef);
        } else if ((ch = testin.read()) != 0xBF) { // if ch ==0xbb
            throw new IOException("错误的UTF-8格式文件");
        } else { // if ch ==0xbf
            // 不需要做，这里是bom头被读完了
            // // System.out.println("still exist bom");
        }
        return testin;

    }
}
