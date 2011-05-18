package biz.v2;

import common.tools.UrlTools;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class HttpClientReader {

    private UrlTools urlTools;

    public void setUrlTools(UrlTools urlTools) {
        this.urlTools = urlTools;
    }

    public boolean loadUrl(String url, PrintWriter writer, boolean skipCommet) {
        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod(url);
        String a = null;

        try {
            client.executeMethod(method);
            if(method.getStatusCode() ==  200) {
                String charset = urlTools.getCharset(new BufferedInputStream(method.getResponseBodyAsStream()));
                String content = new String(method.getResponseBody(), charset);
                if("/*not found*/".equals(content)) {
                    method.releaseConnection();
                    return false;
                } else {
                    if (!skipCommet) {
                        writer.println("/*ucool filePath=" + url + "*/");
                    }
                    writer.print(content);

                }
//                System.out.println(new String(method.getResponseBody(), "UTF-8"));
            }
        } catch (IOException e) {
            
        }
        writer.flush();
        method.releaseConnection();
        return true;
    }

    /**
     * 加载文件，保证文件路径正确
     * @param filePath
     * @param writer
     * @param skipCommet
     * @return
     */
//    public boolean loadFile(String filePath, PrintWriter writer, boolean skipCommet) {
//        int n;
//        byte[] firstline = new byte[50];
//        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
//        if((n = bufferedInputStream.read(firstline)) >= 0) {
//            String first = new String(firstline, 0, n);
//            if(first.equals("/*not found*/")) {
//                bufferedInputStream.close();
//                outputStream.flush();
//                return false;
//            } else {
//                if (!skipCommet) {
//                    outputStream.println("/*ucool filePath=" + fileUrl + "*/");
//                }
//                outputStream.write(firstline, 0, n);
//            }
//        }
//
//        byte[] buf = new byte[1024 * 64];
//        while ((n = bufferedInputStream.read(buf)) >= 0) {
//            outputStream.write(buf, 0, n);
//        }
//        bufferedInputStream.close();
//        outputStream.flush();
//        return true;
//    }
//
//    public static void main(String[] args) {
//        HttpClientReader reader = new HttpClientReader();
//        reader.loadUrl("http://assets.daily.taobao.net/s/kissy/1.1.6/kissy.js?t=20101224.js", null);
//    }
}
