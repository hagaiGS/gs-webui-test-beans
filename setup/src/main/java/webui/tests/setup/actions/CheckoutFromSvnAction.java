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

import javax.annotation.PostConstruct;
import java.io.File;

/**
 * User: guym
 * Date: 8/10/13
 * Time: 11:32 PM
 */
public class CheckoutFromSvnAction extends AbstractSetupAction{

    @Source.Environment("SVN_OUTPUT_DIR")
    public String outputDir;

    @Source.Environment("SVN_URL")
    public String svnUrl;


    private static Logger logger = LoggerFactory.getLogger(CheckoutFromSvnAction.class);


    @Override
    public void invoke() {
        SvnOperationFactory svnOperationFactory = new SvnOperationFactory();
        try {

            logger.info("invoking checkout with [{}]", this);
            final SvnCheckout checkout = svnOperationFactory.createCheckout();
            checkout.setSingleTarget(SvnTarget.fromFile( new File(outputDir) ));
            checkout.setSource(SvnTarget.fromURL( SVNURL.parseURIEncoded(svnUrl)));
            //... other options
            checkout.run();
        } catch(Exception e){
            throw new RuntimeException( String.format("unable to checkout [%s]", this ), e );
        }finally {
            svnOperationFactory.dispose();
        }
    }

    public String getOutputDir() {
        return outputDir;
    }

    public String getSvnUrl() {
        return svnUrl;
    }

    @Override
    public String toString() {
        return "CheckoutFromSvnAction{" +
                "outputDir='" + outputDir + '\'' +
                ", svnUrl='" + svnUrl + '\'' +
                '}';
    }
}
