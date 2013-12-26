package webui.tests.components.gwt;

import org.apache.commons.collections.Predicate;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webui.tests.annotations.LazyLoad;
import webui.tests.components.abstracts.AbstractComponent;
import webui.tests.components.conditions.SizeCondition;
import webui.tests.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * User: eliranm
 * Date: 7/14/13
 * Time: 5:49 PM
 */
public class ComboBox extends AbstractComponent<ComboBox> {

    @FindBy(tagName = "input")
    private WebElement active;

    @LazyLoad
    @FindBy( css = ".icon")
    private WebElement dropDownButton;


    @FindBy( css=".list-wrapper ul li")
    private List<WebElement> options;

    private static Logger logger = LoggerFactory.getLogger(ComboBox.class);

    public String getActiveValue() {
        return active.getAttribute("value");
    }

    public WebElement getActiveElement(){
        return active;
    }

    public String getActiveText(){
        return active.getText();
    }

    public void select(String name) {
        webElement.findElement(By.cssSelector(".icon")).click();
        List<WebElement> items = getItems();
        for (WebElement item : items) {
            if (item.getText().equals(name)) {
                item.click();
                break;
            }
        }
    }

    /**
     * Using a wait for until we have text.
     * @param name
     * @return
     */
    public boolean has(final String name) {

        Collection<WebElement> items = (Collection<WebElement>) waitFor.predicate(new ExpectedCondition<Collection<WebElement>>() {

            @Override
            public Collection<WebElement> apply(WebDriver webDriver) {
                Collection<WebElement> items = getItems();
                for (WebElement elem : items) {
                    // break if we don't have strings at all..
                    if (elem.getText().trim().length() > 0) {
                        return items;
                    }

                }
                return null;
            }
        });

        return CollectionUtils.find(items, new Predicate() {
            @Override
            public boolean evaluate(Object o) {
                return name.equalsIgnoreCase(((WebElement) o).getText());
            }
        }) != null;
    }

    public List<String> items() {
        ArrayList<String> textItems = new ArrayList<String>();
        for (WebElement item : getItems()) {
            textItems.add(item.getText());
        }
        return textItems;
    }

    private List<WebElement> getItems() {
        // expand the list, there's no way to find the elements without them being visible
        dropDownButton.click();
        waitFor.size(options, SizeCondition.gt(0));
        return options;
    }

}
