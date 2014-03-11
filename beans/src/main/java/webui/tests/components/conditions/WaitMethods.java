package webui.tests.components.conditions;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: guym
 * Date: 10/8/13
 * Time: 8:53 PM
 */
public interface WaitMethods<T> {
    public T elements( long timeout, TimeUnit unit, final WebElement... webElements );
    public T elements( final WebElement ... webElements );
    // waits until element is not visible
    public T hidden( SearchContext searchContext, By... findBys );
    public T hidden( long timeout, TimeUnit unit, SearchContext searchContext, By... findBys );

    public <V> V predicate( long timeout, TimeUnit unit, ExpectedCondition<V> predicate );
    public <V> V predicate( ExpectedCondition<V> predicate );
    public T size( Collection<WebElement> elements, SizeCondition cond );

}
