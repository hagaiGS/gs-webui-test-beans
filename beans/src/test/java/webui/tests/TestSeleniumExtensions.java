package webui.tests;


import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;
import webui.beans.ExtensionsTestPage;
import webui.beans.Iframe1Page;
import webui.beans.Iframe2Page;
import webui.tests.components.html.Select;

/**
 * User: guym
 * Date: 7/31/13
 * Time: 5:51 PM
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"classpath:example-context.xml"})
@ContextConfiguration
public class TestSeleniumExtensions extends AbstractTestNGSpringContextTests {

    private static Logger logger = LoggerFactory.getLogger(TestSeleniumExtensions.class);

    @Autowired
    ExtensionsTestPage testPage;


    @Test
    public void testSelectElement() {
        logger.info("test start");
        testPage.goTo();
        String firstSelected = testPage.select.getSelected().getText();
        WebElement notSelected = testPage.select.getOption(Select.By.selected(false));
        testPage.select.selectOption(Select.By.textContains(notSelected.getText()));
        String secondSelected = testPage.select.getSelected().getText();
        Assert.assertNotEquals(firstSelected, secondSelected, "Selection should have changed, but both selections show the same text.");
        logger.info("test end");
    }

    @Test
    public void testSwitchTo() {
        testPage.goTo();

        // we enter first iframe
        Iframe1Page iframe1Page = testPage.getIframe1Page();
        Assert.assertTrue( iframe1Page.getText().contains("This is iframe1"));

        // now we get content from second iframe
        Assert.assertTrue(iframe1Page.getIframe2Page().getText().contains("This is iframe2"));

        // but we are actually still in iframe1 :)
        Assert.assertTrue( iframe1Page.getText().contains("This is iframe1"));

        // now we enter iframe2
        Iframe2Page iframe2Page = iframe1Page.getIframe2Page();
        Assert.assertTrue(iframe2Page.getText().contains("This is iframe2"));

        // now we exit iframe2 and we are back in iframe1

        Assert.assertTrue( iframe1Page.getText().contains("This is iframe1"));

        // now we exit iframe1 and we are back on top.
        testPage.getOptions();

        Assert.assertTrue( testPage.getIframe1Page().getIframe2Page().getText().contains("This is iframe2") );
        testPage.getIframe1Page();

        testPage.getOptions();
    }
}
