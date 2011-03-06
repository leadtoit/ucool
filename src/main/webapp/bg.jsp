<%@ page import="org.springframework.web.context.WebApplicationContext" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="common.PersonConfig" %>
<%@ page import="common.tools.DirSyncTools" %>
<%--
  Created by IntelliJ IDEA.
  User: zhangting
  Date: 11-1-20
  Time: 下午3:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="biz.file.FileEditor" %>
<%@ page import="common.ConfigCenter" %>
<%@ page import="common.PersonConfig" %>
<%@ page import="dao.UserDAO" %>
<%@ page import="org.springframework.web.context.WebApplicationContext" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="web.handler.impl.PersonConfigHandler" %>
<%@ page import="common.tools.DirSyncTools" %>
<%
    WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServletContext());
    ConfigCenter configCenter = (ConfigCenter) wac.getBean("configCenter");
    FileEditor fileEditor = (FileEditor) wac.getBean("fileEditor");
    PersonConfigHandler personConfigHandler = (PersonConfigHandler) wac.getBean("personConfigHandler");
    UserDAO userDAO = (UserDAO) wac.getBean("userDAO");
    String pid = request.getParameter("pid");
    String callback = request.getParameter("callback");
    DirSyncTools dirSyncTools = (DirSyncTools) wac.getBean("dirSyncTools");

    PersonConfig personConfig = personConfigHandler.doHandler(request);
    if (pid != null) {
        if (pid.equalsIgnoreCase("fuckie")) {
            request.getSession().removeAttribute("personConfig");
            request.getSession().invalidate();
            out.print("clear your session ok");
        } else if(pid.equalsIgnoreCase("check")) {
            out.println("Pc name:" + request.getRemoteHost());
            out.println("<br/>");
            out.println("Directory binded:" + personConfig.getUserDO().getName());
            out.println("<br/>");
            out.println("Directory config:" + personConfig.getUserDO().getConfig());
            out.println("<br/>");
        }
    }

%>