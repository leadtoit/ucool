package web.handler.impl;

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

    public void setUrlTools(UrlTools urlTools) {
        this.urlTools = urlTools;
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

        response.setCharacterEncoding("gbk");
        PrintWriter out = response.getWriter();

        try {
            directURL = urlTools.urlFilter(directURL, true, null);
            URL url = new URL(directURL);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "gbk"));
            String line;
            while ((line = in.readLine()) != null) {
                out.println(line);
            }
            in.close();
        } catch (Exception e) {
            out.println("file not find");
        }
        out.flush();
    }

}
