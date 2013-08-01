How to use this project


1. Add a maven dependency to your pom

        <groupId>webui.tests</groupId>
        <artifactId>beans</artifactId>
        <version>9.5.0-SNAPSHOT</version>


2. Add the following maven repository to your pom


<repository>
            <id>openspaces</id>
                <url>http://maven-repository.openspaces.org</url>
        </repository>

3. Add a submodule to your git project (or simply make the files available)

git submodule add https://github.com/guy-mograbi-at-gigaspaces/selenium-drivers.git src/test/resources/selenium-drivers

4. Start writing your test. You can use Spring to define your beans, but you can also write in code.
