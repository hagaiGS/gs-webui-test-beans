package webui.tests.selenium;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.DefaultFieldDecorator;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.FieldDecorator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import webui.tests.SeleniumSwitchManager;
import webui.tests.annotations.Absolute;
import webui.tests.annotations.LazyLoad;
import webui.tests.annotations.SwitchTo;
import webui.tests.components.conditions.WaitMethods;

import java.lang.reflect.Field;

/**
 * User: guym
 * Date: 4/8/13
 * Time: 5:18 PM
 */
public class GsFieldDecorator implements FieldDecorator {

    private static Logger logger = LoggerFactory.getLogger(GsFieldDecorator.class);

    final DefaultFieldDecorator defaultFieldDecorator;

    final SearchContext searchContext;
    private final WebDriver webDriver;
    private SeleniumSwitchManager switchManager;
    @Autowired
    private WaitMethods waitFor;




    public GsFieldDecorator( SearchContext searchContext, WebDriver webDriver ) {
        this.searchContext = searchContext;
        this.webDriver = webDriver;
        defaultFieldDecorator = new DefaultFieldDecorator( new DefaultElementLocatorFactory( searchContext ) );
    }


    public Object getEnhancedObject( Class clzz, MethodInterceptor methodInterceptor  ){
        Enhancer e = new Enhancer();
        // We could do a better abstraction here..
        // We could use a factory to return the Implementing class for each type.
        // For example, we could define SelectComponent, and map it to MograblogSelectComponent
        // in the factory.
        e.setSuperclass(clzz);
        e.setCallback( methodInterceptor );
        return e.create();
    }

    public static void initElementsForEnhancedObject(FieldDecorator decorator, Object page) {
        Class<?> proxyIn = page.getClass().getSuperclass();
        while (proxyIn != Object.class) {
            proxyFields(decorator, page, proxyIn);
            proxyIn = proxyIn.getSuperclass();
        }
    }

    private static void proxyFields(FieldDecorator decorator, Object page, Class<?> proxyIn) {
        Field[] fields = proxyIn.getDeclaredFields();
        for (Field field : fields) {
            Object value = decorator.decorate(page.getClass().getClassLoader(), field);
            if (value != null) {
                try {
                    field.setAccessible(true);
                    field.set(page, value);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    // allow infinite layers of wrapping - initialize components with PageFactory.
    private void initializeElement( Field field, GsSeleniumComponent enhancedObject ) {
        field.setAccessible(true);
        logger.debug("loading field :  " + field.getName());

        try {
            // NOTE : use the getter function so that CGLIB will intercept it and inject the root element.
            if ( field.isAnnotationPresent( SwitchTo.class )){
                WebElement rootElement = enhancedObject.getRootElement();

                switchManager.enter( getElementHandler(field).getSwitchTo() );

                initElementsForEnhancedObject(new GsFieldDecorator(rootElement, webDriver).setSwitchManager(switchManager).setWaitFor(waitFor), enhancedObject);

                switchManager.exit();
            }else{
                initElementsForEnhancedObject(new GsFieldDecorator( enhancedObject.getRootElement(), webDriver).setSwitchManager( switchManager ).setWaitFor(waitFor), enhancedObject);
            }
        } catch (RuntimeException e) {
            String msg = String.format("problems loading field [%s]", field.getClass().getName() + "#" + field.getName());
            logger.info(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    @Override
    public Object decorate( ClassLoader loader, Field field ) {
        if ( GsSeleniumComponent.class.isAssignableFrom( field.getType() )  && field.isAnnotationPresent( FindBy.class )) {
            final GsSeleniumComponent enhancedObject =  (GsSeleniumComponent) getEnhancedObject( field.getType(), getElementHandler( field ) );

            enhancedObject.setWaitFor(waitFor);
            enhancedObject.setSwitchManager(switchManager);

            if ( !field.isAnnotationPresent(LazyLoad.class) || field.getAnnotation(LazyLoad.class).value() == false ){
                initializeElement( field, enhancedObject );
            }

            return enhancedObject;

        }else{
            return defaultFieldDecorator.decorate( loader, field );
        }
    }

    public GsFieldDecorator setSwitchManager(SeleniumSwitchManager switchManager) {
        this.switchManager = switchManager;
        return this;
    }

    private GsLocators.ElementHandler getElementHandler( Field field ) {
        return new GsLocators.ElementHandler( field, getLocator( field ), webDriver ).setSwitchManager( switchManager );
    }

    private ElementLocator getLocator( Field field ) {
        if ( field.isAnnotationPresent(Absolute.class) ){
            return new DefaultElementLocatorFactory( webDriver ).createLocator( field );
        }else{
            return new DefaultElementLocatorFactory( searchContext ).createLocator( field );
        }

    }

    public GsFieldDecorator setWaitFor(WaitMethods waitFor) {
        this.waitFor = waitFor;
        return this;
    }
}
