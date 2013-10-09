package webui.tests.setup.configuration.managers;

/**
 * Created with IntelliJ IDEA.
 * User: guym
 * Date: 8/10/13
 * Time: 8:15 PM
 */
public class SimpleInstantiator implements ConfigurationManager{
    @Override
    public void populate(Data data) {

    }

    @Override
    public <T> T instantiate(Class<T> clzz) {
        try {
            return clzz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException( String.format("unable to instantiate [%s]", clzz), e );
        }
    }
}
