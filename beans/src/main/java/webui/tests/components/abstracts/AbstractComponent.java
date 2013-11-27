package webui.tests.components.abstracts;

import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import webui.tests.SeleniumSwitchManager;
import webui.tests.annotations.NoEnhancement;
import webui.tests.components.conditions.WaitMethods;
import webui.tests.selenium.GsFieldDecorator;
import webui.tests.selenium.GsSeleniumComponent;

/**
 * User: guym
 * Date: 4/9/13
 * Time: 3:24 PM
 */
@Component
public abstract class  AbstractComponent<T extends AbstractComponent> implements GsSeleniumComponent {

    @Autowired(required = true)
    protected WebDriver webDriver;

    @Autowired( required = true )
    protected SeleniumSwitchManager switchManager;

    protected WebElement webElement;

    @Autowired
    protected WaitMethods waitFor;

    private static Logger logger = LoggerFactory.getLogger( AbstractComponent.class );

    @Override
    public WebElement getRootElement() {
        return webElement;
    }

    @NoEnhancement
    @Override
    public T setWebElement( WebElement webElement ) {
        this.webElement = webElement;
        return (T) this;
    }

    @NoEnhancement
    @Override
    public T setSwitchManager(SeleniumSwitchManager switchManager) {
        this.switchManager = switchManager;
        return (T) this;
    }



    @NoEnhancement
    @Override
    public T setWebDriver( WebDriver webDriver ) {
        this.webDriver = webDriver;
        return (T) this;
    }

    @NoEnhancement
    public T load ( SearchContext searchContext ){
        PageFactory.initElements( new GsFieldDecorator( searchContext, webDriver ).setSwitchManager( switchManager  ).setWaitFor( waitFor) , this );
        return (T) this;
    }


    @NoEnhancement
    public T load(){
        return load( webElement == null ? webDriver : webElement );
    }


    @NoEnhancement
    public T setWaitFor( WaitMethods waitFor) {
        this.waitFor = waitFor;
        return (T) this;
    }

    @NoEnhancement
    @Override
    public String toString() {
        return super.toString();
    }

    @NoEnhancement
    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
