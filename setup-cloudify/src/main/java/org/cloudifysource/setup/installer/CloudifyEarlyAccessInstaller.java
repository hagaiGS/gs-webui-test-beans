package org.cloudifysource.setup.installer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: guym
 * Date: 8/7/13
 * Time: 12:40 PM
 */
public class CloudifyEarlyAccessInstaller extends CloudifyWebInstaller {

    public static final String ANCHOR_TAG_START = "<a class=\"eaTitle\" href=\"";
    String url = "http://www.cloudifysource.org/downloads/early_access";

    String outputPath = "output3.zip";


    private static Logger logger = LoggerFactory.getLogger(CloudifyLatestGaInstaller.class);

    @Override
    public void getCloudify() {
        String downloadUrl = getDownloadUrl();
        logger.info("my downloadUrl is [{}]", downloadUrl);
        downloadBinary(downloadUrl, new File(outputPath));
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDownloadUrl(){


        String html = getHtml( url );
        // now lets parse the HTML, we are looking for the HREF in the download link

//        <a class="eaTitle" href="http://repository.cloudifysource.org/org/cloudifysource/2.7.0-5981-M1/gigaspaces-cloudify-2.7.0-m1-b5981.zip" onclick="javascript: _gaq.push(['_trackPageview', '/downloads/gigaspaces-cloudify-2.7.0-m1-b5981.zip/early_access']);">Cloudify 2.7 M1</a>
        int start = html.indexOf( ANCHOR_TAG_START ) + ANCHOR_TAG_START.length() ;
        int end = html.indexOf("\"", start);
        String href = html.substring(start, end);
        return href;


    }

}
