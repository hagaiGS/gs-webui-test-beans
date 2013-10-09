package webui.tests.setup.configuration.managers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

/**
 * User: guym
 * Date: 8/8/13
 * Time: 5:44 PM
 */
public interface ConfigurationManager {



    public static class Data{
        public Class<?> clzz;
        public Object obj;

        private static Logger logger = LoggerFactory.getLogger(ConfigurationManager.Data.class);

        public Data setClzz(Class<?> clzz) {
            this.clzz = clzz;
            return this;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "clzz=" + clzz +
                    '}';
        }


        public Data setObj(Object obj) {
            this.obj = obj;
            return this;
        }

        public Field[] getFields(){
            return clzz.getFields();
        }

        public void set( Field field, Object value) {
            try{
            field.set( obj, value );
            }catch(Exception e){
                logger.error("unable to set [{}] into [{}]#[{}] ", value, clzz, field, e);
                throw new RuntimeException(String.format("unable to set [%s] into [%s]#[%s]", value, clzz, field), e);
            }
        }
    }

    // return true/false if population occurred successfully.
    void populate( Data data );

    public <T> T instantiate( Class<T> clzz  );
}
