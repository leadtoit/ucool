package biz.url;

import biz.v2.ResourceLoader;
import common.tools.UrlTools;

import javax.servlet.ServletOutputStream;
import java.io.*;

/**
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 11-4-5 下午10:31
 */
public class UrlReader {

    private UrlTools urlTools;

    public void setUrlTools(UrlTools urlTools) {
        this.urlTools = urlTools;
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
     * @param writer
     * @param stream
     * @param fileUrl
     * @param skipCommet
     * @return
     * @throws IOException
     */
    public boolean pushStream(PrintWriter writer, InputStream stream, String fileUrl, boolean skipCommet) throws IOException {
        String charset = urlTools.getCharset(stream);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream, charset));
        String firstLine = bufferedReader.readLine();
        if(firstLine == null || "/*not found*/".equals(firstLine)) {
            writer.flush();
            bufferedReader.close();
            return false;
        } else {
            if (!skipCommet) {
                writer.println("/*ucool filePath=" + fileUrl + "*/");
            }
            writer.print(firstLine);
            String line;
            while((line = bufferedReader.readLine()) != null) {
                writer.print(line);
            }
            writer.flush();
            bufferedReader.close();
        }
        return true;
    }

}
