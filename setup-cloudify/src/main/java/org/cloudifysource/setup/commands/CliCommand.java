package org.cloudifysource.setup.commands;

import org.apache.commons.collections.CollectionUtils;
import org.cloudifysource.setup.manager.CloudDetails;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * User: guym
 * Date: 8/7/13
 * Time: 5:53 PM
 * <p/>
 * This is a new take on the cloudify command interface,
 * which was cool on the one hand but the implementation was long.
 * <p/>
 * This interface will be less cool but cool enough and implementing it will be shorter
 */
public class CliCommand<T> {



    @Switch("--help")
    public Boolean help = null;

    @Switch("--verbose")
    public Boolean verbose;


    protected String join(Collection<String> coll) {
        if (coll == null || coll.size() == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (String commandArg : coll) {
            sb.append(commandArg).append(" ");
        }
        return sb.toString().trim();
    }





    public List<Field> getAllOptions() {
        List<Field> result = new LinkedList<Field>();
        Field[] fields = this.getClass().getFields();
        for (Field f : fields) {
            if (f.isAnnotationPresent(Flag.class) || f.isAnnotationPresent(Switch.class) || f.isAnnotationPresent(Argument.class)) {
                result.add(f);
            }
        }
        return result;
    }

    public String getCommand(){
        return "";
    }

    private void addValue( Object value, List<String> args ){
        if ( Collection.class.isAssignableFrom(value.getClass())){
            Collection c = (Collection) value;
            for (Object o : c) {
                args.add(o.toString());
            }
        }else{
            args.add( value.toString() );
        }
    }

    public String[] getCommandLineArgs() {
        List<String> commandArgs = new LinkedList<String>();
        try {

            if (this.getClass().isAnnotationPresent(Command.class)){
                commandArgs.add( this.getClass().getAnnotation( Command.class ).value() );
            }else{
                commandArgs.add(getCommand()); // assuming it is overriden.
            }

            for (Field f : getAllOptions()) {
                Object value = f.get(this);
                if (value != null) {
                    if ( f.isAnnotationPresent(Flag.class)){
                        commandArgs.add(f.getAnnotation(Flag.class).value());
                        addValue( value, commandArgs );
                    }else if ( f.isAnnotationPresent(Switch.class) && Boolean.valueOf(String.valueOf(value)) ){
                        commandArgs.add( f.getAnnotation(Switch.class).value() );
                    }else if ( f.isAnnotationPresent(Argument.class)){
                        addValue(value, commandArgs);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("unable to apply command", e);
        }
        return commandArgs.toArray( new String[commandArgs.size()]);
    }


    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface Flag {
        String value();
    }
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface Argument {
    }
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    // like a flag but does not get an argument
    public static @interface Switch{
        String value();
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface Command {
        String value();
    }

    // allows multiple commands
    public static class CommandGroup extends CliCommand<CommandGroup>{
        List<CliCommand> commands = new LinkedList<CliCommand>();

        @Override
        public String[] getCommandLineArgs() {
            List<String> args = new LinkedList<String>();
            for (CliCommand command : commands) {
                CollectionUtils.addAll(args, command.getCommandLineArgs());
                args.add(";");
            }
            return args.toArray( new String[args.size() ]);
        }
    }

    public static class WildCommand extends CliCommand<WildCommand>{

        public List<String> args = new LinkedList<String>();

        @Override
        public String[] getCommandLineArgs() {
            return args.toArray( new String[args.size()]);
        }
    }

    @Command("install-application")
    public static class InstallApplication extends CliCommand<InstallApplication> {

        @Flag("-cloudConfiguration")
        public String cloudConfiguration;

        @Flag("-name")
        public String name;

        @Switch("-disableSelfHealing")
        public Boolean disableSelfHealing;

        @Flag("-overrides")
        public String overrides;

        @Flag("-authGroups")
        public String authGroups;

        @Flag("-timeout")
        public String timeout;

        @Flag("-debug-mode")
        public String debugMode;

        @Flag("-cloud-overrides")
        public String cloudOverrides;

        @Flag("-debug-events ")
        public String debugEvents;

        @Switch("-debug-all")
        public Boolean debugAll;

        @Argument
        public String applicationFile;

        public void setCloudConfiguration(String cloudConfiguration) {
            this.cloudConfiguration = cloudConfiguration;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setDisableSelfHealing(Boolean disableSelfHealing) {
            this.disableSelfHealing = disableSelfHealing;
        }

        public void setOverrides(String overrides) {
            this.overrides = overrides;
        }

        public void setAuthGroups(String authGroups) {
            this.authGroups = authGroups;
        }

        public void setTimeout(String timeout) {
            this.timeout = timeout;
        }

        public void setDebugMode(String debugMode) {
            this.debugMode = debugMode;
        }

        public void setCloudOverrides(String cloudOverrides) {
            this.cloudOverrides = cloudOverrides;
        }

        public void setDebugEvents(String debugEvents) {
            this.debugEvents = debugEvents;
        }

        public void setDebugAll(Boolean debugAll) {
            this.debugAll = debugAll;
        }

        public void setApplicationFile(String applicationFile) {
            this.applicationFile = applicationFile;
        }

    }


    @Command("install-service")
    public static class InstallService extends CliCommand {


        @Flag("-name") // main arguments
        public String serviceName;

        @Flag("-zone")
        public String zone = null;

        @Switch("-disableSelfHealing")
        public Boolean disableSelfHealing = false;

        @Flag("-debug-mode ")
        public String debugMode = null;

        @Flag("-cloud-overrides")
        public String cloudOverrides = null;

        @Flag("-cloudConfiguration")
        public String cloudConfiguration = null;

        @Flag("-timeout")
        public String timeout;

        @Flag("-debug-all")
        public String debugAll;


        @Flag("-overrides")
        public String overrides;

        @Flag("-authGroups")
        public String authGroups;

        @Flag("-debug-events")
        public String debugEvents;


        @Flag("-name")
        public String name;

        @Flag("-service-file-name")
        public String serviceFileName;

        @Argument
        public String recipe;
    }

    @Command("uninstall-application")
    public static class UninstallApplication extends CliCommand {
        @Flag("%s")
        public String applicationName;

        @Flag("-timeout ")
        public String timeout;

    }

    @Command("uninstall-service")
    public static class UninstallService extends CliCommand {


        @Flag("-timeout")
        public String timeout;

        @Argument
        public String serviceName;
    }


    public static class Bootstrap extends CliCommand<Bootstrap>{

        @Autowired
        CloudDetails cloudDetails;

        @Override
        public String getCommand() {
            return cloudDetails.isLocal() ? "bootstrap-localcloud" : "bootstrap-cloud";
        }

        public void setCloudDetails(CloudDetails cloudDetails) {
            this.cloudDetails = cloudDetails;
        }

        @Switch("-secured")
        public Boolean secured = false;

        @Flag("-security-file")
        public String securityFile = null;

        @Flag("-user")
        public String user = null;

        @Flag("-password")
        public String password = null;

        @Flag("-keystore")
        public String keystore = null;

        @Flag("-keystore-password")
        public String keystorePassword = null;

        public void setKeystorePassword(String keystorePassword) {
            this.keystorePassword = keystorePassword;
        }

        public void setKeystore(String keystore) {
            this.keystore = keystore;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public void setSecurityFile(String securityFile) {
            this.securityFile = securityFile;
        }

        public void setSecured(Boolean secured) {
            this.secured = secured;
        }

    }

    public static class Teardown extends CliCommand<Teardown>{

        CloudDetails cloudDetails;

        @Override
        public String getCommand() {
            return cloudDetails.isLocal() ? "bootstrap-localcloud" : "bootstrap-cloud";
        }

    }

    @Command("invoke")
    public static class Invoke extends CliCommand {

        @Flag("-instance-id")
        public String instanceId;

        @Flag("-beanname")
        public String beanName;

        @Argument()
        public String serviceName;

        @Argument()
        public String commandName;

        @Argument
        private List<String> params = new LinkedList<String>();

        public void addParam(String... p) {
            Collections.addAll(params, p);
        }

        public void addParams(Collection<String> params) {
            params.addAll(params);
        }

    }

    @Command("connect")
    public static class Connect extends CliCommand{

        @Flag("-user")
        public String user;

        @Flag("-ssl")
        public String ssl;

        @Flag("-password")
        public String password;

        @Argument
        public String url;


    }

}
