<%--
  Created by IntelliJ IDEA.
  User: czy-dell
  Date: 11-10-11
  Time: 上午10:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="common.ConfigCenter" %>
<%@ page import="org.springframework.web.context.WebApplicationContext" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="common.PersonConfig" %>
<%@ page import="biz.file.FileEditor" %>
<%@ page import="java.util.List" %>
<%@ page import="web.handler.impl.PersonConfigHandler" %>
<%@ page import="common.tools.DirSyncTools" %>
<%@ page import="dao.UserDAO" %>
<%@ page import="common.tools.CookieUtils" %>
<%@ page import="dao.entity.UserDO" %>
<%@ page import="java.util.Map" %>
<html>
<head><title>Simple jsp page</title></head>
<body>
    <%
        WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServletContext());
        ConfigCenter configCenter = (ConfigCenter) wac.getBean("configCenter");
        PersonConfigHandler personConfigHandler  = (PersonConfigHandler) wac.getBean("personConfigHandler");
        PersonConfig personConfig = personConfigHandler.doHandler(request);
        FileEditor fileEditor = (FileEditor) wac.getBean("fileEditor");
        CookieUtils cookieUtils = (CookieUtils)wac.getBean("cookieUtils");
        Cookie cookie1 = cookieUtils.getCookie(request.getCookies(), CookieUtils.DEFAULT_KEY);

        out.println("当前session中的guid为:" + request.getSession().getAttribute(request.getSession().getId()) + "<br>");
        out.println("当前cookie中的guid为:" + cookie1 == null? "":cookie1.getValue() + "<br>");
    %>
    <fieldset>
        <legend>操作:</legend>
        <ul>
            <li><a href="sync.jsp?op=new">完全清理（会重新生成一个guid）</a></li>
            <li><a href="sync.jsp?op=clean">清理guid</a></li>
        </ul>

      </fieldset>

    <%
        String op = request.getParameter("op");
        if("news".equals(op)) {
            String tempGuid1 = request.getSession().getAttribute(request.getSession().getId()).toString();
            request.getSession().removeAttribute(request.getSession().getId());
            request.getSession().invalidate();
            out.println("session已经清理<br>");

            Cookie cookie = cookieUtils.getCookie(request.getCookies(), CookieUtils.DEFAULT_KEY);
            String tempGuid2 = cookie.getValue();
            cookie.setMaxAge(0);
            response.addCookie(cookie);
            
            out.println("cookie已经清理<br>");
            Map<String, UserDO> userCache = personConfigHandler.getUserCache();
            userCache.remove(tempGuid1);
            if(userCache.containsKey(tempGuid2)) {
                userCache.remove(tempGuid2);
            }
            out.println("useCache已经清理<br>");
        } else if("clean".equals(op)) {
            request.getSession().removeAttribute(request.getSession().getId());
            request.getSession().invalidate();
            out.println("session已经清理<br>");

            Cookie cookie = cookieUtils.getCookie(request.getCookies(), CookieUtils.DEFAULT_KEY);
            cookie.setMaxAge(0);
            response.addCookie(cookie);
            out.println("cookie已经清理<br>");
        }
    %>
</body>
</html>