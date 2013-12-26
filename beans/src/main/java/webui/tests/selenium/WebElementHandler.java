package webui.tests.selenium;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import webui.tests.annotations.FirstDisplayed;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: guym
 * Date: 12/26/13
 * Time: 2:43 PM
 */
public class WebElementHandler implements MethodInterceptor{

    private final WebDriver webDriver;
    private final ElementLocator locator;
    private final Field field;

    public WebElementHandler(Field field, ElementLocator locator, WebDriver webDriver) {
        this.field = field;
        this.locator = locator;
        this.webDriver = webDriver;
    }

    public boolean isFirstDisplayed(){
        return field.isAnnotationPresent(FirstDisplayed.class);
    }

    public Object getFirstDisplayed(){
        List<WebElement> elements = locator.findElements();
        for (WebElement element : elements) {
            if ( element.isDisplayed()){
                return element;
            }
        }
        return locator.findElement();
    }

    public Object locateElement(){
        return isFirstDisplayed() ? getFirstDisplayed() : locator.findElement();
    }


    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {

        return  method.invoke( locateElement(), objects);
    }

}
