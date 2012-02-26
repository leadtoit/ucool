package action

import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServletRequest

/**
 * Created by IntelliJ IDEA.
 * User: czy-thinkpad
 * Date: 11-12-18
 * Time: ÏÂÎç11:57
 * To change this template use File | Settings | File Templates.
 */
abstract class BaseAction implements Action {

    def beforeProcess(HttpServletRequest request) {

    }

    def process(HttpServletRequest request, HttpServletResponse response) {
        beforeProcess(request);
        def result = processing(request, response)
        afterProcess(response, result);
    }

    def abstract processing(HttpServletRequest request, HttpServletResponse response);

    def afterProcess(HttpServletResponse response, result) {
        def re = '{\"result\":\"${result[0]}\", \"value\":\"${result[0]}\"';
        def writer = response.getWriter();
        writer.print(re);
    }
}