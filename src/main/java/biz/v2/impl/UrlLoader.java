package biz.v2.impl;


import biz.v2.ResourceLoader;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: czy-thinkpad
 * Date: 11-5-17
 * Time: обнГ4:59
 * To change this template use File | Settings | File Templates.
 */
public class UrlLoader implements ResourceLoader {

    private String fullUrl;

    public UrlLoader(String url) {
        this.fullUrl = url;
    }

    @Override
    public InputStream getResourceStream() {
//        URL url = new URL(this.fullUrl);
//        return url.openStream();
        return null;
    }
}
