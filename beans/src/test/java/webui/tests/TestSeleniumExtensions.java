package webui.tests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testng.Assert;
import webui.beans.ExtensionsTestPage;
import webui.tests.components.html.Select;
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

    @Autowired
    ExtensionsTestPage testPage;

    @Test
    public void testSelectElement(){
        logger.info("test start");
        testPage.goTo();
        String firstSelected = testPage.select.getSelected().getText();
        WebElement notSelected = testPage.select.getOption(Select.By.selected(false));
        testPage.select.selectOption(Select.By.textContains( notSelected.getText() ));
        String secondSelected = testPage.select.getSelected().getText();
        Assert.assertNotEquals( firstSelected, secondSelected , "Selection should have changed, but both selections show the same text.");
        logger.info("test end");
    }
}
