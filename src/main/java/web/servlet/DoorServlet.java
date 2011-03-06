package web.servlet;

import common.SuffixDispatcher;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author <a href="mailto:zhangting@taobao.com">уем╕</a>
 * @since 2010-9-20 15:06:19
 */
public class DoorServlet extends HttpServlet {

    private SuffixDispatcher suffixDispatcher;

    public void setSuffixDispatcher(SuffixDispatcher suffixDispatcher) {
        this.suffixDispatcher = suffixDispatcher;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        if (suffixDispatcher == null) {
            WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
            setSuffixDispatcher((SuffixDispatcher) context.getBean("suffixDispatcher"));
        }
    }

    /**
     * Method doPost ...
     *
     * @param request  of type HttpServletRequest
     * @param response of type HttpServletResponse
     * @throws ServletException when
     * @throws IOException      when
     * @author zhangting
     * @since 2010-9-20 15:37:01
     */
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        //begin to dispatch
        this.suffixDispatcher.dispatch(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

}
