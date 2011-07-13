package common;

/**
 * Created by IntelliJ IDEA.
 * User: czy-dell
 * Date: 11-7-13
 * Time: ионГ10:03
 * To change this template use File | Settings | File Templates.
 */
public class MatchTest {
    public static void main(String[] args) {
        String url = "http://a.tbcdn.cn/p??/mods.js?pcname=213";
        System.out.println(url.indexOf("?", url.indexOf("??") + 2));
    }
}
