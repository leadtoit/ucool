package web.handler.impl;

import biz.file.FileEditor;
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

    private FileEditor fileEditor;

    private ConfigCenter configCenter;

    private UrlTools urlTools;

    public void setFileEditor(FileEditor fileEditor) {
        this.fileEditor = fileEditor;
    }

    public void setConfigCenter(ConfigCenter configCenter) {
        this.configCenter = configCenter;
    }

    public void setUrlTools(UrlTools urlTools) {
        this.urlTools = urlTools;
    }

    @Override
    public void doHandler(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        if (request.getRequestURI().indexOf(".swf") != -1) {
            String fullUrl = (String) request.getAttribute("fullUrl");
            fullUrl = urlTools.urlFilter(fullUrl, true, null);
            //哥对flash没办法，无论怎么取都无法正确展现，只好302
            //            response.sendRedirect(fullUrl);
            response.setCharacterEncoding("gbk");
            response.setContentType("application/x-shockwave-flash");
            PrintWriter out = response.getWriter();
            try {
                URL url = new URL(fullUrl);
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "gbk"));
                fileEditor.pushStream(out, in, null, true);
            } catch (Exception e) {
            }
            out.flush();
        } else if (request.getRequestURI().indexOf(".xml") != -1) {
            response.setContentType("text/xml");

            PrintWriter out = response.getWriter();
            try {
                URL url = new URL("http://" + configCenter.getUcoolOnlineIp() + request.getRequestURI());
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                fileEditor.pushStream(out, in, null, true);
            } catch (Exception e) {
            }
            out.flush();
        }

    }
}
