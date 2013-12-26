package webui.tests.selenium;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.FieldDecorator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webui.tests.SeleniumSwitchManager;
import webui.tests.annotations.NoEnhancement;
import webui.tests.annotations.SwitchTo;
import webui.tests.utils.CollectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: guym
 * Date: 4/8/13
 * Time: 5:35 PM
 */
public class GsLocators {



    public static class ElementHandler implements MethodInterceptor {
        private static Logger logger = LoggerFactory.getLogger(ElementHandler.class);

        private final ElementLocator locator;
        private WebDriver webDriver = null;
        private Field field;
        private boolean isSwitchTo = false;
        private SeleniumSwitchManager switchManager;

        private List<WebElement> framePath;

        // this way we know when we actually leave this page
        // fixes scenario when page calls itself.
        AtomicInteger switches = new AtomicInteger(0);



        public ElementHandler(Field field, ElementLocator locator, WebDriver webDriver) {
            this.locator = locator;
            this.webDriver = webDriver;
            this.field = field;
            this.isSwitchTo = field.isAnnotationPresent( SwitchTo.class );
            logger.debug( field.getName() + " : created handler for [{}]", field);
        }

        public ElementHandler setSwitchManager( SeleniumSwitchManager switchManager ){
            this.switchManager = switchManager;
            framePath = switchManager.getCurrentPath();
            logger.info( field.getName() + " : my frame path length is : " + CollectionUtils.size(framePath) );
            return this;
        }

        private WebElement locateElement() {
            if ( isSwitchTo ){
                switchManager.enter(framePath);
            }
            return locator.findElement();
        }

        public WebElement getSwitchTo(){
            return locateElement();
        }



        private boolean isFirstLoad( Field field, Method method, Object o ){
            if ( o instanceof GsSeleniumComponent && "load".equals(method.getName()) ){
                GsSeleniumComponent comp = (GsSeleniumComponent)o;
                boolean loaded = comp.isLoaded();
                if ( loaded ){
                    logger.info("already loaded, reloading [{}]", fieldToString(field, method));
                }
                return !loaded;
            }
            return false;
        }

        private String fieldToString(Field field, Method method){
            return field.getDeclaringClass().getSimpleName() + "." + field.getName() + "#" + method.getName();
        }

        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            if ( method.isAnnotationPresent(NoEnhancement.class) || isFirstLoad( field, method, o) ){
                logger.debug("unenhanced [{}]", fieldToString(field,method));
                return  methodProxy.invokeSuper(o, objects);
            }

            logger.debug("intercepted [{}]", fieldToString(field,method));
            if (o instanceof GsSeleniumComponent) {
                GsSeleniumComponent comp = (GsSeleniumComponent) o;

                WebElement element = locateElement();

                if ( isSwitchTo ){
                    switches.incrementAndGet();
                    switchManager.enter( element );
                    comp.setWebElement( webDriver.findElement(By.cssSelector("body")));
                }else{
                    comp.setWebElement(element);
                }


                comp.setWebDriver(webDriver);

                try {
                    return methodProxy.invokeSuper(o, objects);
                } catch (InvocationTargetException e) {
                    throw e.getCause();
                } finally{

                    if ( isSwitchTo && switches.decrementAndGet() == 0 ){
                        switchManager.exit();
                    }
                }

            } else if (o instanceof WebElement ) {// only handle first displayed
                WebElement displayedElement = locateElement();

                if (displayedElement != null) {
                    logger.info("found first displayed. invoking method");
                    return method.invoke(displayedElement, objects);
                } else {
                    logger.info("unable to detect first displayed");
                }
            }

            return null;

        }

        @Override
        public String toString() {
            return "ElementHandler{" +
                    "field=" + field +
                    '}';
        }
    }
}
