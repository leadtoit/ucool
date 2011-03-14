package web.url;

import biz.file.FileEditor;
import common.ConfigCenter;
import common.PersonConfig;
import common.tools.UrlTools;
import dao.entity.RequestInfo;

import java.io.*;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 2010-10-2 13:33:26
 */
public class UrlExecutor {

    private FileEditor fileEditor;

    private ConfigCenter configCenter;

    private static String LOCAL_COMBO_CONFIG_NAME = "/combo.properties";

    private static String PERSON_CONFIG_NAME = "/my.properties";

    private static String ENCODING_CORRECT = "my.encoding.list";

    public void setFileEditor(FileEditor fileEditor) {
        this.fileEditor = fileEditor;
    }

    public void setConfigCenter(ConfigCenter configCenter) {
        this.configCenter = configCenter;
    }

    /**
     * 为debug模式特殊处理url请求，不走cache
     *
     * @param requestInfo
     * @param out      of type PrintWriter
     * @param personConfig
     * @author zhangting
     * @since 10-10-29 上午9:51
     */
    public void doDebugUrlRule(RequestInfo requestInfo, PrintWriter out, PersonConfig personConfig) {
        String filePath = requestInfo.getFilePath();
        String realUrl = requestInfo.getRealUrl();
        String fullUrl = requestInfo.getFullUrl();
        if (validateLocalCombo(requestInfo, out, personConfig)) {
            return;
        }
        if (findAssetsFile(filePath, personConfig)) {
            this.fileEditor.pushFileOutputStream(out, loadExistFileStream(filePath, getConfigEncoding(requestInfo, personConfig), personConfig), filePath);
        } else {
            if (!readUrlFile(realUrl, out)) {
                if (personConfig.isUcoolAssetsDebug()) {
                    //debug mode下如果请求-min的源文件a.js，会出现请求a.source.js的情况，到这里处理
                    //如果到这里那就说明线上都没有改文件，即使返回压缩的文件也没问题，只要保证尽可能的命中cache
                    requestInfo.setFilePath(filePath.replace(".source", ""));
                    requestInfo.setRealUrl(realUrl.replace(".source", ""));
                    doDebugUrlRuleCopy(requestInfo, out, personConfig);
                } else {
                    //最后的保障，如果缓存失败了，从线上取吧
                    readUrlFile(fullUrl, out);
                }
            }
        }
    }


    public void doDebugUrlRuleCopy(RequestInfo requestInfo, PrintWriter out, PersonConfig personConfig) {
        if (validateLocalCombo(requestInfo, out, personConfig)) {
            return;
        }
        if (findAssetsFile(requestInfo.getFilePath(), personConfig)) {
            this.fileEditor.pushFileOutputStream(out, loadExistFileStream(requestInfo.getFilePath(), getConfigEncoding(requestInfo, personConfig), personConfig), requestInfo.getFilePath());
        } else {
            //最后的保障，如果缓存失败了，从线上取吧
            readUrlFile(requestInfo.getFullUrl(), out);
        }
    }

    /**
     * 根据当前用户的配置获取文件编码
     *
     * @param requestInfo
     * @param personConfig
     * @return
     */
    private String getConfigEncoding(RequestInfo requestInfo, PersonConfig personConfig) {
        //read properties
        Properties p = new Properties();
        StringBuilder sb = new StringBuilder();
        sb.append(configCenter.getWebRoot()).append(configCenter.getUcoolAssetsRoot()).append(personConfig.getUserRootDir()).append(PERSON_CONFIG_NAME);
        try {
            File comboFile = new File(sb.toString());
            if(comboFile.exists() && comboFile.canRead()) {
                FileReader fileReader = new FileReader(comboFile);
                p.load(fileReader);
                fileReader.close();
            }
        } catch (IOException e) {
        }
        if(!p.isEmpty()) {
            String utfFiles = p.getProperty(ENCODING_CORRECT);
            String[] utfLists = utfFiles.split(",");
            for (String utfList : utfLists) {
                if (requestInfo.getFilePath().indexOf(utfList) != -1) {
                    return "utf-8";
                }
            }
        }
        return "gbk";
    }

    private boolean validateLocalCombo(RequestInfo requestInfo, PrintWriter out, PersonConfig personConfig) {
        //当用户目录配置了特殊需要反向combo的文件，需要特殊特殊处理
        if(personConfig.isEnableLocalCombo()) {
            //read properties
            Properties p = new Properties();
            StringBuilder sb = new StringBuilder();
            sb.append(configCenter.getWebRoot()).append(configCenter.getUcoolAssetsRoot()).append(personConfig.getUserRootDir()).append(LOCAL_COMBO_CONFIG_NAME);
            try {
                File comboFile = new File(sb.toString());
                if(comboFile.exists() && comboFile.canRead()) {
                    FileReader fileReader = new FileReader(comboFile);
                    p.load(fileReader);
                    fileReader.close();
                }
            } catch (IOException e) {
            }
            if(!p.isEmpty()) {
                //url replace
                boolean matchUrl = false;
                for (Map.Entry<Object, Object> objectObjectEntry : p.entrySet()) {
                    if (((String) objectObjectEntry.getKey()).indexOf(requestInfo.getFilePath()) != -1) {
                        String newUrl = (String) objectObjectEntry.getValue();
                        newUrl = newUrl.replace("{baseUrl}", requestInfo.getServerName());
                        newUrl = "http://" + newUrl + UrlTools.getParam(requestInfo.getRealUrl());
                        //简单校验，不能同一文件循环请求
                        if (newUrl.indexOf((String) objectObjectEntry.getKey()) == -1) {
                            requestInfo.setRealUrl(newUrl);
                            matchUrl = true;
                            break;
                        }
                    }
                }
                if (matchUrl) {
                    String realUrl = requestInfo.getRealUrl();
                    if(realUrl.indexOf("?") != -1) {
                        realUrl += ("?pcname=" + personConfig.getUserDO().getHostName());
                    } else {
                        realUrl += ("&pcname=" + personConfig.getUserDO().getHostName());
                    }
                    requestInfo.setRealUrl(realUrl);
                    out.println("/*ucool local combo matched:"+requestInfo.getFilePath()+ "*/");
                    readUrlFile(requestInfo.getRealUrl(), out);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 从assets目录中查找本地修改过的文件
     *
     *
     * @param filePath of type String
     * @param personConfig
     * @return boolean
     * @author zhangting
     * @since 2010-8-19 14:49:26
     */
    private boolean findAssetsFile(String filePath, PersonConfig personConfig) {
        if (personConfig.isEnableAssets()) {
            StringBuilder sb = new StringBuilder();
            sb.append(configCenter.getWebRoot()).append(personConfig.getUcoolAssetsRoot()).append(filePath);
            return this.fileEditor.findFile(sb.toString());
        }
        return false;
    }

    /**
     * 根据编码返回新的文件流
     *
     *
     * @param filePath of type String
     * @param encoding of type String
     * @param personConfig
     * @return InputStreamReader
     */
    private InputStreamReader loadExistFileStream(String filePath, String encoding, PersonConfig personConfig) {
        String root = personConfig.getUcoolAssetsRoot();
        StringBuilder sb = new StringBuilder();
        sb.append(configCenter.getWebRoot()).append(root).append(filePath);
        try {
            return this.fileEditor.loadFileStream(sb.toString(), encoding);
        } catch (FileNotFoundException e) {

        } catch (UnsupportedEncodingException e) {
        }
        return null;
    }

    /**
     * Method readUrlFile ...
     *
     * @param fullUrl of type String
     * @param out     of type PrintWriter
     * @return
     */
    private boolean readUrlFile(String fullUrl, PrintWriter out) {
        try {
            URL url = new URL(fullUrl);
            String encoding = "gbk";
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), encoding));
            return fileEditor.pushStream(out, in, fullUrl, false);
        } catch (Exception e) {
        }
//        out.flush();
        return false;
    }
}
