# Common Components Library


Build the library and include this library into microservice by system depenency:


```
        <dependency>
          <groupId>com.nasdaq</groupId>
          <artifactId>lib</artifactId>
          <version>0.0.1-SNAPSHOT</version>
          <scope>system</scope>
          <systemPath>${basedir}/../lib/target/lei-lib.jar</systemPath>
        </dependency>
```



The spring beans of componenets (initially cors, loggin, etc..) should initialize automaticaly.

