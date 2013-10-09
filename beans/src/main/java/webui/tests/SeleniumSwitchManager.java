package webui.tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import webui.tests.utils.ListUtils;

import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;


/**
 * Created with IntelliJ IDEA.
 * User: guym
 * Date: 9/18/13
 * Time: 2:29 PM
 */
@Component
public class SeleniumSwitchManager {

    private static Logger logger = LoggerFactory.getLogger(SeleniumSwitchManager.class);

    private Stack<WebElement> switchStack = new Stack<WebElement>();

    @Autowired
    private WebDriver webDriver;



    public void enter(WebElement e) {
        switchStack.push(e);
        enterStack();
    }

    private void enterStack( ){
        webDriver.switchTo().defaultContent();

        Set<WebElement> memory = new HashSet<WebElement>();

        for (WebElement webElement : switchStack) {
            if ( !memory.contains(webElement)){
                memory.add(webElement);
                try{
                    webDriver.switchTo().frame( webElement );
                }catch(Exception e){
                    logger.error("nable to switch to element",e);
                }
            }
        }
    }



    public SeleniumSwitchManager enter(List<WebElement> handler) {
        switchStack.clear();
        switchStack.addAll(handler);
        enterStack();
        return this;
    }

    public SeleniumSwitchManager goToDefaultContent(){
        webDriver.switchTo().defaultContent();
        return this;
    }

    public void exit() {
        switchStack.pop();
        webDriver.switchTo().defaultContent();
        enterStack();
    }

    public List<WebElement> getCurrentPath() {
        return new LinkedList<WebElement>(switchStack);
    }

    public SeleniumSwitchManager setWebDriver(WebDriver webDriver) {
        this.webDriver = webDriver;
        return this;
    }

}
