package webui.tests.browserstack;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: guym
 * Date: 7/31/14
 * Time: 6:01 PM
 */
public class BrowserstackDriverFactory {
    public String username = "username";
    public String key = "key";
    public boolean debug = true;


    BrowserDetails browserDetails;



    public WebDriver getDriver(){
        try {
            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setCapability("browser", browserDetails.getBrowserName()); // IE
            caps.setCapability("browser_version", browserDetails.getBrowserVersion()); // 7.0
            caps.setCapability("os", browserDetails.getOsName()); // "Windows"
            caps.setCapability("os_version", browserDetails.getOsVersion()); // XP
            caps.setCapability("browserstack.debug", debug);
            caps.setCapability("resolution", browserDetails.getResolutionValue());

            WebDriver driver = new RemoteWebDriver(new URL(getUrl()), caps);
            return driver;
        }catch(Exception e){
            throw new RuntimeException("unable to open browserstack driver",e);
        }
    }

    public String getUrl(){
        return "http://" +  username + ":" + key + "@hub.browserstack.com/wd/hub";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public BrowserDetails getBrowserDetails() {
        return browserDetails;
    }

    public void setBrowserDetails(BrowserDetails browserDetails) {
        this.browserDetails = browserDetails;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}
