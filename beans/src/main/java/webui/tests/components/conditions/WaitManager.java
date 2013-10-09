package webui.tests.components.conditions;

import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.perf4j.StopWatch;
import org.perf4j.slf4j.Slf4JStopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * User: guym
 * Date: 10/8/13
 * Time: 8:12 PM
 */
@Component
public class WaitManager implements WaitMethods<WaitManager>{

    private static Logger logger = LoggerFactory.getLogger(WaitManager.class);
    protected static StopWatch stopWatch = new Slf4JStopWatch( logger );
    public static final int DEFAULT_TIMEOUT = 120000;
    private static final String TOTAL_WAIT = "total_wait";
    private static final String ELEMENT_WAIT = "element_wait";
    private static final long SLEEP_DELTA_MILLIS = 100;

    @Autowired
    private WebDriver webDriver;

    public <V> V predicate( ExpectedCondition<V> predicate ){
        return predicate( DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS, predicate );
    }

    public <V> V predicate( long timeout, TimeUnit unit, ExpectedCondition<V> predicate ){
        try
        {
            stopWatch.start( ELEMENT_WAIT );
            return new WebDriverWait( webDriver, unit.toMillis( timeout ), SLEEP_DELTA_MILLIS )
                    .pollingEvery( SLEEP_DELTA_MILLIS, TimeUnit.MILLISECONDS )
                    .withTimeout( timeout, unit )
                    .ignoring( RuntimeException.class, ElementNotVisibleException.class )
                    .until( predicate );
        } catch ( Exception e )
        {
            throw new RuntimeException( String.format( "waited %s millis for predicate [%s] and failed", predicate.toString(), unit.toMillis( timeout ) ), e );
        } finally
        {
            stopWatch.stop( ELEMENT_WAIT );
        }

    }

    public WaitManager elements( final WebElement... webElements ) {
        elements(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS, webElements);
        return this;
    }

    /**
     * waits for all webElements to be visible.
     *
     * @param timeout     - total timeout
     * @param unit        - timeout unit
     * @param webElements - webElements we are waiting for.
     *                    can have "List", or "WebElement" or "AbstractComponent"
     */
    public WaitManager elements(long timeout, TimeUnit unit, final WebElement... webElements) {
        try {
            stopWatch.start(TOTAL_WAIT + "_" + hashCode());
            for (WebElement o : webElements) {
                predicate(timeout, unit, ExpectedConditions.visibilityOf(o));
            }

        } finally {
            stopWatch.stop(TOTAL_WAIT + "_" + hashCode());
        }
        return this;
    }

    @Override
    public WaitManager size(final Collection<WebElement> elements, final SizeCondition cond) {
        predicate(new ExpectedCondition<Object>() {
            @Override
            public Object apply( WebDriver webDriver) {
                try{
                    return cond.applies( elements.size() ) ? elements : null ;
                }catch(Exception e){
                    logger.error("unable to determine collection size",e);
                }
                return null;
            }
        });
        return this;
    }

    public void setWebDriver(WebDriver webDriver) {
        this.webDriver = webDriver;
    }
}
