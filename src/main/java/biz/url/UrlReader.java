package biz.url;

import common.ConfigCenter;
import common.tools.UrlTools;
import dao.entity.RequestInfo;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;

/**
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 11-4-5 下午10:31
 */
public class UrlReader {

    private UrlTools urlTools;

    private ConfigCenter configCenter;

    public void setUrlTools(UrlTools urlTools) {
        this.urlTools = urlTools;
    }

    public void setConfigCenter(ConfigCenter configCenter) {
        this.configCenter = configCenter;
    }

    /**
     * 字节读取方法，无视编码，让浏览器自己判断
     * 
     * @param outputStream
     * @param stream
     * @param fileUrl
     * @param skipCommet
     * @return
     * @throws IOException
     */
    public boolean pushStream(ServletOutputStream outputStream, InputStream stream, String fileUrl, boolean skipCommet) throws IOException {
        int n;
        byte[] firstline = new byte[50];
        BufferedInputStream bufferedInputStream = new BufferedInputStream(stream);
        if((n = bufferedInputStream.read(firstline)) >= 0) {
            String first = new String(firstline, 0, n);
            if("/*not found*/".equals(first)) {
                bufferedInputStream.close();
                outputStream.flush();
                return false;
            } else {
                if (!skipCommet) {
                    outputStream.println("/*ucool filePath=" + fileUrl + "*/");
                }
                outputStream.write(firstline, 0, n);
            }
        }

        byte[] buf = new byte[1024 * 64];
        while ((n = bufferedInputStream.read(buf)) >= 0) {
            outputStream.write(buf, 0, n);
        }
        bufferedInputStream.close();
        outputStream.flush();
        return true;
    }

    /**
     * 字符流读取
     *
     * @param requestInfo
     * @param stream
     * @return
     * @throws IOException
     */
    public boolean pushStream(RequestInfo requestInfo, HttpServletResponse response, InputStream stream) throws IOException {
        BufferedInputStream buff = new BufferedInputStream(stream);
        String charset = null;
        boolean findCharset = false;
        for (String filterEncoding : configCenter.getUcoolAssetsEncodingCorrectStrings()) {
            if(requestInfo.getRealUrl().indexOf(filterEncoding) != -1) {
                charset = "utf-8";
                findCharset = true;
                break;
            }
        }
        if(!findCharset) {
            charset = urlTools.getCharset(buff);
        }
        if(requestInfo.getRealUrl().indexOf("kissy.js") != -1) {
            response.setCharacterEncoding("gbk");
        } else if(requestInfo.getRealUrl().indexOf("/s/kissy/") != -1) {
//            response.setCharacterEncoding("utf-8");
        } else {
            response.setCharacterEncoding("gbk");
        }

        PrintWriter writer = response.getWriter();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(buff, charset));
        String firstLine = bufferedReader.readLine();
        if(firstLine == null || "/*not found*/".equals(firstLine)) {
            writer.flush();
            bufferedReader.close();
            buff.close();
            return false;
        } else {
            if (requestInfo.getType().equals("assets")) {
                if(requestInfo.isLocalCombo()) {
                    writer.println("/*ucool local combo matched:" + requestInfo.getFilePath() + "*/");
                }
                writer.println("/*ucool filePath=" + requestInfo.getRealUrl() + "*/");
            }
            writer.println(firstLine);
            String line;
            while((line = bufferedReader.readLine()) != null) {
                writer.println(line);
            }
            writer.flush();
            bufferedReader.close();
            buff.close();
        }
        return true;
    }

}
