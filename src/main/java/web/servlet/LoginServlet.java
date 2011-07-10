package web.servlet;

import common.SuffixDispatcher;
import common.tools.CookieUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: czy-dell
 * Date: 11-6-14
 * Time: ÏÂÎç2:39
 * To change this template use File | Settings | File Templates.
 */
public class LoginServlet extends HttpServlet {

    private CookieUtils cookieUtils;

    public void setCookieUtils(CookieUtils cookieUtils) {
        this.cookieUtils = cookieUtils;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(cookieUtils.hasCookie(request.getCookies(), CookieUtils.DEFAULT_KEY)) {
            System.out.println("has cookie:" + cookieUtils.getCookie(request.getCookies(), CookieUtils.DEFAULT_KEY).getValue());
        } else {
            for (String domain : CookieUtils.domains) {
                Cookie cookie = cookieUtils.addCookie(CookieUtils.DEFAULT_KEY, request.getRemoteAddr() , domain);
                if(cookie != null) {
                    response.addCookie(cookie);
                    System.out.println("add cookie:" + cookie.getValue());
                }
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        if (cookieUtils == null) {
            WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
            setCookieUtils((CookieUtils) context.getBean("cookieUtils"));
        }
    }
}
