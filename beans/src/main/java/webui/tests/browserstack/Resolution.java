package webui.tests.browserstack;

/**
 * Created with IntelliJ IDEA.
 * User: guym
 * Date: 7/31/14
 * Time: 6:21 PM
 */
public enum Resolution {
    R_1024_768("1024x768"),
    R_1280_960("1280x960"),
    R_1280_1024("1280x1024"),
    R_1600_1200("1600x1200"),
    R_1920_1080("1920x1080");

    public String value;

    Resolution (String value){
        this.value = value;
    }
}
