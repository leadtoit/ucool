package dao.entity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/**
 * Created by IntelliJ IDEA.
 * User: czy-thinkpad
 * Date: 11-8-27
 * Time: ÏÂÎç5:07
 * To change this template use File | Settings | File Templates.
 */
public class UrlBase extends URLStreamHandler {

    private URL url;

    public UrlBase(String url) throws MalformedURLException {
        this.url = new URL(url);
    }

    public String getHost() {
        return this.url.getHost();
    }

    public String getProtocol() {
        return this.url.getProtocol();
    }

    public int getPort() {
        if (this.url.getPort() == -1) {
            return this.url.getDefaultPort();
        }
        return this.url.getPort();
    }

    public String getPath() {
        return this.url.getPath();
    }

    public String getQuery() {
        return this.url.getQuery();
    }

    @Override
    protected URLConnection openConnection(URL u) throws IOException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setHost(String newHost) {
        this.setURL(url, getProtocol(), newHost, getPort(), this.url.getAuthority(),
                this.url.getUserInfo(), getPath(), getQuery(), this.url.getRef());
    }

    public void setPort(int newPort) {
        this.setURL(url, getProtocol(), getHost(), newPort, this.url.getAuthority(),
                this.url.getUserInfo(), getPath(), getQuery(), this.url.getRef());
    }

    public void setPath(String newPath) {
        this.setURL(url, getProtocol(), getHost(), getPort(), this.url.getAuthority(),
                this.url.getUserInfo(), newPath, getQuery(), this.url.getRef());
    }

    public void setQuery(String newQuery) {
        this.setURL(url, getProtocol(), getHost(), getPort(), this.url.getAuthority(),
                this.url.getUserInfo(), getPath(), newQuery, this.url.getRef());
    }
}
