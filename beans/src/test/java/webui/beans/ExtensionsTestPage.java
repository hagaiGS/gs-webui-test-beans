package webui.beans;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import webui.tests.annotations.SwitchTo;
import webui.tests.components.abstracts.AbstractComponent;

import java.util.List;

@Component
public class ExtensionsTestPage extends AbstractComponent<ExtensionsTestPage> {

    public String location="http://beans.gsdev.info";

    private static Logger logger = LoggerFactory.getLogger(ExtensionsTestPage.class);

    @FindBy( css = "select")
    public webui.tests.components.html.Select select;

    @FindBy( css = "iframe.first")
    @SwitchTo
    public Iframe1Page iframe1Page;

    public void goTo(){
        logger.info("loading [{}]", location);
        webDriver.get(location);
        load();
    }

    public Iframe1Page getIframe1Page(){
        return iframe1Page;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<WebElement> getOptions(){
        return select.getOptionElements();
    }
}
