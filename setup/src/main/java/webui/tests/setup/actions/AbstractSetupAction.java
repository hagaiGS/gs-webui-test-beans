package webui.tests.setup.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import webui.tests.setup.configuration.ConfigurationPopulator;

import javax.annotation.PostConstruct;

/**
 * Created with IntelliJ IDEA.
 * User: guym
 * Date: 11/27/13
 * Time: 6:26 PM
 */
public abstract class AbstractSetupAction implements SetupAction {


    @Autowired(required = false)
    private ConfigurationPopulator configurationPopulator;

    private static Logger logger = LoggerFactory.getLogger(AbstractSetupAction.class);

    @PostConstruct
    public void overrideConfiguration() {
        if (configurationPopulator != null) {
            logger.info("applying environment variables, class [{}]", getClass().getName() );
            configurationPopulator.populate(this);
        }else{
            logger.info("configuration populator does not exist. values from environment will not be applied, class [{}]", getClass().getName() );
        }
    }


}
