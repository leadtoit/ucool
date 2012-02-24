package web.url;

import biz.file.FileEditor;
import biz.url.LocalComboExecutor;
import biz.url.UrlReader;
import common.ConfigCenter;
import common.MyConfig;
import common.PersonConfig;
import dao.entity.RequestInfo;
import tools.JSONFilter;

import javax.servlet.ServletOutputStream;
import java.io.*;
import java.util.Properties;

/**
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 2010-10-2 13:33:26
 */
public class UrlExecutor {

    private FileEditor fileEditor;

    private ConfigCenter configCenter;

    private UrlReader urlReader;

    private JSONFilter jsonFilter;

    private LocalComboExecutor localComboExecutor;

    public void setFileEditor(FileEditor fileEditor) {
        this.fileEditor = fileEditor;
    }

    public void setConfigCenter(ConfigCenter configCenter) {
        this.configCenter = configCenter;
    }

    public void setUrlReader(UrlReader urlReader) {
        this.urlReader = urlReader;
    }

    public void setJsonFilter(JSONFilter jsonFilter) {
        this.jsonFilter = jsonFilter;
    }

    public void setLocalComboExecutor(LocalComboExecutor localComboExecutor) {
        this.localComboExecutor = localComboExecutor;
    }

    /**
     * 为debug模式特殊处理url请求，不走cache
     *
     * @param requestInfo
     * @param personConfig
     * @author zhangting
     * @since 10-10-29 上午9:51
     */
    public void doDebugUrlRule(RequestInfo requestInfo, PersonConfig personConfig) {
        String filePath = requestInfo.getFilePath();
        String realUrl = requestInfo.getRealUrl();
        String curMappingPath = null;

        // 防止本地映射开启时没填映射路径，这样还可以走旧逻辑
        if(personConfig.getUserDO().getMappingPath() != null && !"".equals(personConfig.getUserDO().getMappingPath())) {
            // 本地映射不走服务器assets目录
            String usedMappings = this.jsonFilter.getUsedMappings(personConfig.getUserDO().getMappingPath());
            if(!"".equals(usedMappings)) {
                String[] mappingPaths = usedMappings.split(";");
                // 取得当前的映射路径
                for (String mappingPath : mappingPaths) {
                    if(requestInfo.getFilePath().startsWith(mappingPath)){
                        curMappingPath = mappingPath;
                        requestInfo.setCurMappingPath(curMappingPath);
                        break;
                    }
                }
            }
        }

        if(personConfig.isEnableLocalMapping() && curMappingPath != null) {
            // 将ip替换为客户端的，并且将目录映射掉
            if(realUrl.contains("assets.lp.alibaba.com")) {
                realUrl = realUrl.replaceAll("assets.lp.alibaba.com", requestInfo.getClientAddr() + ":" + configCenter.getUcoolProxyClientPort());
            } else {
                realUrl = realUrl.replaceAll(configCenter.getUcoolProxyIp(), requestInfo.getClientAddr() + ":" + configCenter.getUcoolProxyClientPort());
            }

            realUrl = realUrl.replaceAll(curMappingPath, "");
            requestInfo.setRealUrl(realUrl);

            String fullUrl = requestInfo.getFullUrl();
            if(fullUrl.contains("assets.lp.alibaba.com")) {
                fullUrl = fullUrl.replaceAll("assets.lp.alibaba.com", requestInfo.getClientAddr() + ":" + configCenter.getUcoolProxyClientPort());
            } else {
                fullUrl = fullUrl.replaceAll(configCenter.getUcoolProxyIp(), requestInfo.getClientAddr() + ":" + configCenter.getUcoolProxyClientPort());
            }

            fullUrl = fullUrl.replaceAll(curMappingPath, "");
            requestInfo.setFullUrl(fullUrl);

            // 判断本地combo
            if(personConfig.isEnableLocalCombo() && !requestInfo.isAfterLocalCombo()) {
                StringBuilder sb = new StringBuilder();
                sb.append("http://").append(requestInfo.getClientAddr()).append(":")
                            .append(configCenter.getUcoolProxyClientPort())
                            .append(MyConfig.LOCAL_COMBO_CONFIG_NAME);
                if(localComboExecutor.executeLocalCombo(localComboExecutor.getPropertiesByUrl(sb.toString()), requestInfo, personConfig)) {
                    // 如果已经被本地代理掉了，就return
                    return;
                }
            }
            //直接请求客户端
            if (!urlReader.readUrlFile(requestInfo)) {
                // 图片不用重复请求
                if (!requestInfo.getType().equals("assets")) {
                    return;
                }
                if (personConfig.isUcoolAssetsDebug()) {
                    //debug mode下如果请求-min的源文件a.js，会出现请求a.source.js的情况，到这里处理
                    //如果到这里那就说明线上都没有改文件，即使返回压缩的文件也没问题，只要保证尽可能的命中cache
                    requestInfo.setFilePath(filePath.replace(".source", ""));
                    requestInfo.setRealUrl(realUrl.replace(".source", ""));
                    doDebugUrlRuleCopy(requestInfo, personConfig);
                } else {
                    //最后的保障，如果缓存失败了，从线上取吧
                    requestInfo.setRealUrl(requestInfo.getFullUrl());
                    urlReader.readUrlFile(requestInfo);
                }
            }
        } else {
            // 判断本地combo
            if(personConfig.isEnableLocalCombo()) {
                StringBuilder sb = new StringBuilder();
                sb.append(configCenter.getWebRoot()).append(configCenter.getUcoolAssetsRoot()).append(personConfig.getUserRootDir()).append(MyConfig.LOCAL_COMBO_CONFIG_NAME);
                if(localComboExecutor.executeLocalCombo(localComboExecutor.getPropertiesByFile(sb.toString()), requestInfo, personConfig)) {
                    // 如果已经被本地代理掉了，就return
                    return;
                }
            }

            //TODO 这里是临时方案，要重构
            String filePath2 = filePath.replace(".source", "");
            boolean useFilePath2 = false;

            if (findAssetsFile(filePath, personConfig) || (useFilePath2 = findAssetsFile(filePath2, personConfig))) {
                try {
                    requestInfo.setRealUrl(requestInfo.getFilePath());
                    this.urlReader.pushStream(requestInfo, loadExistFileStream(useFilePath2 ? filePath2 : filePath, personConfig));
                } catch (IOException e) {
                    //捕获所有异常，这里有可能缓存失败，所以取不到文件
                    System.out.println("file has exception" + e);
                }
            } else {
                if (!urlReader.readUrlFile(requestInfo)) {
                    // 图片不用重复请求
                    if (!requestInfo.getType().equals("assets")) {
                        return;
                    }
                    if (personConfig.isUcoolAssetsDebug()) {
                        //debug mode下如果请求-min的源文件a.js，会出现请求a.source.js的情况，到这里处理
                        //如果到这里那就说明线上都没有改文件，即使返回压缩的文件也没问题，只要保证尽可能的命中cache
                        requestInfo.setFilePath(filePath.replace(".source", ""));
                        requestInfo.setRealUrl(realUrl.replace(".source", ""));
                        doDebugUrlRuleCopy(requestInfo, personConfig);
                    } else {
                        //最后的保障，如果缓存失败了，从线上取吧
                        requestInfo.setRealUrl(requestInfo.getFullUrl());
                        urlReader.readUrlFile(requestInfo);
                    }
                }
            }
        }
    }


    public void doDebugUrlRuleCopy(RequestInfo requestInfo, PersonConfig personConfig) {
        if(personConfig.isEnableLocalMapping() && requestInfo.getFilePath().startsWith(requestInfo.getCurMappingPath())) {
            // 判断本地combo
            if(personConfig.isEnableLocalCombo() && !requestInfo.isAfterLocalCombo()) {
                StringBuilder sb = new StringBuilder();
                sb.append("http://").append(requestInfo.getClientAddr()).append(":")
                            .append(configCenter.getUcoolProxyClientPort())
                            .append(MyConfig.LOCAL_COMBO_CONFIG_NAME);
                if(localComboExecutor.executeLocalCombo(localComboExecutor.getPropertiesByUrl(sb.toString()), requestInfo, personConfig)) {
                    // 如果已经被本地代理掉了，就return
                    return;
                }
            }

            requestInfo.setRealUrl(requestInfo.getFullUrl());
            urlReader.readUrlFile(requestInfo);
        } else {
            // 判断本地combo
            if(personConfig.isEnableLocalCombo()) {
                StringBuilder sb = new StringBuilder();
                sb.append(configCenter.getWebRoot()).append(configCenter.getUcoolAssetsRoot()).append(personConfig.getUserRootDir()).append(MyConfig.LOCAL_COMBO_CONFIG_NAME);
                if(localComboExecutor.executeLocalCombo(localComboExecutor.getPropertiesByFile(sb.toString()), requestInfo, personConfig)) {
                    // 如果已经被本地代理掉了，就return
                    return;
                }
            }

            //最后的保障，如果缓存失败了，从线上取吧
            requestInfo.setRealUrl(requestInfo.getFullUrl());
            urlReader.readUrlFile(requestInfo);
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
            urlReader.readUrlFileForPng(requestInfo, realUrl, out);
        }
    }

    /**
     * 根据当前用户的配置获取文件编码
     *
     * @param requestInfo
     * @param personConfig
     * @return
     * @Deprecated
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
                    if (requestInfo.getFilePath().contains(gbkList)) {
                        return "gbk";
                    }
                }
            }
            String utfFiles = p.getProperty(MyConfig.ENCODING_CORRECT);
            if(utfFiles != null && !utfFiles.isEmpty()) {
                String[] utfLists = utfFiles.split(",");
                for (String utfList : utfLists) {
                    if (requestInfo.getFilePath().contains(utfList)) {
                        return "utf-8";
                    }
                }
            }
        }
        return "gbk";
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

}
