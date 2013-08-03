How to use this project


1. Add a maven dependency to your pom
        
        <dependency>
            <groupId>webui.tests</groupId>
            <artifactId>beans</artifactId>
            <version>9.5.0-SNAPSHOT</version>
        </dependency>


2. Add the following maven repository to your pom


        <repository>
            <id>openspaces</id>
            <url>http://maven-repository.openspaces.org</url>
        </repository>

3. Add a submodule to your git project (or simply make the files available)

        git submodule add https://github.com/guy-mograbi-at-gigaspaces/selenium-drivers.git src/test/resources/selenium-drivers

4. If you are not in a GIT project or you cannot add a submodule, you can simply  
   make sure the files are available under `src/test/resources/selenium-drivers`  
   This path is configurable. 

5. Start writing your test. You can use Spring to define your beans.  
   Here is an example without a Spring context

        package example;

        import org.junit.After;
        import org.junit.Test;
        import org.openqa.selenium.WebDriver;
        import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;
        import webui.tests.SeleniumDriverFactory;

        public class MyTest {

            private static Logger logger = LoggerFactory.getLogger(MyTest.class);
            SeleniumDriverFactory factory = new SeleniumDriverFactory();

            @After
            public void after(){
                logger.info("closing web driver");
               factory.quit();
            }

            @Test
            public void exampleTest(){
                logger.info("starting test");
                WebDriver driver = factory.init().getDriver();
                driver.get("http://www.google.com");

            }
        }


6. But this is not cool enough.. To get the full power of our system, lets use Spring.


        package example;

        import org.junit.Test;
        import org.junit.runner.RunWith;
        import org.openqa.selenium.WebDriver;
        import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.test.context.ContextConfiguration;
        import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

        @ContextConfiguration
        @RunWith(SpringJUnit4ClassRunner.class)
        public class MyTest {

            private static Logger logger = LoggerFactory.getLogger(MyTest.class);

            @Autowired
            public WebDriver webDriver;

            @Test
            public void exampleTest(){
                logger.info("starting test");
                webDriver.get("http://www.google.com");
            }
        }

  Which is shorter, and nicer, but still - not REALLY COOL.. So lets get even cooler.
  Selenium works best when using the [Page model](https://code.google.com/p/selenium/wiki/PageObjects).

7. This example uses a page.

