package web.handler.impl;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 2010-10-1 20:43:02
 */
public class ComboHandlerTest {
    @BeforeMethod
    public void setUp() throws Exception {

    }

    @AfterMethod
    public void tearDown() throws Exception {

    }

    @Test
    public void testDoHandler() throws Exception {
        String allUrl = "http://a.tbcdn.cn/p/??header/header-min.css, fp/2010c/fp-base-min.css,fp/2010c/fp-channel-min.css, fp/2010c/fp-product-min.css,fp/2010c/fp-mall-min.css, fp/2010c/fp-category-min.css,fp/2010c/fp-sub-min.css, fp/2010c/fp-gdp4p-min.css,fp/2010c/fp-css3-min.css, fp/2010c/fp-misc-min.css?t=20100902.css";
        String[] firstCut = allUrl.split("\\?\\?");
        String pathPrefix = firstCut[0];
        String[] allFiles = firstCut[1].split(",");
        System.out.println(allFiles.length);
    }
    
    @Test
    public void testFiter() throws Exception {
        String allUrl = "http://a.tbcdn.cn/p/fp/2010c/fp-misc-min.css?t=20100902.css";
        System.out.println(allUrl.split("\\?")[0]);
        allUrl = "http://a.tbcdn.cn/p/header/header-min.css";
        System.out.println(allUrl.split("\\?")[0]);
        allUrl = allUrl.replace("http://a.tbcdn.cn", "");
        System.out.println(allUrl);
    }
}
