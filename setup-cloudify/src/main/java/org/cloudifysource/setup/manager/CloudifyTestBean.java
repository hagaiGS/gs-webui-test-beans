package org.cloudifysource.setup.manager;


import org.cloudifysource.setup.commands.CloudifyCliManager;

/**
 * User: guym
 * Date: 3/6/13
 * Time: 4:09 PM
 */
public interface CloudifyTestBean {


    public CloudifyCliManager.Execution bootstrap();

    public CloudifyCliManager.Execution teardown();
}
