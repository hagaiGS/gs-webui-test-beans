package org.cloudifysource.setup.manager;

import org.cloudifysource.setup.commands.CloudifyCliManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


import static org.cloudifysource.setup.commands.CliCommand.*;

/**
 * User: guym
 * Date: 3/6/13
 * Time: 4:09 PM
 */
public class CloudifyTestBeanImpl implements CloudifyTestBean {

    private static Logger logger = LoggerFactory.getLogger( CloudifyTestBean.class );

    @Autowired
    private CloudifyCliManager cloudifyCliManager;

    /************ Command flags ******************/

    @Autowired(required = false)
    public Connect connect;

    @Autowired(required = false)
    public Teardown teardown;

    @Autowired(required = false)
    public InstallService installService;

    @Autowired(required = false)
    public InstallApplication installApplication;

    @Autowired(required = false)
    public UninstallService uninstallService;

    @Autowired(required = false)
    public UninstallApplication uninstallApplication;

    @Autowired(required = false)
    public Bootstrap bootstrap;


    /************* Connect Flags *****************/

    @Autowired( required = false )
    private int restPort = 8100;

    @Autowired( required = false )
    private String cloudifyHost = "localhost";

    @Override
    public CloudifyCliManager.Execution teardown() {
        logger.info( "cloudify bean teardown" );
        return cloudifyCliManager.execute(connect, teardown);
    }

    public CloudifyCliManager.Execution installService(){
        logger.info("installingService");
        return cloudifyCliManager.execute( connect, installService );
    }

    @Override
    public CloudifyCliManager.Execution bootstrap() {
        logger.info( "cloudify bean bootstrapping" );
        return cloudifyCliManager.execute( bootstrap );
    }

    @Override
    public CloudifyCliManager.Execution installApplication() {
        logger.info("cloudify bean installApplication");
        return cloudifyCliManager.execute( connect, installApplication );
    }

    @Override
    public CloudifyCliManager.Execution uninstallApplication() {
        logger.info("cloudify bean uninstallApplication");
        return cloudifyCliManager.execute( connect, uninstallApplication );
    }

    @Override
    public CloudifyCliManager.Execution uninstallService() {
        logger.info("cloudify bean uninstallService");
        return cloudifyCliManager.execute( connect, uninstallService );
    }

    public void setCloudifyCliManager( CloudifyCliManager cloudifyCliManager ) {
        this.cloudifyCliManager = cloudifyCliManager;
    }


}
