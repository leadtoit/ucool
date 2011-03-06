package connection;

import biz.file.FileEditor;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 10-12-5 ÏÂÎç8:10
 */
public class GetFileTest {
    public static void main(String[] args) {
        FileEditor fileEditor = new FileEditor();
        try {
            InputStreamReader in = fileEditor
                    .loadFileStream(
                            "E:\\JavaProjects\\ucool\\target\\ucool-2.4.1b\\cache\\online\\p\\global\\global.js", "gbk");
            BufferedReader br = new BufferedReader(in);
            String line = null;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }

            br.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();  //FIXME Òì³£×°»»
        }

    }
}
