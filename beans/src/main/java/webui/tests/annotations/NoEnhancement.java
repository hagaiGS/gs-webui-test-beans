package webui.tests.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created with IntelliJ IDEA.
 * User: guym
 * Date: 9/21/13
 * Time: 5:16 PM
 *
 *
 * Tells enhancers to ignore this method
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NoEnhancement {
}
