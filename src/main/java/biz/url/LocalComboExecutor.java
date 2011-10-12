package biz.url;

import common.PersonConfig;
import common.tools.UrlTools;
import dao.entity.RequestInfo;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

/**
 * localcombo 执行类
 * 
 * Created by IntelliJ IDEA.
 * User: czy-thinkpad
 * Date: 11-6-6
 * Time: 下午4:16
 * To change this template use File | Settings | File Templates.
 */
public class LocalComboExecutor {

    private UrlReader urlReader;

    public void setUrlReader(UrlReader urlReader) {
        this.urlReader = urlReader;
    }

    public Properties getPropertiesByFile(String propertiesPath) {
        //read properties
        Properties p = new Properties();
        try {
            File comboFile = new File(propertiesPath);
            if (comboFile.exists() && comboFile.canRead()) {
                FileReader fileReader = new FileReader(comboFile);
                p.load(fileReader);
                fileReader.close();
            }
        } catch (IOException e) {
        }

        return p;
    }

    public Properties getPropertiesByUrl(String propertiesUrl) {
        Properties p = new Properties();
        URL url = null;
        try {
            url = new URL(propertiesUrl);
            p.load(url.openStream());
        } catch (IOException e) {
            System.out.println(e);
        }
        return p;
    }

    public boolean executeLocalCombo(Properties p, RequestInfo requestInfo, PersonConfig personConfig) {
        //读取combo.properties效率太低，改成只读一次，之后的配置从session中取
        if(!p.isEmpty()) {
            //url replace
            boolean matchUrl = false;
            for (Map.Entry<Object, Object> objectObjectEntry : p.entrySet()) {
                if (((String) objectObjectEntry.getKey()).indexOf(requestInfo.getFilePath()) != -1) {
                    String newUrl = (String) objectObjectEntry.getValue();
                    newUrl = newUrl.replace("{baseUrl}", "localhost");
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
                int fromIndex = realUrl.indexOf("??");
                if(fromIndex == -1) {
                    fromIndex = 0;
                } else {
                    fromIndex += 2;
                }
                if(realUrl.indexOf("?", fromIndex) != -1) {
                    realUrl += ("&ucool-guid=" + personConfig.getUserDO().getHostName());
                } else {
                    realUrl += ("?ucool-guid=" + personConfig.getUserDO().getHostName());
                }
                requestInfo.setRealUrl(realUrl);
                requestInfo.setLocalCombo(true);

                urlReader.readUrlFile(requestInfo);
                return true;
            }
        }
        return false;
    }
}
