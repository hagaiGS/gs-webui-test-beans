package org.cloudifysource.setup.installer;

import webui.tests.setup.actions.SetupAction;

/**
 * User: guym
 * Date: 8/7/13
 * Time: 11:42 AM
 *
 * This interface should take care of getting cloudify from somewhere.
 * We can copy it, refer to an existing location or download it from online.
 *
 * TODO : guy - we need to somehow provide the JSHOMEDIR as well.
 *
 */
public interface CloudifyInstaller extends SetupAction{

    public void getCloudify();
}
