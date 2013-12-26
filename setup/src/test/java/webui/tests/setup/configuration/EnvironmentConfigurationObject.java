package webui.tests.setup.configuration;

/**
 * Created with IntelliJ IDEA.
 * User: guym
 * Date: 8/9/13
 * Time: 4:03 PM
 */
public class EnvironmentConfigurationObject {

    @Source.Environment("FILE_LOCATION")
    public String configurationFileLocation;

    @Override
    public String toString() {
        return "EnvironmentConfigurationObject{" +
                "configurationFileLocation='" + configurationFileLocation + '\'' +
                '}';
    }
}
