package webui.tests.utils;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: guym
 * Date: 9/18/13
 * Time: 4:49 PM
 */
public class ListUtils {

    public static <T> List<T> removeDuplicates( Collection<T> items ) {
        LinkedList<T> list = new LinkedList<T>();
        HashSet<T> set = new HashSet<T>();

        for (T item : items) {
            if (! set.contains( item )){
                list.add( item );
                set.add(item);
            }
        }

        return list;
    }
}
