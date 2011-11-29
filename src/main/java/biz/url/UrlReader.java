package biz.url;

import common.ConfigCenter;
import common.tools.UrlTools;
import dao.entity.RequestInfo;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.util.Map;

/**
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 11-4-5 ����10:31
 */
public class UrlReader {

    private ConfigCenter configCenter;

    public void setConfigCenter(ConfigCenter configCenter) {
        this.configCenter = configCenter;
    }

    /**
     * �ֽڶ�ȡ���������ӱ��룬��������Լ��ж�
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
     * �ַ�����ȡ
     *
     * @param requestInfo
     * @param stream
     * @return
     * @throws IOException
     */
    public boolean pushStream(RequestInfo requestInfo, InputStream stream) throws IOException {
        BufferedInputStream buff = new BufferedInputStream(stream);
        String charset = null;
        boolean findCharset = false;
        for (String filterEncoding : configCenter.getUcoolAssetsEncodingCorrectStrings()) {
            if(requestInfo.getRealUrl().contains(filterEncoding)) {
                charset = "UTF-8";
                findCharset = true;
                break;
            }
        }
        if(!findCharset) {
            charset = UrlTools.getCharset(buff);
        }

        InputStream filterBuff = buff;
        //ȥ��utf-8��bomͷ
        if(charset.equals("UTF-8")) {
            filterBuff = UrlTools.removeBom(filterBuff);
        }

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(filterBuff, charset));
        String firstLine = bufferedReader.readLine();
        if(firstLine == null || "/*not found*/".equals(firstLine)) {
            bufferedReader.close();
            filterBuff.close();
            return false;
        } else {
            if(requestInfo.getRealUrl().contains("kissy.js") || requestInfo.getRealUrl().contains("seed.js") || requestInfo.getRealUrl().contains("/s/kissy/")) {
                if(requestInfo.isUrlCombo()) {
                    requestInfo.getResponse().setCharacterEncoding("GBK");
                } else {
                    requestInfo.getResponse().setCharacterEncoding("UTF-8");
                }
            } else {
                requestInfo.getResponse().setCharacterEncoding(charset);
            }

            PrintWriter writer = requestInfo.getResponse().getWriter();

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
            filterBuff.close();
        }
        return true;
    }

        /**
     * Method readUrlFile ...
     *
     * @param requestInfo
     * @return
     */
    public boolean readUrlFile(RequestInfo requestInfo) {
        try {
            URL url = new URL(requestInfo.getRealUrl());
            return this.pushStream(requestInfo, url.openStream());
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * רΪͼƬ����ķ���
     * @param requestInfo
     * @param fullUrl
     * @param out
     * @return
     */
    public boolean readUrlFileForPng(RequestInfo requestInfo, String fullUrl, ServletOutputStream out) {
        try {
            URL url = new URL(fullUrl);
            return this.pushStream(out, url.openStream(), fullUrl, !requestInfo.getType().equals("assets"));
        } catch (Exception e) {
        }
        return false;
    }

}
