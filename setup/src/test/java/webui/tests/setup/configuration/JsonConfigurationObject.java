package webui.tests.setup.configuration;

/**
 * User: guym
 * Date: 8/9/13
 * Time: 4:06 PM
 */
@Source.File
public class JsonConfigurationObject {

    public String configurationObject;

    public MoreConfiguration moreConfiguration;

    public static class MoreConfiguration{
        public String moreConfiguration;

        @Override
        public String toString() {
            return "MoreConfiguration{" +
                    "moreConfiguration='" + moreConfiguration + '\'' +
                    '}';
        }
    }

    public void setConfigurationObject(String configurationObject) {
        this.configurationObject = configurationObject;
    }

    public void setMoreConfiguration(MoreConfiguration moreConfiguration) {
        this.moreConfiguration = moreConfiguration;
    }

    @Override
    public String toString() {
        return "JsonConfigurationObject{" +
                "configurationObject='" + configurationObject + '\'' +
                ", moreConfiguration=" + moreConfiguration +
                '}';
    }
}
