package common.tools;

import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: czy-thinkpad
 * Date: 11-7-13
 * Time: 上午12:01
 * To change this template use File | Settings | File Templates.
 */
public class RandomString {
    /**
     * 产生一个随机的字符串
     *
     * @param length 字符串长度
     * @return
     */
    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
}
