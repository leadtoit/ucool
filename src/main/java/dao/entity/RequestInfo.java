package dao.entity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 11-3-12 下午1:19
 */
public class RequestInfo {

    private HttpServletRequest request;
    private HttpServletResponse response;

    private String filePath;
    private String realUrl;
    private String fullUrl;

    private String requestUrl;
    private String serverName;
    private String queryString;
    private String clientAddr;

    private String type = "assets";

    private boolean isUrlCombo = false;

    private boolean isLocalCombo = false;

    private String curMappingPath = "";

    /**
     * 是否是本地combo之后的combo链接
     */
    private boolean isAfterLocalCombo = false;

    public RequestInfo(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        this.requestUrl = request.getRequestURI();
        this.serverName = request.getServerName();
        this.queryString = request.getQueryString();
        this.clientAddr = request.getRemoteAddr();
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isLocalCombo() {
        return isLocalCombo;
    }

    public void setLocalCombo(boolean localCombo) {
        isLocalCombo = localCombo;
    }

    public boolean isUrlCombo() {
        return isUrlCombo;
    }

    public void setUrlCombo(boolean urlCombo) {
        isUrlCombo = urlCombo;
    }

    public String getClientAddr() {
        return clientAddr;
    }

    public void setClientAddr(String clientAddr) {
        this.clientAddr = clientAddr;
    }

    public String getCurMappingPath() {
        return curMappingPath;
    }

    public void setCurMappingPath(String curMappingPath) {
        this.curMappingPath = curMappingPath;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public boolean isAfterLocalCombo() {
        return isAfterLocalCombo;
    }

    public void setAfterLocalCombo(boolean afterLocalCombo) {
        isAfterLocalCombo = afterLocalCombo;
    }
}
