package webui.tests;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: guym
 * Date: 8/7/14
 * Time: 5:10 PM
 */
public class SeleniumScreenshotTaker {

    private static Logger logger = LoggerFactory.getLogger(SeleniumScreenshotTaker.class);



    public void takeScreenshot( String filename ){
        // guy - unfortunately, spring and testng are not good friends..
        // we have to hack the system in order to get a bean driver..
        // this means this screenshot implementation is assuming driver was created by
        // selenium driver factory.

        WebDriver driver = SeleniumDriverFactory.staticWebDriver;
        driver = new Augmenter().augment(driver);
        File screenshotAs = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(screenshotAs, new File(filename));
        } catch (IOException e) {
            logger.error("unable to take screenshot",e);
        }


    }

}
