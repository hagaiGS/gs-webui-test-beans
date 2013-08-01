package webui.tests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.testng.annotations.Test;

/**
 * Created with IntelliJ IDEA.
 * User: guym
 * Date: 7/31/13
 * Time: 5:51 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"classpath:example-context.xml"})
@ContextConfiguration
public class TestSeleniumExtensions {

    private static Logger logger = LoggerFactory.getLogger(TestSeleniumExtensions.class);
    @Test
    public void testSelectElement(){
        logger.info("test start");

        logger.info("test end");
    }
}
