package webui.tests;

import junit.framework.Assert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import webui.tests.setup.configuration.ConfigurationPopulator;
import webui.tests.setup.configuration.EnvironmentConfigurationObject;
import webui.tests.setup.configuration.JsonConfigurationObject;
import webui.tests.setup.configuration.SpringConfigurationObject;

/**
 * Created with IntelliJ IDEA.
 * User: guym
 * Date: 8/9/13
 * Time: 4:07 PM
 */
@ContextConfiguration
//@RunWith(SpringJUnit4ClassRunner.class)
public class TestConfigurationLoad  extends AbstractTestNGSpringContextTests {

    @Autowired
    public ConfigurationPopulator populator;

    private static Logger logger = LoggerFactory.getLogger(TestConfigurationLoad.class);

    @Test
    public void testConfigurationLoad(){

        System.setProperty("FILE_LOCATION", "This is the location");

        EnvironmentConfigurationObject obj = new EnvironmentConfigurationObject();
        populator.populate( obj );
        logger.error( obj.toString() );
        Assert.assertNotNull( obj.configurationFileLocation );


        JsonConfigurationObject instantiate = populator.instantiate(JsonConfigurationObject.class);
        logger.info(instantiate.toString());
        Assert.assertNotNull( "moreConfiguration should not be NULL",instantiate.moreConfiguration);

        SpringConfigurationObject instantiate2 = populator.instantiate( SpringConfigurationObject.class );
        logger.info(instantiate2.toString());
        Assert.assertNotNull( instantiate2.moreConfiguration );
    }



    public void setPopulator(ConfigurationPopulator populator) {
        this.populator = populator;
    }
}
