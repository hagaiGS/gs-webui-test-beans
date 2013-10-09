package webui.tests.setup.configuration;

/**
 * Created with IntelliJ IDEA.
 * User: guym
 * Date: 8/8/13
 * Time: 5:52 PM
 */
public interface ConfigurationPopulator {

    public <T> T instantiate( Class<T> clzz );

    /**
     * Should populate object's fields with configuration values.
     * @param o
     */
    public void populate( Object o );
}
