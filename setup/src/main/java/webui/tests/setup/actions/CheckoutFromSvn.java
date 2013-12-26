package webui.tests.setup.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.wc2.SvnCheckout;
import org.tmatesoft.svn.core.wc2.SvnOperationFactory;
import org.tmatesoft.svn.core.wc2.SvnTarget;
import webui.tests.setup.configuration.ConfigurationPopulator;
import webui.tests.setup.configuration.Source;
import webui.tests.setup.configuration.managers.ConfigurationManager;

import java.io.File;

/**
 * User: guym
 * Date: 8/10/13
 * Time: 11:32 PM
 */
public class CheckoutFromSvn implements SetupAction{
    final SvnOperationFactory svnOperationFactory = new SvnOperationFactory();


    public CheckoutConfiguration conf = new CheckoutConfiguration();

    private static Logger logger = LoggerFactory.getLogger(CheckoutFromSvn.class);

    @Autowired
    private ConfigurationPopulator configurationPopulator;

    @Override
    public void invoke() {

        try {
            configurationPopulator.populate( conf );
            logger.info("invoking checkout with [{}]", conf);
            final SvnCheckout checkout = svnOperationFactory.createCheckout();
            checkout.setSingleTarget(SvnTarget.fromFile( new File(conf.outputDir) ));
            checkout.setSource(SvnTarget.fromURL( SVNURL.parseURIEncoded(conf.svnUrl)));
            //... other options
            checkout.run();
        } catch(Exception e){
            throw new RuntimeException( String.format("unable to checkout [%s]", conf ), e );
        }finally {
            svnOperationFactory.dispose();
        }
    }

    public void setConfigurationPopulator(ConfigurationPopulator configurationPopulator) {
        this.configurationPopulator = configurationPopulator;
    }

    public void setConf(CheckoutConfiguration conf) {
        this.conf = conf;
    }

    public static class CheckoutConfiguration{
        @Source.Environment("SVN_OUTPUT_DIR")
        public String outputDir;

        @Source.Environment("SVN_URL")
        public String svnUrl;

        public void setOutputDir(String outputDir) {
            this.outputDir = outputDir;
        }

        public void setSvnUrl(String svnUrl) {
            this.svnUrl = svnUrl;
        }

        @Override
        public String toString() {
            return "CheckoutConfiguration{" +
                    "outputDir='" + outputDir + '\'' +
                    ", svnUrl='" + svnUrl + '\'' +
                    '}';
        }
    }
}
