package web.handler.impl;

import common.tools.HttpTools;
import common.PersonConfig;
import dao.entity.RequestInfo;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 2010-9-24 11:04:09
 */
public class ComboHandler extends AssetsHandler {
    /**
     * Method doHandler ...
     *
     * @param request  of type HttpServletRequest
     * @param response of type HttpServletResponse
     * @throws java.io.IOException            when
     * @throws javax.servlet.ServletException when
     */
    public void doHandler(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        PersonConfig personConfig = getPersonConfigHandler().doHandler(request);
        /**
         * combo url example:
         *
         * http://a.tbcdn.cn/p/??header/header-min.css,
         * fp/2010c/fp-base-min.css,fp/2010c/fp-channel-min.css,
         * fp/2010c/fp-product-min.css,fp/2010c/fp-mall-min.css,
         * fp/2010c/fp-category-min.css,fp/2010c/fp-sub-min.css,
         * fp/2010c/fp-gdp4p-min.css,fp/2010c/fp-css3-min.css,
         * fp/2010c/fp-misc-min.css?t=20100902.css
         *
         */

        /**
         * combo的文件必须拆开后再combo输出
         */
        String realUrl = (String) request.getAttribute("realUrl");
        String filePath = (String) request.getAttribute("filePath"); // e.g.:/p/

        String[] firstCut = realUrl.split(HttpTools.filterSpecialChar(getConfigCenter().getUcoolComboDecollator()));
        String pathPrefix = firstCut[0];    // e.g.:http://a.tbcdn.cn/p/
        String[] allFiles = firstCut[1].split(HttpTools.filterSpecialChar(","));

        if (realUrl.indexOf(".css") != -1) {
            response.setContentType("text/css");
        } else {
            response.setContentType("application/x-javascript");
        }

        boolean isOnline = getConfigCenter().getUcoolOnlineDomain().indexOf(request.getServerName()) != -1;
        boolean isDebugMode = personConfig.isUcoolAssetsDebug() || HttpTools.isReferDebug(request);
        for (String everyFile : allFiles) {
            //过滤时间戳和一些参数
            everyFile = everyFile.replace("\\?.*$", "");
            // e.g.:header/header-min.css
            //拼出单个url，然后的逻辑和单文件相同
            String singleFilePath = filePath + everyFile;
            String singleRealUrl = pathPrefix + everyFile;
            singleRealUrl= attachOper(singleRealUrl, request);
            String singleFullUrl = singleRealUrl;

            //获取源文件url
            if (isDebugMode) {
                singleFilePath = getUrlTools().debugMode(singleFilePath, singleFullUrl);
                singleRealUrl = getUrlTools().debugMode(singleRealUrl, singleFullUrl);
            }

            singleRealUrl = getUrlTools().urlFilter(singleRealUrl, isOnline, personConfig);
            singleFullUrl = getUrlTools().urlFilter(singleFullUrl, isOnline, personConfig);

            //尝试debug下所有的直接走source，不走cache
            //线上缓存已经迁移至ucool-proxy
            RequestInfo requestInfo = new RequestInfo(request);
            requestInfo.setFilePath(singleFilePath);
            requestInfo.setRealUrl(singleRealUrl);
            requestInfo.setFullUrl(singleFullUrl);
            requestInfo.setUrlCombo(true);
            getUrlExecutor().doDebugUrlRule(requestInfo, response, personConfig);
        }

    }

}
