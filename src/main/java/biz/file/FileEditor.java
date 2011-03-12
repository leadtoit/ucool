package biz.file;

import common.ConfigCenter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 2010-9-23 14:17:50
 */
public class FileEditor {

    private ConfigCenter configCenter;

    public void setConfigCenter(ConfigCenter configCenter) {
        this.configCenter = configCenter;
    }

    /**
     * 查询文件是否存在
     *
     * @param filePath of type String
     *
     * @return boolean
     *
     * @author zhangting
     * @since 2010-8-16 13:51:20
     */
    public boolean findFile(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    /**
     * 使用fileInputStream加载文件，可以设置编码
     *
     * @param filePath of type String
     * @return FileReader
     * @throws FileNotFoundException when
     */
    public InputStreamReader loadFileStream(String filePath, String encoding)
            throws FileNotFoundException, UnsupportedEncodingException {
        return new InputStreamReader(new FileInputStream(filePath), encoding);
    }

    /**
     * 支持创建带多级目录的文件
     *
     * @param filePath of type String
     *
     * @throws java.io.IOException when
     * @author zhangting
     * @since 2010-8-19 16:16:53
     */
    public void createDirectory(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        file.createNewFile();
    }

    /**
     * Method pushStream ...
     *
     * @param out of type PrintWriter
     * @param in  of type BufferedReader
     *
     * @param fileUrl 用于combo的时候输出是哪个文件
     * @throws IOException when
     */
    public boolean pushStream(PrintWriter out, BufferedReader in, String fileUrl, boolean skipCommet) throws IOException {
        String line;
        if ((line = in.readLine()) != null && line.equals("/*not found*/")) {
            in.close();
            return false;
        } else {
            if(!skipCommet) {
                out.println("/*ucool filePath=" + fileUrl + "*/");
            }
            out.println(line);
            out.flush();
        }
        while ((line = in.readLine()) != null) {
            out.println(line);
        }
        in.close();
        out.flush();
        return true;
    }

    public void pushFileOutputStream(PrintWriter out, InputStreamReader reader, String filePath) {
        try {
            BufferedReader in = new BufferedReader(reader);
            pushStream(out, in, filePath, false);
        } catch(Exception e) {
            //捕获所有异常，这里有可能缓存失败，所以取不到文件
        }
    }

    /**
     * Method saveFile ...
     *
     * @param filePath of type String
     * @param in       of type BufferedReader
     *
     * @return boolean
     *
     * @throws IOException when
     */
    public boolean saveFile(String filePath, BufferedReader in) throws IOException {
        String line;
        File file = new File(filePath);
        if (file.canWrite()) {
            FileOutputStream writerStream = new FileOutputStream(file);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(writerStream, "gbk"));
            if ((line = in.readLine()) != null && line.equals("/*not found*/")) {
                bw.close();
                writerStream.close();
                in.close();
                file.delete();
                return false;
            } else {
                if(line != null) {
                    bw.write(line);
                    bw.newLine();
                    bw.flush();
                } else {
                    bw.close();
                    writerStream.close();
                    in.close();
                    file.delete();
                    return false;
                }
            }
            while ((line = in.readLine()) != null) {
                bw.write(line);
                bw.newLine();
                bw.flush();
            }
            bw.close();
            writerStream.close();
            in.close();
            return true;
        }
        return false;
    }

    public boolean removeDirectory(String filePath) {
        return delAllFile(filePath);
    }

    public boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);//再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }

    private void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); //删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            myFilePath.delete(); //删除空文件夹
        } catch (Exception e) {

        }
    }

    /**
     * 用于获取assets的子目录，让用户选择
     *
     * @return the assetsSubDirs (type ArrayList<String>) of this FileEditor object.
     */
    public List<String> getAssetsSubDirs() {
        return getSubDirs(this.configCenter.getWebRoot() + this.configCenter.getUcoolAssetsRoot(), null);
    }

    public List<String> getAssetsSubDirs(String filePath) {
        return getSubDirs(this.configCenter.getWebRoot() + this.configCenter.getUcoolAssetsRoot() + "/" + filePath, configCenter.getUcoolAssetsDirectoryPrefix());
    }

    /**
     * 获取目录的子目录列表
     *
     * @param directoryPath of type String
     * @return List<String>
     */
    public List<String> getSubDirs(String directoryPath, String filter) {
        List<String> dirList = new ArrayList<String>();
        File assetsDir = new File(directoryPath);
        if (!assetsDir.exists()) {
            return dirList;
        }
        File[] files = assetsDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                if(filter != null && !file.getName().startsWith(filter)) {
                    continue;
                }
                dirList.add(file.getName());
            }
        }
        return dirList;
    }

    public static void main(String[] args) {
        FileEditor editor = new FileEditor();
        editor.removeDirectory("D:\\dev\\ucool\\target\\ucool\\cache\\daily");
    }

}
