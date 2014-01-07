package webui.tests.setup.actions;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.Executor;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import webui.tests.exec.ExecutorFactory;
import webui.tests.utils.CollectionUtils;
import webui.tests.utils.FileUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 */
public abstract class SetupActions extends AbstractSetupAction {

    private static Logger logger = LoggerFactory.getLogger(SetupActions.class);

    public static class CleanFolders extends SetupActions{
        public String archivesDirectory;
        public String extractedArchiveFolder;
        @Override
        public void invoke() {
            logger.info(toString());
            try {
                FileUtils.cleanDirectory(new File(archivesDirectory));
            } catch (Exception e) {
                throw new RuntimeException(String.format("unable to delete [%s]", archivesDirectory),e);
            }
            try {
                FileUtils.cleanDirectory(new File(extractedArchiveFolder));
            } catch (Exception e) {
                throw new RuntimeException(String.format("unable to delete [%s]", extractedArchiveFolder),e);
            }
        }


        public void setArchivesDirectory(String archivesDirectory) {
            this.archivesDirectory = archivesDirectory;
        }
        
        public void setExtractedArchiveFolder(String extractedArchiveFolder) {
            this.extractedArchiveFolder = extractedArchiveFolder;
        }

        @Override
        public String toString() {
            return "CleanFolder{" +
                    "archivesDirectory='" + archivesDirectory + '\'' +
                    ", extractedArchiveFolder='" + extractedArchiveFolder + '\'' +
                    '}';
        }
    }

    public static class CopyFile extends SetupActions{

        public String from;
        public String to;
        public String archive;

        @Override
        public void invoke() {
            logger.info(toString());
            try {
                FileUtils.copyFileToDirectory(new File(calculateArchiveFilePath( from, archive )), new File(to));
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
        
        public void setArchive(String archive) {
            this.archive = archive;
        }          

        @Override
        public String toString() {
            return "CopyFile{" +
            		"archive='" + archive + '\'' +
                    ", from='" + from + '\'' +
                    ", to='" + to + '\'' +
                    '}';
        }
    }

    public static class ExecuteCommandAction extends SetupActions{

         @Autowired
        public ExecutorFactory executorFactory;

        public File workingDirectory;

        public String executable;

        public String[] commandLineArgs = new String[0];

        @Override
        public void invoke() {
            logger.info(toString());
            CommandLine parse = null;

            Executor executor = executorFactory.createNew();
            try{

            	workingDirectory = locateBinDirectory();
            	
            	//tODO use assert
//            	Assert
            	
                executor.setWorkingDirectory(workingDirectory);
        		// need to use the call command to intercept the cloudify batch file return code.
                List<String> cmdArgs = new ArrayList<String>( commandLineArgs.length + 4 );
            	if( isWindows() ) {
            		cmdArgs.add( "cmd" );
            		cmdArgs.add( "/c" );
            		cmdArgs.add( "call" );
            	}
        		
            	cmdArgs.add( executable );
        		
        		for( String arg : commandLineArgs ){
        			cmdArgs.add( arg );
        		}
            	
                parse = new CommandLine( cmdArgs.get( 0 ) );
                int argsCount =  cmdArgs.size();
                
                if ( argsCount > 1 ){
                	//add rest of arguments to CommandLine
                	parse.addArguments( cmdArgs.subList( 1, argsCount ).toArray( new String[ argsCount -1 ] ), false );
                }
                
            }catch(RuntimeException e){
                logger.error("unable to parse command [{}] , [{}]", executable, commandLineArgs);
                throw e;
            }
            try{
                logger.info("executing [{}]", parse);
                executor.execute(parse);
            }catch(Exception e){
                throw new RuntimeException( String.format("unable to execute command [%s]", parse ),e);
            }
        }

        private File locateBinDirectory() {
        	
            File[] files = workingDirectory.listFiles();
            File cloudifyRoot;
            File cloudifyBin = null;
            if (CollectionUtils.size(files) == 1 && CollectionUtils.first(files).getName().startsWith("gigaspaces-cloudify-")){ // we need to go down more
                cloudifyRoot = CollectionUtils.first(files);
                File[] binFolders = cloudifyRoot.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        return "bin".equals(name);
                    }
                });
                if( !CollectionUtils.isEmpty( binFolders ) ){
                	cloudifyBin = binFolders[ 0 ] ;
                }
                 
            }        	
			return cloudifyBin;
		}

		public ExecutorFactory getExecutorFactory() {
            return executorFactory;
        }

        public void setExecutorFactory(ExecutorFactory executorFactory) {
            this.executorFactory = executorFactory;
        }

        public void setCommandLineArgs(String[] commandLineArgs) {
            this.commandLineArgs = commandLineArgs;
        }

        public void setWorkingDirectory(File workingDirectory) {
            this.workingDirectory = workingDirectory;
        }

        public void setExecutable(String executable) {
            this.executable = executable;
        }

        @Override
        public String toString() {
            return "ExecuteCommandAction{" +
                    "workingDirectory=" + workingDirectory +
                    ", executable='" + executable + '\'' +
                    ", args='" + Arrays.toString(commandLineArgs) + '\'' +
                    '}';
        }
    }


    public static class Unzip extends SetupActions{

        public String archivesDirectory;
        public File outputDir;
        public String archive;

        private static Logger logger = LoggerFactory.getLogger(Unzip.class);
		private File extractedArchive;

        @Override
        public void invoke() {
            logger.info(toString());
            try {
                ZipFile zipfile = new ZipFile( calculateArchiveFilePath( archivesDirectory, archive ) );
                for (Enumeration e = zipfile.entries(); e.hasMoreElements(); ) {
                    ZipEntry entry = (ZipEntry) e.nextElement();
                    String name = entry.getName();
                   	unzipEntry(zipfile, entry);
                }
            } catch (Exception e) {
                throw new RuntimeException(String.format("unable to unzip [%s]", archivesDirectory),e);
            }
        }

        public String getArchive() {
            return archive;
        }        
        
        public void setArchivesDirectory(String archivesDirectory) {
            this.archivesDirectory = archivesDirectory;
        }

        public File getOutputDir() {
            return outputDir;
        }

        public void setOutputDir(File outputDir) {
            this.outputDir = outputDir;
        }

        public void setArchive(String archive) {
            this.archive = archive;
        }   

        public File getExtractedArchive(){
        	return extractedArchive;
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
            
            File parentFile = outputFile.getParentFile();
            FileUtils.forceMkdir( parentFile );
            if( extractedArchive == null ){
            	extractedArchive = parentFile; 
            	logger.info("Extracted to [{}]", extractedArchive.getAbsolutePath() );
            }

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
    
    private static String calculateArchiveFilePath( String archivesFolder, String archive ){
    	String path;
    	if( !archivesFolder.endsWith( File.separator ) ){
    		path = archivesFolder + File.separator;
    	}
    	else{
    		path = archivesFolder;
    	}
    	path += archive;
    	return path;
    }
    
	public static boolean isWindows() {
		return System.getProperty("os.name").toLowerCase().startsWith("win");
	}    
}
