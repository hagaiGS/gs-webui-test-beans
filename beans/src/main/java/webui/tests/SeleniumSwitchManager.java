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
import java.util.LinkedList;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: guym
 * Date: 9/18/13
 * Time: 2:29 PM
 */
@Component
public class SeleniumSwitchManager {

    private static Logger logger = LoggerFactory.getLogger(SeleniumSwitchManager.class);

    private Deque<WebElement> switchPath = new LinkedList<WebElement>();

    @Autowired
    private WebDriver webDriver;


    private void switchToPath() {

        webDriver.switchTo().defaultContent();
        List<WebElement> pathToTraverse = ListUtils.removeDuplicates(switchPath);
        Collections.reverse(pathToTraverse);
        for (WebElement webElement :  pathToTraverse ) {
            try {
                webDriver.switchTo().frame(webElement);
            } catch (RuntimeException e) {
                logger.error("unable to switch to " + webElement);
                throw e;
            }

        }
    }

    public void enter( List<WebElement> path ){
        switchPath.clear();
        switchPath.addAll(path);
        switchToPath();
    }

    public void leaveAll(){
        switchPath.clear();
        webDriver.switchTo().defaultContent();
    }

    public WebElement enter(WebElement webElement) {
        switchPath.push(webElement);
        switchToPath();
        return webElement;
    }


    public WebElement leave(WebElement webElement) {
        if (!switchPath.peek().equals(webElement)) {
            throw new RuntimeException(String.format("leaving object that I did not switch to!"));
        } else {
            WebElement pop = switchPath.pop();
            switchToPath();
            return pop.equals(webElement) ? webElement : null;
        }

    }


    public SeleniumSwitchManager setWebDriver(WebDriver webDriver) {
        this.webDriver = webDriver;
        return this;
    }

}
