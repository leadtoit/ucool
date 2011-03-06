package common;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 10-12-19 обнГ4:38
 */
public class PersonConfigTest {
    @AfterMethod
    public void tearDown() throws Exception {

    }

    @Test
    public void testParseConfigString() throws Exception {
        PersonConfig personConfig = new PersonConfig();
        String configString = personConfig.getConfigString();
        for (String o : configString.split(":")) {
            System.out.println(o);
        }

    }
}
