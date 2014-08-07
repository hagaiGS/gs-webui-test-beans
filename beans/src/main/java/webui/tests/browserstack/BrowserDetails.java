package webui.tests.browserstack;

/**
 * Created with IntelliJ IDEA.
 * User: guym
 * Date: 7/31/14
 * Time: 6:06 PM
 */
public class BrowserDetails {

    private Resolution resolution;

    private Browser browser;

    private Os os;


    public String getResolutionValue(){
        return resolution.value;
    }

    public String getBrowserName(){
        return browser.name;
    }
    public String getBrowserVersion(){
        return browser.version;
    }


    public String getOsName(){
        return os.name;
    }
    public String getOsVersion(){
        return os.version;
    }



    public Os getOs() {
        return os;
    }

    public void setOs(Os os) {
        this.os = os;
    }

    public Browser getBrowser() {
        return browser;
    }

    public void setBrowser(Browser browser) {
        this.browser = browser;
    }

    public Resolution getResolution() {
        return resolution;
    }

    public void setResolution(Resolution resolution) {
        this.resolution = resolution;
    }
}
