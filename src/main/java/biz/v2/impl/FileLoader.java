package biz.v2.impl;

import biz.v2.ResourceLoader;
import common.PersonConfig;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 * User: czy-thinkpad
 * Date: 11-5-17
 * Time: ÏÂÎç5:03
 * To change this template use File | Settings | File Templates.
 */
public class FileLoader implements ResourceLoader {

    private String filePath;

    public FileLoader(String webroot, String filePath, PersonConfig personConfig) {
        String root = personConfig.getUcoolAssetsRoot();
        StringBuilder sb = new StringBuilder();
        this.filePath = sb.append(webroot).append(root).append(filePath).toString();
    }

    @Override
    public InputStream getResourceStream() {
        try {
            return new FileInputStream(filePath);
        } catch (FileNotFoundException e) {

        }
        return null;
    }
}
