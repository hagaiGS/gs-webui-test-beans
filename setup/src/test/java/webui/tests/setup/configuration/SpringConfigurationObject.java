package webui.tests.setup.configuration;

/**
 * Created with IntelliJ IDEA.
 * User: guym
 * Date: 8/9/13
 * Time: 4:04 PM
 */
@Source.File
public class SpringConfigurationObject {

    public String configurationObject;

    public MoreConfiguration moreConfiguration;

    ;

    public static class MoreConfiguration{
        String moreConfiguration;

        @Override
        public String toString() {
            return "MoreConfiguration{" +
                    "moreConfiguration='" + moreConfiguration + '\'' +
                    '}';
        }

        public void setMoreConfiguration(String moreConfiguration) {
            this.moreConfiguration = moreConfiguration;
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
        return "SpringConfigurationObject{" +
                "configurationObject='" + configurationObject + '\'' +
                ", moreConfiguration=" + moreConfiguration +
                '}';
    }
}
