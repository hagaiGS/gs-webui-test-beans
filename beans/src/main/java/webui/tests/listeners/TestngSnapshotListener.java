package webui.tests.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webui.tests.SeleniumScreenshotTaker;
import webui.tests.annotations.NoScreenshot;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

import org.testng.*;

/**
 * User: guym
 * Date: 8/11/13
 * Time: 2:53 PM
 */
public class TestngSnapshotListener extends TestListenerAdapter {


        private static Logger logger = LoggerFactory.getLogger(TestngSnapshotListener.class);

    @Override
    public void onTestFailure(ITestResult failure)  {
        logger.info( "testFailure : [{}].", failure );
            if ( failure.getMethod().getConstructorOrMethod().getMethod().isAnnotationPresent( NoScreenshot.class ) ){
                logger.info( "skipping screenshot due to annotation" );
            }else{
                try{
                String filename = "screenshot" + System.currentTimeMillis() + ".jpg";
                    try{
                        filename = failure.getTestName().replaceAll( "\\(", "_" ).replaceAll( "\\)","_" ) + "_" + System.currentTimeMillis() + ".jpg";
                    }catch(Exception e){
                        logger.error("unable to give proper name to image", failure );
                    }
                logger.info( "saving screenshot to file [{}]", filename );
                new SeleniumScreenshotTaker().takeScreenshot( filename );
                super.onTestFailure( failure );
                }catch(Exception e){
                    throw new RuntimeException( String.format("unable to take screenshot"), e );
                }
            }
    }

    @Override
    public void onStart(ITestContext testContext) {
        logger.info("started!");

    }

    @Override
    public void onFinish(ITestContext testContext) {

        logger.info("finished!");
    }

    //        @Override
//        public void testFailure( Failure failure ) throws Exception {
//
//        }




}
