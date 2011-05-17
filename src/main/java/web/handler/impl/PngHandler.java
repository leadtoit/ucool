package web.handler.impl;

import biz.url.UrlReader;
import common.ConfigCenter;
import common.PersonConfig;
import common.tools.UrlTools;
import dao.entity.RequestInfo;
import web.handler.Handler;
import web.url.UrlExecutor;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;

/**
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 2010-9-23 13:34:55
 */
public class PngHandler implements Handler {

    private ConfigCenter configCenter;

    private PersonConfigHandler personConfigHandler;

    private UrlExecutor urlExecutor;

    private UrlTools urlTools;

    public void setConfigCenter(ConfigCenter configCenter) {
        this.configCenter = configCenter;
    }

    public void setPersonConfigHandler(PersonConfigHandler personConfigHandler) {
        this.personConfigHandler = personConfigHandler;
    }

    public void setUrlExecutor(UrlExecutor urlExecutor) {
        this.urlExecutor = urlExecutor;
    }

    public void setUrlTools(UrlTools urlTools) {
        this.urlTools = urlTools;
    }

    @Override
    public void doHandler(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        PersonConfig personConfig = personConfigHandler.doHandler(request);
        String filePath = (String) request.getAttribute("filePath");
        String realUrl = (String) request.getAttribute("realUrl");
        boolean isOnline = configCenter.getUcoolOnlineDomain().indexOf(request.getServerName()) != -1;
        //´¦ÀíÏÂpng
        if(request.getRequestURI().indexOf("png") != -1) {
            response.setContentType("image/png");
        } else if(request.getRequestURI().indexOf("gif") != -1) {
            response.setContentType("image/gif");
        } else {
            response.setContentType("image/x-icon");
        }
        String env = "online";
        if(request.getRequestURL().indexOf("assets.daily.taobao.net") != -1) {
            env = "daily";
        }

        String fullUrl = "http://"+ configCenter.getUcoolOnlineIp() + request.getRequestURI();
        if(fullUrl.indexOf("?") != -1) {
            fullUrl += "&env=" + env;
        } else {
            fullUrl += "?env=" + env;
        }
        realUrl = urlTools.urlFilter(realUrl, isOnline, personConfig);
        fullUrl = urlTools.urlFilter(fullUrl, isOnline, personConfig);
        PrintWriter out = response.getWriter();
        RequestInfo requestInfo = new RequestInfo(request);
        requestInfo.setFilePath(filePath);
        requestInfo.setRealUrl(realUrl);
        requestInfo.setFullUrl(fullUrl);
        requestInfo.setType("png");
        urlExecutor.doDebugUrlRule(requestInfo, out, personConfig);

    }
}
