package webui.tests.setup.configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created with IntelliJ IDEA.
 * User: guym
 * Date: 8/8/13
 * Time: 5:40 PM
 *
 * this class defines annotations for properties population.
 *
 * For example - define a properties class and annotate it with @EnvironmentConfiguration so to read
 * the properties from an environment variable or a system property.
 */
public @interface Source {


    @Target( ElementType.FIELD )
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface Environment{
        // defaults to field name
        public String value() default "";

    }

    @Target( { ElementType.FIELD , ElementType.TYPE } )
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface File{
        // defaults to class name with the matching manager's suffix
        public String value() default "";
    }
}
