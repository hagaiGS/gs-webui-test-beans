package org.cloudifysource.setup.tests;

import org.cloudifysource.setup.manager.CloudifyTestBean;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;


/**
 * User: guym
 * Date: 8/8/13
 * Time: 9:52 AM
 */

@ContextConfiguration
public class TestCommands  extends AbstractTestNGSpringContextTests {

    @Autowired
    private CloudifyTestBean cloudifyTestBean;

    @Test
    public void testCommands(){

        cloudifyTestBean.bootstrap();
        cloudifyTestBean.installService();
        cloudifyTestBean.uninstallService();
        cloudifyTestBean.installApplication();
        cloudifyTestBean.uninstallApplication();
        cloudifyTestBean.teardown();


    }



    public void setCloudifyTestBean(CloudifyTestBean cloudifyTestBean) {
        this.cloudifyTestBean = cloudifyTestBean;
    }
}
