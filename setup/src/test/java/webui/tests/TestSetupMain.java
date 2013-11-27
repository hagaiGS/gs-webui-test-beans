package webui.tests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import webui.tests.setup.Main;

/**
 * Created with IntelliJ IDEA.
 * User: guym
 * Date: 8/11/13
 * Time: 10:02 AM
 */
public class TestSetupMain {

    private static Logger logger = LoggerFactory.getLogger(TestSetupMain.class);

    @BeforeSuite
    public void beforeSuite(){
        logger.info("before suite!");
    }

    @Test
    public void testSetupMain(){
        System.setProperty(Main.SETUP_CONTEXT, "classpath:mainSetup-context.xml");
        Main.main(new String[]{});
    }
}
