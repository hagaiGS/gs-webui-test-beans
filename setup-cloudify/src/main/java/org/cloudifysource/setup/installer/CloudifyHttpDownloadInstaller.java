package org.cloudifysource.setup.installer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webui.tests.setup.configuration.ConfigurationPopulator;
import webui.tests.setup.configuration.Source;

import java.io.File;
import java.text.MessageFormat;


/**
 * User: guym
 * Date: 8/7/13
 * Time: 11:43 AM
 */
public class CloudifyHttpDownloadInstaller extends CloudifyWebInstaller{


    public String file = null;

    public String url;

    private HttpDownloadConf conf = null;

    private static Logger logger = LoggerFactory.getLogger(CloudifyHttpDownloadInstaller.class);

    @Override
    public void getCloudify() {
        try{
            if ( conf.getUrl() == null ){
                throw new RuntimeException("URL is null - unable to download");
            }
            if ( file == null ){
                throw new RuntimeException("File is null - unable to download");
            }

            downloadBinary( conf.getUrl(), new File(file) );
        }catch(Exception e){
            logger.error("unable to download cloudify from [{}]",conf.getUrl(), e);
            throw new RuntimeException(String.format("unable to download cloudify from [%s]", conf.getUrl()), e);
        }
    }

    public String url(){
        return conf == null ? url : conf.getUrl();
    }

    public CloudifyHttpDownloadInstaller setUrl(String url) {
        this.url = url;
        return this;
    }

    public CloudifyHttpDownloadInstaller setFile(String file) {
        this.file = file;
        return this;
    }

    public static interface HttpDownloadConf{
        public String getUrl();
    }

    public static class Conf implements HttpDownloadConf{

        private ConfigurationPopulator configurationPopulator;

        Data data;

        @Source.File
        public class Data{
            public String urlTemplate;

            @Source.Environment
            public String milestone;

            @Source.Environment
            public String buildNumber;

            @Source.Environment
            public String version;

            public String getDownloadUrl(){
                return MessageFormat.format(urlTemplate, version, buildNumber, milestone );
            }

        }

        private Data getData(){
            if ( data == null ){
                data = configurationPopulator.instantiate( Data.class );
                configurationPopulator.populate( data );
            }
            return data;
        }

        public String getUrl(){ return getData().getDownloadUrl(); }


        public void setConfigurationPopulator(ConfigurationPopulator configurationPopulator) {
            this.configurationPopulator = configurationPopulator;
        }
    }
}
