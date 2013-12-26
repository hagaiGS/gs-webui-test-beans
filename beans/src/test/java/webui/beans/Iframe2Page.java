package webui.beans;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import webui.tests.components.abstracts.AbstractComponent;

/**
 * Created with IntelliJ IDEA.
 * User: guym
 * Date: 9/18/13
 * Time: 1:44 PM
 */
public class Iframe2Page extends AbstractComponent<Iframe2Page> {
    @FindBy( css = "body" )
    public WebElement body;

    public String getText(){
        return body.getText();
    }
}
