package webui.tests.setup.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webui.tests.setup.configuration.managers.ConfigurationManager;

import java.util.LinkedList;
import java.util.List;

/**
 * User: guym
 * Date: 8/8/13
 * Time: 5:52 PM
 */
public class ConfigurationPopulatorImpl implements ConfigurationPopulator{

    List<ConfigurationManager> managers = new LinkedList<ConfigurationManager>();

    private static Logger logger = LoggerFactory.getLogger(ConfigurationPopulatorImpl.class);

    @Override
    public <T> T instantiate(Class<T> clzz) {
        for (ConfigurationManager manager : managers) {
            T t = manager.instantiate( clzz );
            if ( t != null ){
                return t; // return the first instantiation
            }
        }
        return null; // could not instantiate
    }

    @Override
    public void populate(Object o) {
        Class<?> clzz = o.getClass();
        logger.info("populating [{}]", clzz);

        ConfigurationManager.Data d = new ConfigurationManager.Data().setClzz(clzz).setObj(o);
        for (ConfigurationManager manager : managers) {
            manager.populate(d);
        }
    }

    public void setManagers(List<ConfigurationManager> managers) {
        this.managers = managers;
    }
}
