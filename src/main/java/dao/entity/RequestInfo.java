package dao.entity;

import javax.servlet.http.HttpServletRequest;

/**
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 11-3-12 ����1:19
 */
public class RequestInfo {

    private String filePath;
    private String realUrl;
    private String fullUrl;

    private String requestUrl;
    private String serverName;
    private String queryString;

    public RequestInfo(HttpServletRequest request) {
        this.requestUrl = request.getRequestURI();
        this.serverName = request.getServerName();
        this.queryString = request.getQueryString();
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getRealUrl() {
        return realUrl;
    }

    public void setRealUrl(String realUrl) {
        this.realUrl = realUrl;
    }

    public String getFullUrl() {
        return fullUrl;
    }

    public void setFullUrl(String fullUrl) {
        this.fullUrl = fullUrl;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }
}
