This module helps you set up your test environment

Use it wisely..

You can define certain actions you need to do for example:

 - checkout from SVN
 - copy files


Some actions are given with the module, and you can write your own as you need.

You define the set of actions you want from Spring.

This module is intended to run as a JAR as well,

so lets say you want to manually reproduce a test,

you can skip the setup stages by running the main method with the same
list of actions the test used.

Isn't that great?

You can also have a ready to use spring XML file with a bunch of actions
you use a lot, wrap each with spring profile and then simply write


```java -jar setup.jar -Dspring.profiles.active=installApp```



