package common;

import web.handler.impl.PersonConfigHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 2010-9-23 13:18:10
 */
public class SuffixDispatcher {

    private DispatchMapping dispatchMapping;

    private ConfigCenter configCenter;

    public void setConfigCenter(ConfigCenter configCenter) {
        this.configCenter = configCenter;
    }

    public void setDispatchMapping(DispatchMapping dispatchMapping) {
        this.dispatchMapping = dispatchMapping;
    }

    /**
     * Method dispatch ...
     *
     * @param request  of type HttpServletRequest
     * @param response of type HttpServletResponse
     */
    public void dispatch(HttpServletRequest request,
                         HttpServletResponse response) throws IOException, ServletException {
        //����ҳ
        if (request.getRequestURI().equals("/pz") || request.getRequestURI().equals("/setting")) {
            String target = "ppz.jsp";
            request.getRequestDispatcher(target).forward(request, response);
            return;
        }
        String url = (String) request.getAttribute("realUrl");
        //assets�õ���࣬�������ж�
        if (url.contains(".js") || url.contains(".css")) {
            if (url.contains(configCenter.getUcoolComboDecollator())) {
                //֧��combo�ļ�
                this.dispatchMapping.getMapping("combo").doHandler(request, response);
            } else {
                this.dispatchMapping.getMapping("assets").doHandler(request, response);
            }
        } else if (url.contains(".png") || url.contains(".gif") || url.contains(".ico")) {
            //ͼƬ���� Ŀǰ��png,gif,ico
            this.dispatchMapping.getMapping("png").doHandler(request, response);
        } else if (url.contains(".htm")) {
            // htmҳ�洦��
            this.dispatchMapping.getMapping("htm").doHandler(request, response);
        } else {
            // ������ʽ�Ĵ���Ŀǰ����swf��xml
            this.dispatchMapping.getMapping("other").doHandler(request, response);
        }
    }

}
