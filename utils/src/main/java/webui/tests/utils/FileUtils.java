package webui.tests.utils;

import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 * User: guym
 * Date: 11/27/13
 * Time: 6:33 PM
 */
public class FileUtils extends org.apache.commons.io.FileUtils{


    public static int chmod(String filename, int mode) {
        try {
            Class<?> fspClass = Class.forName("java.util.prefs.FileSystemPreferences");
            Method chmodMethod = fspClass.getDeclaredMethod("chmod", String.class, Integer.TYPE);
            chmodMethod.setAccessible(true);
            return (Integer) chmodMethod.invoke(null, filename, mode);
        } catch(Exception e){
            throw new RuntimeException(String.format("unable to chmod [%s] [%s]", filename, mode),e);
        }
    }

}
