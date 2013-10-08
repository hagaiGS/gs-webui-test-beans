package webui.tests.selenium;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webui.tests.SeleniumSwitchManager;
import webui.tests.annotations.NoEnhancement;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: guym
 * Date: 4/8/13
 * Time: 5:35 PM
 */
public class GsLocators {



    public static class ListHandler implements MethodInterceptor {

        private final ElementLocator locator;
        private final Class<? extends GsSeleniumComponent> type;
        private WebDriver webDriver = null;

        public ListHandler( ElementLocator locator, Class<? extends GsSeleniumComponent> type ) {
            this.locator = locator;
            this.type = type;
        }


        @Override
        public Object intercept( Object o, Method method, Object[] objects, MethodProxy methodProxy ) throws Throwable {
            List<WebElement> elements = locator.findElements();
            List<GsSeleniumComponent> components = ( List<GsSeleniumComponent> ) o;
            components.clear();
            for ( WebElement element : elements )
            {
                GsSeleniumComponent e = type.newInstance();
                e.setWebElement( element );
                e.setWebDriver( webDriver );
                components.add( e );
            }

            try
            {
                return methodProxy.invokeSuper( components, objects );
            } catch ( InvocationTargetException e )
            {
                // Unwrap the underlying exception
                throw e.getCause();
            }
        }
    }

    public static class ElementHandler implements MethodInterceptor {

        private static Logger logger = LoggerFactory.getLogger( ElementHandler.class );

        private final ElementLocator locator;
        private boolean firstDisplayed = false;
        private boolean switchTo = false;
        private WebDriver webDriver = null;
        private SeleniumSwitchManager switchManager = null;
        private Field field;
        // todo : add cache.

        private static Set<String> ignoredMethods = new HashSet<String>(  ){
            {
                add( "toString" );
                add( "hashCode" );
            }
        };

        public ElementHandler( Field field, ElementLocator locator, WebDriver webDriver, SeleniumSwitchManager switchManager  ) {
            this.locator = locator;
            this.webDriver = webDriver;
            this.field = field;
            this.switchManager = switchManager;
            logger.debug( "created handler for [{}]", field );
        }

        public ElementHandler setFirstDisplayed( boolean firstDisplayed ) {
            logger.debug( "setting firstDisplayed [{}] for [{}]", firstDisplayed, field );
            this.firstDisplayed = firstDisplayed;
            return this;
        }

        public ElementHandler setSwitchTo( boolean switchTo ){
            this.switchTo = switchTo;
            return this;
        }

        private WebElement getFirstDisplayed( ){
            List<WebElement> elements = locator.findElements();
            for ( WebElement webElement : elements )
            {
                if ( webElement.isDisplayed() )
                {
                    return webElement;
                }
            }
            return null;
        }

        private WebElement locateElement(){
            try{
               if ( switchElement != null ){
                   return switchElement;
               }
                if ( firstDisplayed ){
                    return  getFirstDisplayed();
                }else{
                    return  locator.findElement();
                }
            }catch( RuntimeException e ){
                try{
                logger.info("could not find element. This is the body text \n" + webDriver.findElement( By.cssSelector("body")).getText() );
                }catch( Exception err){  }

                throw e;
            }
        }

        WebElement switchElement = null;

        @Override
        public Object intercept( Object o, Method method, Object[] objects, MethodProxy methodProxy ) throws Throwable {

            if ( ignoredMethods.contains( method.getName() ) ){
                return methodProxy.invokeSuper( o, objects );
            }
            logger.debug( "[{}] intercepted method [{}] on object [{}]. Will search for first displayed [{}]", new Object[]{field, method, o, firstDisplayed} );
            if ( o instanceof GsSeleniumComponent )
            {
                WebElement element = null;
                boolean shouldSwitch = switchTo;
                // since we call "setWebElement" and "setWebDriver" from within THIS FUNCTION, we must not capture them
                // to avoid infinite recursive loop
                if ( !method.isAnnotationPresent(NoEnhancement.class)  )
                {
                    GsSeleniumComponent comp = ( GsSeleniumComponent ) o;

                    element = locateElement();

                    if ( !switchTo ){
                        comp.setWebElement( element );
                    }
                    comp.setWebDriver( webDriver );
                    comp.setSwitchManager( switchManager );
                }else{
                    shouldSwitch = false;
                }

                try
                {
                    if ( shouldSwitch ){
                        switchElement = switchManager.enter( element );

                    }

                    Object o1;
                    if ( method.getName().equals("enterFrame")){

                        switchManager.enter( element );
                        o1 = o;

                    }else if ( method.getName().equals("exitFrame") ){

                        switchManager.leave( element );
                        o1 = o;
                    }else{
                        o1 = methodProxy.invokeSuper(o, objects);
                    }


                    if ( shouldSwitch ){

                        switchElement = switchManager.leave( element );

                    }

                    return o1;
                } catch ( InvocationTargetException e )
                {
                    throw e.getCause();
                }


            }

            else if ( o instanceof WebElement && firstDisplayed ){// only handle first displayed
                WebElement displayedElement = locateElement();

                if ( displayedElement != null ){
                    logger.info( "found first displayed. invoking method" );
                    return method.invoke( displayedElement, objects );
                }else{
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
