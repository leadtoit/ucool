package web.handler.impl;

import biz.url.UrlReader;
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
 * jsonpµÄhandler
 *
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 2010-9-23 13:34:28
 */
public class HtmHandler implements Handler {
    private UrlTools urlTools;

    private UrlReader urlReader;

    public void setUrlTools(UrlTools urlTools) {
        this.urlTools = urlTools;
    }

    public void setUrlReader(UrlReader urlReader) {
        this.urlReader = urlReader;
    }

    /**
     * Method doHandler ...
     *
     * @param request of type HttpServletRequest
     * @param response of type HttpServletResponse
     * @throws IOException when
     * @throws ServletException when
     */
    @Override
    public void doHandler(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String directURL = request.getRequestURL().toString();

        try {
            directURL = urlTools.urlFilter(directURL, true, null);
            URL url = new URL(directURL);
            urlReader.pushStream(response.getOutputStream(), url.openStream(), null, true);
        } catch (Exception e) {
        }

    }

}
