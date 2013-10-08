package webui.tests.utils;

import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: guym
 * Date: 8/6/13
 * Time: 4:45 PM
 */
public class Assert extends org.testng.Assert {

    public static void assertNotContains( String msg, Collection c, Object o ){
        if ( c.contains( o )){
            fail(msg);
        }
    }

    public static void assertNotEmpty( String msg, String value ){
        if ( value == null || value.trim().length() == 0){
            fail(msg);
        }
    }

    public static void assertContains( String msg, Collection c, Object o ){
        if ( !c.contains( o )){
            fail(msg);
        }

    }


}
