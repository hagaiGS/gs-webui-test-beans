package org.cloudifysource.setup.installer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: guym
 * Date: 8/7/13
 * Time: 12:35 PM
 *
 * installs cloudify from
 */
public class CloudifyLatestGaInstaller extends CloudifyWebInstaller {

    public static final String ANCHOR_TAG_START = "<a class=\"dwnGoBtn\" id=\"downloadme\" href=\"";
    String url = "http://www.cloudifysource.org/downloads/get_cloudify";

    String outputPath = "output2.zip";


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
        // <a class="dwnGoBtn" id="downloadme" href="http://repository.cloudifysource.org/org/cloudifysource/community/gigaspaces-cloudify-2.6.0-ga-b5000.zip" style="display: block;"></a>
        int start = html.indexOf( ANCHOR_TAG_START ) + ANCHOR_TAG_START.length() ;
        int end = html.indexOf("\"", start);
        String href = html.substring(start, end);
        return href;


    }



}
