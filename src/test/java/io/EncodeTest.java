package io;

import info.monitorenter.cpdetector.io.*;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: czy-thinkpad
 * Date: 11-5-17
 * Time: 下午2:00
 * To change this template use File | Settings | File Templates.
 */
public class EncodeTest {
    public static void main(String[] args) throws MalformedURLException {
//        java.io.File f = new java.io.File("C:\\Users\\czy-thinkpad\\Downloads\\core.js");
        URL url = new URL("http://assets.daily.taobao.net/s/kissy/1.1.6/kissy.js?t=20101224.js");
        URL url1 = new URL("http://assets.daily.taobao.net/apps/buy/1.0/buynow_v1.2.source.js");
        try {
//            System.out.println(get_charset(new FileInputStream(f)));
            System.out.println(get_charset(url.openStream()));
            System.out.println(get_charset(url1.openStream()));

//            CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
//            detector.add(new ParsingDetector(false));
//            detector.add(JChardetFacade.getInstance());
//            // ASCIIDetector用于ASCII编码测定
//            detector.add(ASCIIDetector.getInstance());
//            // UnicodeDetector用于Unicode家族编码的测定
//            detector.add(UnicodeDetector.getInstance());
//            java.nio.charset.Charset charset = null;
//
//            charset = detector.detectCodepage(f.toURI().toURL());
//            System.out.println(charset);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String get_charset(InputStream inputStream) {
        String charset = "GBK";
        byte[] first3Bytes = new byte[3];
        try {
            boolean checked = false;
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            bis.mark( 0 );
            int read = bis.read( first3Bytes, 0, 3 );
            if ( read == -1 ) return charset;
            if ( first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE ) {
                charset = "UTF-16LE";
                checked = true;
            }
            else if ( first3Bytes[0] == (byte) 0xFE && first3Bytes[1] == (byte) 0xFF ) {
                charset = "UTF-16BE";
                checked = true;
            }
            else if ( first3Bytes[0] == (byte) 0xEF && first3Bytes[1] == (byte) 0xBB && first3Bytes[2] == (byte) 0xBF ) {
                charset = "UTF-8";
                checked = true;
            }
            bis.reset();
            if ( !checked ) {
            //    int len = 0;
                int loc = 0;

                while ( (read = bis.read()) != -1 ) {
                    loc++;
                    if ( read >= 0xF0 ) break;
                    if ( 0x80 <= read && read <= 0xBF ) // 单独出现BF以下的，也算是GBK
                    break;
                    if ( 0xC0 <= read && read <= 0xDF ) {
                        read = bis.read();
                        if ( 0x80 <= read && read <= 0xBF ) // 双字节 (0xC0 - 0xDF) (0x80
                                                                        // - 0xBF),也可能在GB编码内
                        continue;
                        else break;
                    }
                    else if ( 0xE0 <= read && read <= 0xEF ) {// 也有可能出错，但是几率较小
                        read = bis.read();
                        if ( 0x80 <= read && read <= 0xBF ) {
                            read = bis.read();
                            if ( 0x80 <= read && read <= 0xBF ) {
                                charset = "UTF-8";
                                break;
                            }
                            else break;
                        }
                        else break;
                    }
                }
                //System.out.println( loc + " " + Integer.toHexString( read ) );
            }

            bis.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        return charset;
    } 
}
