package web.handler.impl;

import biz.file.FileEditor;
import biz.url.UrlReader;
import common.ConfigCenter;
import common.tools.UrlTools;
import web.handler.Handler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;

/**
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 2010-10-2 20:12:54
 */
public class OtherHandler implements Handler {

    private ConfigCenter configCenter;

    private UrlTools urlTools;

    private UrlReader urlReader;

    public void setConfigCenter(ConfigCenter configCenter) {
        this.configCenter = configCenter;
    }

    public void setUrlTools(UrlTools urlTools) {
        this.urlTools = urlTools;
    }

    public void setUrlReader(UrlReader urlReader) {
        this.urlReader = urlReader;
    }

    @Override
    public void doHandler(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        if (request.getRequestURI().indexOf(".swf") != -1) {
            String fullUrl = (String) request.getAttribute("fullUrl");
            fullUrl = urlTools.urlFilter(fullUrl, true, null);

            response.setContentType("application/x-shockwave-flash");
            try {
                URL url = new URL(fullUrl);
                urlReader.pushStream(response.getOutputStream(), url.openStream(), null, true);
            } catch (Exception e) {
            }
        } else if (request.getRequestURI().indexOf(".xml") != -1) {
            response.setContentType("text/xml");
            try {
                URL url = new URL("http://" + configCenter.getUcoolOnlineIp() + request.getRequestURI());
                urlReader.pushStream(response.getOutputStream(), url.openStream(), null, true);
            } catch (Exception e) {
            }
        }
    }
}
