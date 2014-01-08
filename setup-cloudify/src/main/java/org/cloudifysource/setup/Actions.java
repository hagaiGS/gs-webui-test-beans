package org.cloudifysource.setup;

import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import webui.tests.setup.actions.SetupActions;
import webui.tests.utils.CollectionUtils;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Created with IntelliJ IDEA.
 * User: guym
 * Date: 11/28/13
 * Time: 2:16 PM
 */
public class Actions {

    private static Logger logger = LoggerFactory.getLogger(Actions.class);
    /**
     * This class helps breach the gap between the unzip and the CLI location.
     * Sometimes the zip extracts a folder "cloudify-X.X.X" and only then we have the heirarchy we know.
     */
    public static class ResolveCliFile extends SetupActions{

        public File root;


        @Autowired
        public BuildProperties buildProperties;

        @Override
        public void invoke() {
            File cloudifyRoot = root;
            logger.info(toString());
            if ( cloudifyRoot.isDirectory() ){
                File[] files = root.listFiles();
                if (CollectionUtils.size(files) == 1 && CollectionUtils.first(files).getName().startsWith("gigaspaces-cloudify-")){ // we need to go down more
                    cloudifyRoot = CollectionUtils.first(files);
                }

                logger.info("looking for tools folder");
                File[] binFolder = cloudifyRoot.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        return "bin".equals(name);
                    }
                });

                if ( CollectionUtils.isEmpty(binFolder)){
                    throw new RuntimeException("unable to find bin folder");
                }

                buildProperties.cliFile = new File(binFolder[0], "cloudify" + ( SystemUtils.IS_OS_WINDOWS ? ".bat" : ".sh" ));

                if ( buildProperties.cliFile.exists() ){
                    logger.info("found cli file at [{}]", buildProperties.cliFile);
                }else{
                    throw new RuntimeException(String.format("I thought cli file is at [%s] but it is not", buildProperties.cliFile));
                }

            }else{
                throw new RuntimeException(String.format("root [%s] is not a directory", root));
            }
        }

        public File getRoot() {
            return root;
        }

        public void setRoot(File root) {
            this.root = root;
        }

        public BuildProperties getBuildProperties() {
            return buildProperties;
        }

        public void setBuildProperties(BuildProperties buildProperties) {
            this.buildProperties = buildProperties;
        }

        @Override
        public String toString() {
            return "ResolveCliFile{" +
                    "root=" + root +
                    '}';
        }
    }
}
