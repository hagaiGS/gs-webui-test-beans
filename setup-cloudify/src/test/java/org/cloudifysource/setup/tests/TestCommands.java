package org.cloudifysource.setup.tests;

import org.cloudifysource.setup.manager.CloudifyTestBean;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import webui.tests.setup.configuration.managers.SetupActionsManager;


/**
 * User: guym
 * Date: 8/8/13
 * Time: 9:52 AM
 */

@ContextConfiguration
public class TestCommands  extends AbstractTestNGSpringContextTests {

    @Autowired
    private CloudifyTestBean cloudifyTestBean;

    @Autowired
    private SetupActionsManager.TestNg setupManager;


    @BeforeMethod
    public void before() {
        try {
            springTestContextPrepareTestInstance();
        } catch (Exception e) {
            throw new RuntimeException("unable to prepare context", e);
        }
        logger.info("before test");
        setupManager.executeBeforeTest();
    }

    @AfterMethod
    public void after(){
        logger.info("after test");
        setupManager.executeAfterTest();
    }
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

    public CloudifyTestBean getCloudifyTestBean() {
        return cloudifyTestBean;
    }

    public SetupActionsManager.TestNg getSetupManager() {
        return setupManager;
    }

    public void setSetupManager(SetupActionsManager.TestNg setupManager) {
        this.setupManager = setupManager;
    }
}
