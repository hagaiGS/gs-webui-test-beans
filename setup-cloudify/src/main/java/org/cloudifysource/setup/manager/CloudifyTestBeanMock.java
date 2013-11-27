package org.cloudifysource.setup.manager;

import org.cloudifysource.setup.commands.CloudifyCliManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: guym
 * Date: 3/21/13
 * Time: 3:41 PM
 */
public class CloudifyTestBeanMock implements CloudifyTestBean{

    private static Logger logger = LoggerFactory.getLogger(CloudifyTestBeanMock.class);


    @Override
    public CloudifyCliManager.Execution bootstrap() {
        logger.info("bootstrap");
        return null;
    }

    @Override
    public CloudifyCliManager.Execution teardown() {
        logger.info("teardown");
        return null;
    }

    @Override
    public CloudifyCliManager.Execution installApplication() {
        logger.info("installApplication");
        return null;
    }

    @Override
    public CloudifyCliManager.Execution uninstallApplication() {
        logger.info("uninstallApplication");
        return null;
    }

    @Override
    public CloudifyCliManager.Execution installService() {
        logger.info("installService");
        return null;
    }

    @Override
    public CloudifyCliManager.Execution uninstallService() {
        logger.info("uninstallService");
        return null;
    }
}
