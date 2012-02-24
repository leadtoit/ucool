package tools

import javax.servlet.http.HttpServletRequest

/**
 * Created by IntelliJ IDEA.
 * User: czy-thinkpad
 * Date: 11-12-5
 * Time: ����12:24
 * To change this template use File | Settings | File Templates.
 */
class HttpTools {
    /**
     * �ж�refer���Ƿ��debug=true
     *
     * @param request of type HttpServletRequest
     * @return boolean
     */
    static boolean isReferDebug(HttpServletRequest request) {
        String refer = request.getHeader("Referer");
        /**
         * Ŀǰ֧��2�ַ�ʽ��?debug��debug=true
         */
        return refer != null && (refer.contains("ucool=debug"));
    }

    static boolean isReferClean(HttpServletRequest request) {
        String refer = request.getHeader("Referer");
        /**
         * Ŀǰ֧��2�ַ�ʽ��?debug��debug=true
         */
        return refer != null && (refer.contains("op=clean"));
    }

    /**
     * ���������ַ�
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
