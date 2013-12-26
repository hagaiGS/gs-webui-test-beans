package org.cloudifysource.setup.installer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: guym
 * Date: 8/7/13
 * Time: 12:11 PM
 */
public class CloudifyInstallerMock implements CloudifyInstaller{

    private static Logger logger = LoggerFactory.getLogger(CloudifyInstallerMock.class);

    @Override
    public void getCloudify() {
        logger.info("getting cloudify");
    }

    @Override
    public void invoke() {
        getCloudify();
    }
}
