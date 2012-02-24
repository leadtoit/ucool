<%@ page import="common.PersonConfig" %>
<%@ page import="org.springframework.web.context.WebApplicationContext" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="web.handler.impl.PersonConfigHandler" %>
<%--
  Created by IntelliJ IDEA.
  User: zhangting
  Date: 11-1-20
  Time: 下午3:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServletContext());
    PersonConfigHandler personConfigHandler = (PersonConfigHandler) wac.getBean("personConfigHandler");
    String pid = request.getParameter("pid");

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