package org.cloudifysource.setup.commands;

import org.apache.commons.collections.Closure;
import org.apache.commons.exec.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.cloudifysource.setup.BuildProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import webui.tests.exec.ExecutorFactory;
import webui.tests.utils.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * User: guym
 * Date: 3/21/13
 * Time: 11:55 AM
 *
 * This is a factory class for commands
 *
 */
public class CloudifyCliManager {


    // guy - todo - we can inject this value from the installer directly.
    @Autowired
    private BuildProperties buildProperties;

    long defaultTimeoutMillis = 120000; // 2 minutes

    @Autowired
    private ExecutorFactory executorFactory;

    private static Logger logger = LoggerFactory.getLogger( CloudifyCliManager.class );

    public Execution execute ( long timeout , CliCommand ... cliCommand){

        for (CliCommand command : cliCommand) {
            if ( command == null ){
                throw new RuntimeException( String.format("unable to execute NULL")  );
            }
        }

        CliCommand.CommandGroup group = new CliCommand.CommandGroup();
        CollectionUtils.addAll(group.commands, cliCommand );
        return execute( group.getCommandLineArgs(), timeout );
    }



    public Execution execute( CliCommand ... command ){
        return execute( defaultTimeoutMillis, command );
    }



    public Execution execute( String[] command , long timeout ) {


        if ( buildProperties == null || buildProperties.cliFile == null){
            throw new RuntimeException("exectuable is null. please specify where to find cloudify.sh/cloudify.bat scripts");
        }

        File executableFile = buildProperties.cliFile;

        if ( !executableFile.exists() ){
            throw new RuntimeException( String.format("clihomedir not found [%s]", executableFile)  );
        }

        CommandLine cmdLine = new CommandLine(executableFile);
        cmdLine.addArguments( command, false );

//        CommandLine cmdLine = new CommandLine( "echo hello" );
        logger.info( "running command [{}]", cmdLine.toString() );

        Executor executor = executorFactory.createNew();
        executor.setExitValue( 0 );
        MyStreamHandler streamHandler = new MyStreamHandler( );
        executor.setStreamHandler( streamHandler );

        try
        {

            Map<String, String> env = new HashMap<String,String>(  );
            env.put( "DEBUG", "true" );
            env.put( "VERBOSE", "true" );
            env.putAll( System.getenv() );
            int i = executor.execute( cmdLine, env );
            logger.info( "executor finished with : " + i );


        } catch ( ExecuteException e )
        {
            logger.error( "Failed to execute process. Exit value: " + e.getExitValue(), e );

            throw new RuntimeException( "Failed to execute process. Exit value: " + e.getExitValue(), e );
        } catch ( IOException e )
        {
            logger.error( "Failed to execute process", e );

            throw new RuntimeException( "Failed to execute process.", e );
        }
        return null; // new Execution().setStreamHandler( streamHandler );
    }

    public BuildProperties getBuildProperties() {
        return buildProperties;
    }

    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    public static class Execution {
           private MyStreamHandler streamHandler;

           public String getOutput() {
               return streamHandler.getOutput();
           }

           protected Execution setStreamHandler( MyStreamHandler streamHandler ) {
               this.streamHandler = streamHandler;
               return this;
           }
       }



    protected static class MyStreamOutputHandler extends LogOutputStream {
        private StringBuilder sb;
        private Logger logger;

        public MyStreamOutputHandler( String name ) {
            logger = LoggerFactory.getLogger( name );
        }

        @Override
        protected void processLine( String line, int level ) {
            logger.info( line ); // currently ignoring level. todo: use level;
            sb.append( line );
        }

        public String getOutput(){
            return sb.toString();
        }

        public MyStreamOutputHandler setSb( StringBuilder sb ) {
            this.sb = sb;
            return this;
        }
    }
       protected static class MyStreamHandler extends PumpStreamHandler {

           StringBuilder sb = new StringBuilder(  );


           public MyStreamHandler(  ) {

           }

           @Override
           protected void createProcessOutputPump( InputStream is, OutputStream os ) {
               super.createProcessOutputPump( is, new MyStreamOutputHandler( "cli-info " ).setSb( sb ) );
           }

           @Override
           protected void createProcessErrorPump( InputStream is, OutputStream os ) {
               super.createProcessErrorPump( is, new MyStreamOutputHandler("cli-error").setSb( sb ) );
           }

           public String getOutput() {
               return sb.toString();
           }

       }



    public void setExecutorFactory( ExecutorFactory executorFactory ) {
        this.executorFactory = executorFactory;
    }
}
