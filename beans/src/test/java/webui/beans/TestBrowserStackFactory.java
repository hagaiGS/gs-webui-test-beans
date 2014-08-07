package webui.beans;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import webui.tests.SeleniumDriverFactory;
import webui.tests.remoteclient.RemoteClientFactory;

/**
 * Created with IntelliJ IDEA.
 * User: guym
 * Date: 7/31/14
 * Time: 6:31 PM
 */

@ContextConfiguration(value="classpath:dev/browserstack_poc-context.xml")
public class TestBrowserStackFactory extends AbstractTestNGSpringContextTests {
    @Autowired
    SeleniumDriverFactory factory;



    @Test

    public void testRemoteDriver(){
        WebDriver driver = factory.getDriver();
        driver.get("http://www.google.com");
        String text = driver.findElement(By.cssSelector("#hplogo")).getText();
        System.out.println("text = " + text);
    }

    @Test
    public void testBrowserstack(){
        WebDriver driver = factory.getDriver();
        driver.get("http://www.google.com/ncr");
        WebElement element = driver.findElement(By.name("q"));

        element.sendKeys("BrowserStack");
        element.submit();

        System.out.println(driver.getTitle());
    }

    public SeleniumDriverFactory getFactory() {
        return factory;
    }

    public void setFactory(SeleniumDriverFactory factory) {
        this.factory = factory;
    }
}
