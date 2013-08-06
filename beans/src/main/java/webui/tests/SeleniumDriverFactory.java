package webui.tests;

import com.thoughtworks.selenium.Selenium;
import org.apache.commons.lang3.SystemUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverBackedSelenium;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;
import webui.tests.selenium.GsHtmlUnitDriver;

import java.io.File;
import java.util.Arrays;

/**
 * User: guym
 * Date: 3/6/13
 * Time: 4:54 PM
 */
public class SeleniumDriverFactory{

    private static Logger logger = LoggerFactory.getLogger( SeleniumDriverFactory.class );
    private int retries;


    private String chromeDriverPath = DefaultValues.get().getChromeDriverPath();

    private String gwtFirefoxDevXpi = "file:src/test/resources/selenium-drivers/gwt-dev-plugin-1-22.xpi";

    private String gwtChromeDevCrx = "file:src/test/resources/selenium-drivers/chrome_gwt_1_0_11357.crx";

    public static enum DriverType {
        CHROME, FIREFOX, IE, FIREFOX_GWT_DEV, CHROME_GWT_DEV, SAFARI, HTMLUNIT;
        // SAFARI_GWT_DEV // need a mac to run this one
    }

    private DriverType driverType = DriverType.CHROME;

    private WebDriver webDriver = null;

    private Selenium selenium = null;

    private String rootUrl = "http://localhost:8099";


    private ChromeDriverService chromeService = null;

    public SeleniumDriverFactory setDriverType( DriverType driverType ) {
        this.driverType = driverType;
        return this;
    }

    public void setDriverTypeString( String str ) {
        driverType = DriverType.valueOf( str.toUpperCase() );
    }

    public void initializeDriver() {
         logger.info("this is my location [{}]",new File(".").getAbsolutePath());
        switch ( driverType )
        {
            case SAFARI :
            {
                webDriver = new SafariDriver(  );
                break;
            }
            case CHROME_GWT_DEV:
            {
                try
                {
                    ChromeOptions options = new ChromeOptions();
                    logger.info( "using chrome crx [{}] and chrome driver [{}]", gwtChromeDevCrx, chromeDriverPath );
                    options.addExtensions( getResourceLocation( gwtChromeDevCrx ) );
                    DesiredCapabilities desired = DesiredCapabilities.chrome();
                    desired.setCapability( "chrome.switches", Arrays.asList( "--start-maximized" ) );
                    desired.setCapability( ChromeOptions.CAPABILITY, options ); // add the gwt dev plugin

                    chromeService = new ChromeDriverService.Builder().usingAnyFreePort().usingDriverExecutable( getResourceLocation( chromeDriverPath ) ).build();
                    logger.info( "Starting Chrome Driver Server..." );
                    chromeService.start();
                    webDriver = new RemoteWebDriver( chromeService.getUrl(), desired );

                } catch ( Exception e )
                {
                    logger.warn( "unable to initialize chrome [{}]", e.getMessage() );
                }
                break;
            }
            case CHROME:
            {
                try
                {
                    DesiredCapabilities desired = DesiredCapabilities.chrome();
                    desired.setCapability( "chrome.switches", Arrays.asList( "--start-maximized" ) );
                    chromeService = new ChromeDriverService.Builder().usingAnyFreePort().usingDriverExecutable( getResourceLocation( chromeDriverPath ) ).build();
                    logger.info( "Starting Chrome Driver Server..." );
                    chromeService.start();
                    webDriver = new RemoteWebDriver( chromeService.getUrl(), desired );

                } catch ( Exception e )
                {
                    logger.warn( "unable to initialize chrome [{}]", e.getMessage() );
                }
                break;
            }
            case FIREFOX_GWT_DEV:
            {
                try
                {
                    FirefoxProfile profile = new FirefoxProfile();
                    profile.addExtension( getResourceLocation( gwtFirefoxDevXpi ) );
                    webDriver = new FirefoxDriver( profile );

                } catch ( Exception e )
                {
                    logger.warn( "unable to initialize FIREFOX_GWT_DEV [{}]", e.getMessage() );
                }
                break;
            }
            case FIREFOX:
            {
                webDriver = new FirefoxDriver();
                break;
            }
            case IE:
            {
                DesiredCapabilities desired = DesiredCapabilities.internetExplorer();
                desired.setCapability( InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true );
                webDriver = new InternetExplorerDriver( desired );
                break;
            }
            case HTMLUNIT:
            {
                webDriver = new GsHtmlUnitDriver( );
            }

        }
    }

    private File getResourceLocation( String uri ) {
        try
        {
            return ResourceUtils.getFile( uri ).getAbsoluteFile();
        } catch ( Exception e )
        {
            throw new RuntimeException( String.format( "unable to get resource %s", uri ), e );
        }
    }

    public SeleniumDriverFactory init() {
        retries = 3;
        for ( int i = 0; i < retries; i++ )
        {
            logger.info( "initializing driver [{}] try [{}]", driverType, i );
            try
            {
                initializeDriver();

            } catch ( RuntimeException e )
            {
                logger.warn( "initializeDriver throws runtime exceptions", e.getMessage() );
            }
            if ( webDriver != null )
            {
                logger.info( "successfully initialized driver" );
                break;
            }
        }
        if ( webDriver == null )
        {
            logger.error( "unable to launch browser, failing test" );
            throw new RuntimeException();
        }

        selenium = new WebDriverBackedSelenium( webDriver, rootUrl );
        return this;
    }

    public WebDriver getDriver() {
        return webDriver;
    }

    public void quit(){
        logger.info("destroying driver");
        try
        {
            selenium.close();
        } catch ( Exception e )
        {
            logger.info( "error while closing selenium", e );
        }
        try
        {
            webDriver.quit();
        } catch ( Exception e )
        {
            logger.info( "error while closing webdriver", e );
        }


        try
        {
            if ( chromeService != null )
            {
                chromeService.stop();
                logger.info( "chrome service is still running [{}] ", chromeService.isRunning() );
            }
        } catch ( Exception e )
        {
            logger.info( "error while closing chromeService", e );
        }
    }

    public Selenium getSelenium() {
        return selenium;
    }

    public void setRetries( int retries ) {
        this.retries = retries;
    }

    public void setRootUrl( String rootUrl ) {
        this.rootUrl = rootUrl;
    }

    public String getRootUrl(){
        return rootUrl;
    }

    public void setChromeDriverPath( String chromeDriverPath ) {
        this.chromeDriverPath = chromeDriverPath;
    }

    public void setGwtFirefoxDevXpi( String gwtFirefoxDevXpi ) {
        this.gwtFirefoxDevXpi = gwtFirefoxDevXpi;
    }

    public void setGwtChromeDevCrx( String gwtChromeDevCrx ) {
        this.gwtChromeDevCrx = gwtChromeDevCrx;
    }



    public abstract static class DefaultValues{

        abstract public String getChromeDriverPath();

        static public DefaultValues get(){
            DefaultValues res = null;
            if ( SystemUtils.IS_OS_LINUX ){
                res = new Linux();
            } else if ( SystemUtils.IS_OS_WINDOWS ) {
                res = new Windows();
            }
            if (res != null) {
                logger.info("Using default values for OS [{}]", res.getClass().getSimpleName());
            }
            return res;
        }

        // TODO : create multiple options and try all.
        // this is due to a maven bug! when running webui.tests from command line, the resources are copied and loose their permissions.
        // since chrome files should be executable, this is a problem for us.
        // In IDEs, we need to use classpath, however in command line we can use "file:".
        // You can tell the IDE to use a different work folder as a work around.
        public static class Windows extends DefaultValues{
            @Override
            public String getChromeDriverPath() {
                return "file:src/test/resources/selenium-drivers/chromedriver_win_26.0.1383.0/chromedriver.exe";
            }
        }
        public static class Linux extends DefaultValues{
            @Override
            public String getChromeDriverPath() {
                return "file:src/test/resources/selenium-drivers/chromedriver_linux64_2.1/chromedriver";
            }
        }
    }
}
