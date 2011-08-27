package connection;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: czy-thinkpad
 * Date: 11-8-27
 * Time: 下午4:50
 * To change this template use File | Settings | File Templates.
 */
public class UrlTest {
    public static void main(String[] args) throws IOException, URISyntaxException {
        URL url = new URL("http://a.tbcdn.cn:8080/s/kissy/kissy.js?t=20112020&a=b");
        System.out.println(url.getHost()); //a.tbcdn.cn
        System.out.println(url.getQuery()); //t=20112020&a=b
        System.out.println(url.getAuthority()); //a.tbcdn.cn:8080
        System.out.println(url.getDefaultPort()); //一直都是80
        System.out.println(url.getPort()); //8080 没填会返回-1
        System.out.println(url.getFile()); // /s/kissy/kissy.js?t=20112020&a=b
        System.out.println(url.getPath()); // /s/kissy/kissy.js
        System.out.println(url.getProtocol()); //http
        System.out.println(url.getRef());// null
        System.out.println(url.getUserInfo()); //null
        System.out.println(url.toURI().toString()); // http://a.tbcdn.cn:8080/s/kissy/kissy.js?t=20112020&a=b

        System.out.println("--------------------");

        URL url1 = new URL("http://127.0.0.1:8080/??s/kissy/kissy.js?t=20112020&a=b");
        System.out.println(url1.getHost()); //127.0.0.1
        System.out.println(url1.getQuery()); //?s/kissy/kissy.js?t=20112020&a=b
        System.out.println(url1.getAuthority()); //a.tbcdn.cn:8080
        System.out.println(url1.getDefaultPort()); //一直都是80
        System.out.println(url1.getPort()); //8080 没填会返回-1
        System.out.println(url1.getFile()); // /??s/kissy/kissy.js?t=20112020&a=b
        System.out.println(url1.getPath()); // /
        System.out.println(url1.getProtocol()); //http
        System.out.println(url1.getRef());// null
        System.out.println(url1.getUserInfo()); //null
        System.out.println(url1.toURI().toString()); // http://a.tbcdn.cn:8080/s/kissy/kissy.js?t=20112020&a=b


        System.out.println("--------------------");
        //replace and modify
        URL url2 = new URL("http://127.0.0.1:8080/??s/kissy/kissy.js?t=20112020&a=b");
    }
}
