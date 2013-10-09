package org.cloudifysource.setup.installer;

/**
 * Created with IntelliJ IDEA.
 * User: guym
 * Date: 8/7/13
 * Time: 12:30 PM
 */
public abstract class AbstractCloudifyInstaller implements CloudifyInstaller {

    String targetDirectory;

    public boolean force; // override existing

    @Override
    public void invoke() {
        getCloudify();
    }
}
