package webui.tests.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import webui.tests.SeleniumSwitchManager;
import webui.tests.components.conditions.WaitMethods;

/**
 * User: guym
 * Date: 4/8/13
 * Time: 5:21 PM
 *
 * An interface to represent a Selenium component
 *
 */
public interface GsSeleniumComponent<T extends GsSeleniumComponent> {

    public T setWebElement(WebElement webElement);

    public T setWebDriver( WebDriver webDriver );

    public T setSwitchManager( SeleniumSwitchManager switchManager );

    public WebElement getRootElement();

    public T setWaitFor( WaitMethods waitFor );

}
