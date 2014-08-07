package webui.tests.browserstack;

/**
 * Created with IntelliJ IDEA.
 * User: guym
 * Date: 7/31/14
 * Time: 6:18 PM
 */
public enum Browser {

    IE_6("IE","6.0"),
    IE_7("IE","7.0"),
    IE_8("IE","8.0"),
    IE_9("IE","9.0"),
    IE_10("IE","10.0"),
    IE_11("IE","11.0"),


    FF_24("Firefox","24.0"),
    FF_25("Firefox","25.0"),
    FF_26("Firefox","26.0"),
    FF_27("Firefox","27.0"),
    FF_28("Firefox","28.0"),
    FF_29("Firefox","29.0"),
    FF_30("Firefox","30.0"),

    SAFARI_5_1("Safari","5.1"),
    SAFARI_6_0("Safari","6.0"),
    SAFARI_6_1("Safari","6.1"),
    SAFARI_7_0("Safari","7.0"),

    CHROME_30("Chrome","30.0"),
    CHROME_31("Chrome","31.0"),
    CHROME_32("Chrome","32.0"),
    CHROME_33("Chrome","33.0"),
    CHROME_34("Chrome","34.0"),
    CHROME_35("Chrome","35.0"),

    OPERA_12_15("Chrome","12.15"),
    OPERA_12_16("Chrome","12.16");


    public String name;
    public String version;
    Browser(String name, String version){
        this.name = name;
        this.version = version;
    }


}
