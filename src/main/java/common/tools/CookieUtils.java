package common.tools;

import javax.servlet.http.Cookie;

/**
 * Created by IntelliJ IDEA.
 * User: czy-dell
 * Date: 11-6-14
 * Time: ÏÂÎç3:17
 * To change this template use File | Settings | File Templates.
 */
public class CookieUtils {

    public static String DEFAULT_KEY = "";
    
    public Cookie addCookie(String key, String value, String domain) {
        Cookie cookie = new Cookie(key, value);
        cookie.setDomain(domain);
        return cookie;
    }

    public Cookie getCookie(Cookie[] cookies, String key) {
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals(key)) {
                return cookie;
            }
        }
        return null;
    }

    public boolean hasCookie(Cookie[] cookies, String key) {
        return getCookie(cookies, key) != null;
    }
}
