package web.url;

import biz.file.FileEditor;
import biz.url.UrlReader;
import common.ConfigCenter;
import common.MyConfig;
import common.PersonConfig;
import common.tools.UrlTools;
import dao.entity.RequestInfo;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

/**
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 2010-10-2 13:33:26
 */
public class UrlExecutor {

    private FileEditor fileEditor;

    private ConfigCenter configCenter;

    private UrlReader urlReader;

    public void setFileEditor(FileEditor fileEditor) {
        this.fileEditor = fileEditor;
    }

    public void setConfigCenter(ConfigCenter configCenter) {
        this.configCenter = configCenter;
    }

    public void setUrlReader(UrlReader urlReader) {
        this.urlReader = urlReader;
    }

    /**
     * 为debug模式特殊处理url请求，不走cache
     *
     *
     * @param requestInfo
     * @param response
     * @param personConfig
     * @author zhangting
     * @since 10-10-29 上午9:51
     */
    public void doDebugUrlRule(RequestInfo requestInfo, HttpServletResponse response, PersonConfig personConfig) {
        String filePath = requestInfo.getFilePath();
        String realUrl = requestInfo.getRealUrl();
        String curMappingPath = null;
        
        if (validateLocalCombo(requestInfo, response, personConfig)) {
            return;
        }
        if(personConfig.getUserDO().getMappingPath() != null && !"".equals(personConfig.getUserDO().getMappingPath())) {
            // 本地映射不走服务器assets目录
            String[] mappingPaths = personConfig.getUserDO().getMappingPath().split(";");
            // 取得当前的映射路径
            for (String mappingPath : mappingPaths) {
                if(requestInfo.getFilePath().startsWith(mappingPath)){
                    curMappingPath = mappingPath;
                    break;
                }
            }
        }
        if(personConfig.isEnableLocalMapping() && curMappingPath != null) {
            // 将ip替换为客户端的，并且将目录映射掉
            realUrl = realUrl.replaceAll(configCenter.getUcoolProxyIp(), requestInfo.getClientAddr() + ":" + configCenter.getUcoolProxyClientPort());
            realUrl = realUrl.replaceAll(curMappingPath, "");
            requestInfo.setRealUrl(realUrl);
            String fullUrl = requestInfo.getFullUrl();
            fullUrl = fullUrl.replaceAll(configCenter.getUcoolProxyIp(), requestInfo.getClientAddr() + ":" + configCenter.getUcoolProxyClientPort());
            fullUrl = fullUrl.replaceAll(curMappingPath, "");
            requestInfo.setFullUrl(fullUrl);
            //直接请求客户端
            if (!readUrlFile(requestInfo, response)) {
                // 图片不用重复请求
                if (!requestInfo.getType().equals("assets")) {
                    return;
                }
                if (personConfig.isUcoolAssetsDebug()) {
                    //debug mode下如果请求-min的源文件a.js，会出现请求a.source.js的情况，到这里处理
                    //如果到这里那就说明线上都没有改文件，即使返回压缩的文件也没问题，只要保证尽可能的命中cache
                    requestInfo.setFilePath(filePath.replace(".source", ""));
                    requestInfo.setRealUrl(realUrl.replace(".source", ""));
                    doDebugUrlRuleCopy(requestInfo, response, personConfig);
                } else {
                    //最后的保障，如果缓存失败了，从线上取吧
                    requestInfo.setRealUrl(requestInfo.getFullUrl());
                    readUrlFile(requestInfo, response);
                }
            }
        } else {
            if (findAssetsFile(filePath, personConfig)) {
                try {
                    requestInfo.setRealUrl(requestInfo.getFilePath());
                    this.urlReader.pushStream(requestInfo, response, loadExistFileStream(filePath, personConfig));
                } catch (IOException e) {
                    //捕获所有异常，这里有可能缓存失败，所以取不到文件
                    System.out.println("file has exception" + e);
                }
            } else {
                if (!readUrlFile(requestInfo, response)) {
                    // 图片不用重复请求
                    if (!requestInfo.getType().equals("assets")) {
                        return;
                    }
                    if (personConfig.isUcoolAssetsDebug()) {
                        //debug mode下如果请求-min的源文件a.js，会出现请求a.source.js的情况，到这里处理
                        //如果到这里那就说明线上都没有改文件，即使返回压缩的文件也没问题，只要保证尽可能的命中cache
                        requestInfo.setFilePath(filePath.replace(".source", ""));
                        requestInfo.setRealUrl(realUrl.replace(".source", ""));
                        doDebugUrlRuleCopy(requestInfo, response, personConfig);
                    } else {
                        //最后的保障，如果缓存失败了，从线上取吧
                        requestInfo.setRealUrl(requestInfo.getFullUrl());
                        readUrlFile(requestInfo, response);
                    }
                }
            }
        }
    }


    public void doDebugUrlRuleCopy(RequestInfo requestInfo, HttpServletResponse response, PersonConfig personConfig) {
        if(personConfig.isEnableLocalMapping() && requestInfo.getFilePath().startsWith(personConfig.getUserDO().getMappingPath())) {
            requestInfo.setRealUrl(requestInfo.getFullUrl());
            readUrlFile(requestInfo, response);
        } else {
            if (validateLocalCombo(requestInfo, response, personConfig)) {
                return;
            }
            if (findAssetsFile(requestInfo.getFilePath(), personConfig)) {
                try {
                    requestInfo.setRealUrl(requestInfo.getFilePath());
                    this.urlReader.pushStream(requestInfo, response, loadExistFileStream(requestInfo.getFilePath(), personConfig));
                } catch (IOException e) {
                    //捕获所有异常，这里有可能缓存失败，所以取不到文件
                    System.out.println("file has exception" + e);
                }
            } else {
                //最后的保障，如果缓存失败了，从线上取吧
                requestInfo.setRealUrl(requestInfo.getFullUrl());
                readUrlFile(requestInfo, response);
            }
        }
    }

    public void doDebugUrlRuleForPng(RequestInfo requestInfo, ServletOutputStream out, PersonConfig personConfig) {
        String filePath = requestInfo.getFilePath();
        String realUrl = requestInfo.getRealUrl();

        if (findAssetsFile(filePath, personConfig)) {
            try {
                this.urlReader.pushStream(out, loadExistFileStream(filePath, personConfig), filePath, !requestInfo.getType().equals("assets"));
            } catch (IOException e) {
                //捕获所有异常，这里有可能缓存失败，所以取不到文件
                System.out.println("file has exception" +  e);
            }
        } else {
            readUrlFileForPng(requestInfo, realUrl, out);
        }
    }

    /**
     * 根据当前用户的配置获取文件编码
     *
     * @param requestInfo
     * @param personConfig
     * @return
     */
    @Deprecated
    private String getConfigEncoding(RequestInfo requestInfo, PersonConfig personConfig) {
        //read properties
        Properties p = new Properties();
        StringBuilder sb = new StringBuilder();
        sb.append(configCenter.getWebRoot()).append(configCenter.getUcoolAssetsRoot()).append(personConfig.getUserRootDir()).append(MyConfig.PERSON_CONFIG_NAME);
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
            String whileList = p.getProperty(MyConfig.ENCODING_CORRECT_WHITE_LIST);
            if(whileList != null && !whileList.isEmpty()) {
                String[] gbkLists = whileList.split(",");
                for (String gbkList : gbkLists) {
                    if (requestInfo.getFilePath().indexOf(gbkList) != -1) {
                        return "gbk";
                    }
                }
            }
            String utfFiles = p.getProperty(MyConfig.ENCODING_CORRECT);
            if(utfFiles != null && !utfFiles.isEmpty()) {
                String[] utfLists = utfFiles.split(",");
                for (String utfList : utfLists) {
                    if (requestInfo.getFilePath().indexOf(utfList) != -1) {
                        return "utf-8";
                    }
                }
            }
        }
        return "gbk";
    }

    private boolean validateLocalCombo(RequestInfo requestInfo, HttpServletResponse response, PersonConfig personConfig) {
        //当用户目录配置了特殊需要反向combo的文件，需要特殊处理
        if(personConfig.isEnableLocalCombo()) {
            //read properties
            Properties p = new Properties();
            StringBuilder sb = new StringBuilder();
            sb.append(configCenter.getWebRoot()).append(configCenter.getUcoolAssetsRoot()).append(personConfig.getUserRootDir()).append(MyConfig.LOCAL_COMBO_CONFIG_NAME);
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
                    requestInfo.setLocalCombo(true);
                    
                    readUrlFile(requestInfo, response);
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
     * 返回新的文件流
     *
     *
     * @param filePath of type String
     * @param personConfig
     * @return InputStreamReader
     */
    private InputStream loadExistFileStream(String filePath, PersonConfig personConfig) {
        String root = personConfig.getUcoolAssetsRoot();
        StringBuilder sb = new StringBuilder();
        sb.append(configCenter.getWebRoot()).append(root).append(filePath);
        try {
            return new FileInputStream(sb.toString());
        } catch (FileNotFoundException e) {

        }
        return null;
    }

    /**
     * Method readUrlFile ...
     *
     * @param requestInfo
     * @param response
     * @return
     */
    private boolean readUrlFile(RequestInfo requestInfo,  HttpServletResponse response) {
        try {
            URL url = new URL(requestInfo.getRealUrl());
            return this.urlReader.pushStream(requestInfo, response, url.openStream());
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 专为图片处理的方法
     * @param requestInfo
     * @param fullUrl
     * @param out
     * @return
     */
    private boolean readUrlFileForPng(RequestInfo requestInfo, String fullUrl, ServletOutputStream out) {
        try {
            URL url = new URL(fullUrl);
            return this.urlReader.pushStream(out, url.openStream(), fullUrl, !requestInfo.getType().equals("assets"));
        } catch (Exception e) {
        }
        return false;
    }

}
