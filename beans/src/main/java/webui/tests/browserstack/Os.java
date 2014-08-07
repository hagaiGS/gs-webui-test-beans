package webui.tests.browserstack;

/**
 * Created with IntelliJ IDEA.
 * User: guym
 * Date: 7/31/14
 * Time: 6:14 PM
 */
public enum Os {
    WIDNOWS_XP("Windows","XP"),
    WINDOWS_7("Windows","7"),
    WINDOWS_8("Windows","8"),
    WINDOWS_8_1("Windows","8.1"),


    OS_X_SNOW_LEOPARD("OS X","Snow Leopard"),
    OS_X_LION("OS X","Lion"),
    OS_X_MOUNTAIN_LION("OS X","Mountain Lion"),
    OS_X_MAVERICKS("OS X","Mavericks");


    String name;
    String version;

    Os(String name, String version){
        this.name = name;
        this.version = version;
    }
}
