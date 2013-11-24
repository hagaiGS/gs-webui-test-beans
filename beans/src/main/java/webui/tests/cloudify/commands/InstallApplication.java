package webui.tests.cloudify.commands;

/**
 * User: eliranm
 * Date: 11/24/13
 * Time: 2:48 PM
 */
public class InstallApplication extends CloudifyCommand<InstallApplication> {

    public InstallApplication() {
        arg( "install-application" );
    }

    public InstallApplication name(String name) {
        return arg( name );
    }

    public InstallApplication authGroups(String authGroups) {
        return arg( "-authGroups", authGroups );
    }

    public static class Details extends CloudifyCommand.Details<InstallApplication> {

        private String name;
        private String authGroups;

        @Override
        public InstallApplication populate(InstallApplication installApplication) {
            return installApplication._if( authGroups ).authGroups( authGroups )._if( name ).name( name );
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setAuthGroups(String authGroups) {
            this.authGroups = authGroups;
        }
    }

}
