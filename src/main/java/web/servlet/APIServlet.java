package web.servlet;

import action.BaseAction;
import common.ActionMapping;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: czy-thinkpad
 * Date: 11-9-18
 * Time: ÏÂÎç7:10
 * To change this template use File | Settings | File Templates.
 */
public class APIServlet extends HttpServlet {

    private ActionMapping actionMapping;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map parameterMap = request.getParameterMap();
        Object action = parameterMap.get("action");
        if(action != null) {
            BaseAction a = this.actionMapping.getMapping(action.toString());
            if(a != null) {
                a.process(request, response);
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        if (actionMapping == null) {
            WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
            this.actionMapping = (ActionMapping) context.getBean("actionMapping");
        }
    }
}
