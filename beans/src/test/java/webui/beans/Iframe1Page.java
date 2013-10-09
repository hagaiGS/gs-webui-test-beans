package webui.beans;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webui.tests.annotations.SwitchTo;
import webui.tests.components.abstracts.AbstractComponent;

/**
 * Created with IntelliJ IDEA.
 * User: guym
 * Date: 9/18/13
 * Time: 1:44 PM
 */
public class Iframe1Page extends AbstractComponent<Iframe1Page> {

    private static Logger logger = LoggerFactory.getLogger(Iframe1Page.class);


    @FindBy( css = "body" )
    public WebElement body;

    @FindBy( css = "iframe" )
    @SwitchTo
    public Iframe2Page iframe2Page;

    public String getText(){
        return body.getText();
    }

    @Override
    public Iframe1Page load() {
        logger.info("loading iframe");
        return super.load();
    }

    public Iframe2Page getIframe2Page() {
        return iframe2Page;
    }
}
