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
 * @since 10-11-4 ����10:39
 */
public class UrlTools {

    private ConfigCenter configCenter;

    public void setConfigCenter(ConfigCenter configCenter) {
        this.configCenter = configCenter;
    }

    /**
     * ��������������ļ��е�������������ip
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
         * ��ֹ��λ�����ص�����ѭ��
         * ����һ�ֿ�����ֱ�ӷ��ʱ�������ip�����û����
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
     *  ���ݵ�ǰ���û�ȡ��Ч��ip
     *
     * @return the usefullIp (type String) of this UrlTools object.
     */
    private String getUsefullIp(PersonConfig personConfig) {
        return personConfig.isPrepub() ? configCenter.getUcoolPrepubIp() : configCenter.getUcoolOnlineIp();
    }

    /**
     * TODO �������
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
            //������ʹ�����õ��ļ������⴦��
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
                    if (0x80 <= read && read <= 0xBF) // ��������BF���µģ�Ҳ����GBK
                        break;
                    if (0xC0 <= read && read <= 0xDF) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) // ˫�ֽ� (0xC0 - 0xDF) (0x80
                            // - 0xBF),Ҳ������GB������
                            continue;
                        else break;
                    } else if (0xE0 <= read && read <= 0xEF) {// Ҳ�п��ܳ������Ǽ��ʽ�С
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
     * ��ȡ����ǰ����ַ������Ƿ���bom�������bom����bomͷ�ȶ�������
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
            throw new IOException("�����UTF-8��ʽ�ļ�");
        } else { // if ch ==0xbf
            // ����Ҫ����������bomͷ��������
            // // System.out.println("still exist bom");
        }
        return testin;

    }
}
