package webui.tests.remoteclient;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: guym
 * Date: 8/5/14
 * Time: 3:46 PM
 */
public class RemoteClientFactory {
    public String url = "http://pc-lab58:4444/wd/hub";

    DesiredCapabilities capability = DesiredCapabilities.firefox();

    public WebDriver getDriver(){
        try {
            return new RemoteWebDriver(new URL(url), capability);
        } catch (MalformedURLException e) {
            throw new RuntimeException("unable to load remote web driver",e);
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public DesiredCapabilities getCapability() {
        return capability;
    }

    public void setCapability(DesiredCapabilities capability) {
        this.capability = capability;
    }
}

