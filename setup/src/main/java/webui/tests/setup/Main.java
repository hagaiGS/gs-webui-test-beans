package webui.tests.setup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import webui.tests.setup.actions.SetupAction;
import webui.tests.setup.configuration.managers.SetupActionsManager;

import java.util.List;

/**
 * User: guym
 * Date: 8/11/13
 * Time: 6:51 AM
 */
public class Main implements SetupManager {

    public static final String SETUP_CONTEXT = "SETUP_CONTEXT";

    @Autowired
    private SetupActionsManager.Main setupManager;


    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        String str = System.getProperty(SETUP_CONTEXT, System.getenv(SETUP_CONTEXT));
        if ( str == null || str.trim().length() == 0 ){
            logger.info("no setup context defined. fallback to noop");
            return;
        }

        ApplicationContext context = new GenericXmlApplicationContext( str );
        SetupManager setupMain = (SetupManager) context.getBean("main");
        setupMain.setup();
    }

    @Override
    public void setup() {
        setupManager.invokeActions();
    }
}
