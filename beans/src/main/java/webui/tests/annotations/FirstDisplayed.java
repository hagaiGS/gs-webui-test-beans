package webui.tests.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created with IntelliJ IDEA.
 * User: guym
 * Date: 12/26/13
 * Time: 1:41 PM
 *
 *
 * This annotation fixes a bug in selenium.
 *
 * If I have a selector such as :
 *
 *
 *   [input],div,.someting
 *
 *   Then selenium "findElement" will not work - even though there might be only a single element that fits this description.
 *
 *   So instead, we will run "findElement" behind the scenes and find the first displayed element for you.
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target (ElementType.FIELD)
public @interface FirstDisplayed {
}
