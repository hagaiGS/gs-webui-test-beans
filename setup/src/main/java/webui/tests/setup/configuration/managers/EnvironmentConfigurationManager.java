package webui.tests.setup.configuration.managers;

import webui.tests.setup.configuration.Source;

import java.lang.reflect.Field;

/**
 * User: guym
 * Date: 8/8/13
 * Time: 5:43 PM
 */
public class EnvironmentConfigurationManager implements ConfigurationManager {


    @Override
    public <T> T instantiate(Class<T> clzz) {
        return null; // this manager cannot instantiate a class;
    }

    @Override
    public void populate(Data data) {
        for (Field field : data.getFields()) {
            if (field.isAnnotationPresent(Source.Environment.class)) {
                Source.Environment env = field.getAnnotation(Source.Environment.class);
                String key = field.getName();
                if (env.value().trim().length() > 0) {
                    key = env.value().trim();
                }
                String value = System.getProperty(key, System.getenv(key));
                if (value != null) {
                    data.set(field, value);
                }
            }
        }
    }
}

