package tools

import javax.servlet.http.HttpServletRequest

/**
 * Created by IntelliJ IDEA.
 * User: czy-thinkpad
 * Date: 11-12-5
 * Time: 上午12:24
 * To change this template use File | Settings | File Templates.
 */
class HttpTools {
    /**
     * 判断refer中是否带debug=true
     *
     * @param request of type HttpServletRequest
     * @return boolean
     */
    static boolean isReferDebug(HttpServletRequest request) {
        String refer = request.getHeader("Referer");
        /**
         * 目前支持2种方式：?debug和debug=true
         */
        return refer != null && (refer.contains("ucool=debug"));
    }

    static boolean isReferClean(HttpServletRequest request) {
        String refer = request.getHeader("Referer");
        /**
         * 目前支持2种方式：?debug和debug=true
         */
        return refer != null && (refer.contains("op=clean"));
    }

    /**
     * 过滤特殊字符
     * @param input
     * @return
     */
    static String filterSpecialChar (String input) {
        StringBuilder filtered = new StringBuilder(input.length());
        char c;
        for (int i = 0; i < input.length(); i++) {
            c = input.charAt(i);
            switch (c) {
                case '"':filtered.append("\\\"");break;
                case '\'':filtered.append("\\'");break;
                case '\\':filtered.append("\\\\");break;
                case '?':filtered.append("\\?");break;
                case '.':filtered.append("\\.");break;
                default:filtered.append(c);
            }
        }
        return (filtered.toString());
    }
}
