# Common Components Library


Build the library and include this library into microservice by system depenency:


```
        <dependency>
          <groupId>com.nasdaq</groupId>
          <artifactId>lib</artifactId>
          <version>0.0.1-SNAPSHOT</version>
          <scope>system</scope>
          <systemPath>${basedir}/../lib/target/lib-0.0.1-SNAPSHOT.jar</systemPath>
        </dependency>
```



The spring beans of componenets (currently cors) should initialize automaticaly.

