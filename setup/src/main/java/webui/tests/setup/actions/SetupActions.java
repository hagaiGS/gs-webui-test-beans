package webui.tests.setup.actions;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.Executor;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import webui.tests.exec.ExecutorFactory;
import webui.tests.utils.FileUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 */
public abstract class SetupActions extends AbstractSetupAction {

    private static Logger logger = LoggerFactory.getLogger(SetupActions.class);

    public static class DeleteFolder extends SetupActions{
        public String folder;
        @Override
        public void invoke() {
            logger.info(toString());
            try {
                FileUtils.deleteDirectory(new File(folder));
            } catch (IOException e) {
                throw new RuntimeException(String.format("unable to delete [%s]", folder),e);
            }
        }

        public String getFolder() {
            return folder;
        }

        public void setFolder(String folder) {
            this.folder = folder;
        }

        @Override
        public String toString() {
            return "DeleteFolder{" +
                    "folder='" + folder + '\'' +
                    '}';
        }
    }

    public static class CopyFile extends SetupActions{

        public String from;
        public String to;

        @Override
        public void invoke() {
            logger.info(toString());
            try {
                FileUtils.copyFile(new File(from), new File(to));
            } catch (Exception e) {
                throw new RuntimeException(String.format("unable to invoke copy file action [%s]", toString()), e);
            }
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        @Override
        public String toString() {
            return "CopyFile{" +
                    "from='" + from + '\'' +
                    ", to='" + to + '\'' +
                    '}';
        }
    }

    public static class ExecuteCommandAction extends SetupActions{

         @Autowired
        public ExecutorFactory executorFactory;

        public File workingDirectory;

        public String command;

        @Override
        public void invoke() {
            logger.info(toString());
            try{
                Executor executor = executorFactory.createNew();
                executor.setWorkingDirectory(workingDirectory);
                CommandLine parse = CommandLine.parse(command);
                logger.info("executing [{}]", parse);
                executor.execute(parse);
            }catch(Exception e){
                throw new RuntimeException( String.format("unable to execute command [%s]", command ),e);
            }
        }

        public ExecutorFactory getExecutorFactory() {
            return executorFactory;
        }

        public void setExecutorFactory(ExecutorFactory executorFactory) {
            this.executorFactory = executorFactory;
        }

        @Override
        public String toString() {
            return "ExecuteCommandAction{" +
                    "workingDirectory=" + workingDirectory +
                    ", command='" + command + '\'' +
                    '}';
        }
    }


    public static class Unzip extends SetupActions{

        public File archive;
        public File outputDir;

        private static Logger logger = LoggerFactory.getLogger(Unzip.class);

        @Override
        public void invoke() {
            logger.info(toString());
            try {
                ZipFile zipfile = new ZipFile(archive);
                for (Enumeration e = zipfile.entries(); e.hasMoreElements(); ) {
                    ZipEntry entry = (ZipEntry) e.nextElement();
                    unzipEntry(zipfile, entry);
                }
            } catch (Exception e) {
                throw new RuntimeException(String.format("unable to unzip [%s]", archive),e);
            }
        }

        public File getArchive() {
            return archive;
        }

        public void setArchive(File archive) {
            this.archive = archive;
        }

        public File getOutputDir() {
            return outputDir;
        }

        public void setOutputDir(File outputDir) {
            this.outputDir = outputDir;
        }



        private void unzipEntry(ZipFile zipfile, ZipEntry entry) throws IOException {


            if (entry.isDirectory()) {
                FileUtils.forceMkdir(new File(outputDir, entry.getName()));
                return;
            }

            File outputFile = new File(outputDir, entry.getName());
            if ( outputFile.getName().endsWith(".sh") ){
                if (!outputFile.setExecutable(true)){
                    logger.info("could no set executable for [{}]", outputFile);
                }
            }
            FileUtils.forceMkdir(outputFile.getParentFile());


            logger.trace("Extracting: " + entry);
            BufferedInputStream inputStream = new BufferedInputStream(zipfile.getInputStream(entry));
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));

            try {
                IOUtils.copy(inputStream, outputStream);


            } finally {
                outputStream.close();
                inputStream.close();
            }
        }

        @Override
        public String toString() {
            return "Unzip{" +
                    "archive=" + archive +
                    ", outputDir=" + outputDir +
                    '}';
        }
    }
}
