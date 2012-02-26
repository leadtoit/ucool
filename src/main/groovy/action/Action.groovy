package action

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by IntelliJ IDEA.
 * User: czy-thinkpad
 * Date: 11-12-18
 * Time: обнГ11:46
 * To change this template use File | Settings | File Templates.
 */
interface Action {
    def process(HttpServletRequest request, HttpServletResponse response)
}

abstract BaseAction implements Action {

}