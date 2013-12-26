package webui.tests.components.conditions;

/**
 * Created with IntelliJ IDEA.
 * User: guym
 * Date: 10/8/13
 * Time: 8:11 PM
 */
public abstract class SizeCondition {
        int root;

        public static SizeCondition eq(int size){
            return new EQ().setRoot(size);
        }

        public static SizeCondition gt( int size ){
            return new GT().setRoot( size );
        }

        public static SizeCondition gte( int size ){
            return new GTE().setRoot( size );
        }

        public static SizeCondition lt( int size ){
            return new LT().setRoot( size );
        }

        public static SizeCondition lte ( int size ){
            return new LTE().setRoot( size );
        }

        public SizeCondition setRoot( int root ){
            this.root = root;
            return this;
        }

        public abstract boolean applies( int n );

        public static class EQ extends SizeCondition{
            @Override
            public boolean applies(int n) {
                return root == n;
            }
        }

        public static class GT extends SizeCondition{
            @Override
            public boolean applies(int n) {
                return n > root;
            }
        }

        public static class GTE extends SizeCondition{
            @Override
            public boolean applies(int n) {
                return n >= root;
            }
        }

        public static class LT extends SizeCondition{
            @Override
            public boolean applies(int n) {
                return n < root;
            }
        }

        public static class LTE extends SizeCondition{
            @Override
            public boolean applies(int n) {
                return n <= root;
            }
        }
}
