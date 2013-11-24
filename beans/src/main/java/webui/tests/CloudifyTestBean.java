package webui.tests;


import webui.tests.cloudify.commands.CloudifyCliManager;

/**
 * User: guym
 * Date: 3/6/13
 * Time: 4:09 PM
 */
public interface CloudifyTestBean {


    CloudifyCliManager.Execution bootstrap();

    CloudifyCliManager.Execution teardown();

    CloudifyCliManager.Execution installApplication();
}
