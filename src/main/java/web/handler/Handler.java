package web.handler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 2010-9-23 14:10:47
 */
public interface Handler {
    
    /**
     * 做最终的分配工作
     *
     * @param request of type HttpServletRequest
     * @param response of type HttpServletResponse
     * @throws IOException when
     * @throws ServletException when
     */
    void doHandler(HttpServletRequest request,
                          HttpServletResponse response) throws IOException, ServletException;
}
