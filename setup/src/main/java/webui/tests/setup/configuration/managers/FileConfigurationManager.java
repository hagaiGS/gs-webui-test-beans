package webui.tests.setup.configuration.managers;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import webui.tests.setup.configuration.Source;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

/**
 * User: guym
 * Date: 8/9/13
 * Time: 9:06 AM
 */
public class FileConfigurationManager implements ConfigurationManager{

    List<FileConfigurationHandler> handlers = new LinkedList<FileConfigurationHandler>();

    private static Logger logger = LoggerFactory.getLogger(FileConfigurationManager.class);


    @Override
    public <T> T instantiate(Class<T> clzz) {
        if (clzz.isAnnotationPresent(Source.File.class)) {

            String resourceLocation = name( clzz.getSimpleName(), clzz.getAnnotation(Source.File.class));

            for (FileConfigurationHandler handler : handlers) {

                String path = resourceLocation + handler.getSuffix();
                try {
                    ApplicationContext appContext = new FileSystemXmlApplicationContext();
                    Resource resource = appContext.getResource(path);
                    logger.info("looking if resource [{}] exists", resource);
                    if (resource.isReadable() && resource.getFile().exists()) {
                        T t = handler.instantiate(resource.getFile(), clzz);
                        if ( t != null ){
                            return t;
                        }
                    } else {
                        logger.info("skipping file [{}] which is not readable [{}] or does not exist [{}]", path, resource.isReadable(), resource.isReadable() && resource.getFile().exists());
                    }


                } catch (Exception e) {
                    logger.info("unable to load configuration file [{}]", path, e);
                    throw new RuntimeException(String.format("unable to load configuration file [%s]", path), e);
                }
            }
        }
        return null;
    }

    /**
     * This manager know to handle files
     *
     * JSON - suffix ".json" - can override the entire object or specific fields
     *
     * Spring Context - suffix "-context.xml" - can override specific fields inside the class. Bean name should match fieldName.
     *
     * @param data
     */
    @Override
    public void populate(Data data) {
        for (Field field : data.getFields()) {
            if (field.isAnnotationPresent(Source.File.class)) {

                String resourceLocation = name(field.getName(), field.getAnnotation(Source.File.class));

                for (FileConfigurationHandler handler : handlers) {

                    String path = resourceLocation + handler.getSuffix();
                    try {
                        UrlResource resource = new UrlResource(path);
                        if (resource.isReadable() && resource.getFile().exists()) {
                            Object t = handler.instantiate(resource.getFile(), field.getType());
                            if (t != null) {
                                data.set( field, t );
                            }
                        } else {
                            logger.info("skipping file [{}] which is not readable [{}] or does not exist [{}]", path, resource.isReadable(), resource.getFile().exists());
                        }


                    } catch (Exception e) {
                        logger.info("unable to load configuration file [{}]", path, e);
                        throw new RuntimeException(String.format("unable to load configuration file [%s]", path), e);
                    }
                }
            }
        }
    }

    private String name( String defaultName, Source.File fileAnn ){
        // default resource location
        String resourceLocation = "classpath:" + defaultName;

        // annotation override
        if ( fileAnn.value() != null && fileAnn.value().trim().length() > 0 ){
            resourceLocation = fileAnn.value();
        }
        return resourceLocation;
    }

    // goes through the fields and find possible values to override
    private void handleSpecificFields( Data data ){


    }

    // override the entire class
    private void handleClassPopulation( Data data ){

    }

    public static interface FileConfigurationHandler{

        public String getSuffix();

        public <T> T instantiate(File file, Class<T> clzz );

    }

    public static abstract class AbstractFileHandler implements FileConfigurationHandler{

        public String suffix;

        @Override
        public String getSuffix() {
            return suffix;
        }

        public void setSuffix(String suffix) {
            this.suffix = suffix;
        }
    }

    public static class JsonFileHandler extends AbstractFileHandler{
        public JsonFileHandler() {
            setSuffix(".json");
        }

        @Override
        public <T> T instantiate(File file, Class<T> clzz) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                return mapper.reader(clzz).readValue( file );
            } catch (IOException e) {
                throw new  RuntimeException(String.format("unable to read JSON [%s] for class [%s]", file, clzz ), e);
            }
        }
    }

    public static class SpringContextHandler extends AbstractFileHandler{
        public SpringContextHandler() {
            setSuffix("-context.xml");
        }

        @Override
        public <T> T instantiate(File file, Class<T> clzz) {
            ApplicationContext context = new FileSystemXmlApplicationContext("file:" + file.getAbsolutePath());
            if ( context.getBeanNamesForType( clzz ).length > 0 ){
                return context.getBean( clzz );
            }
            return null;
        }


    }

    public void setHandlers(List<FileConfigurationHandler> handlers) {
        this.handlers = handlers;
    }
}
