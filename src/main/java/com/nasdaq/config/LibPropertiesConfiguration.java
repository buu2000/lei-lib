package com.nasdaq.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources({@PropertySource(value ="classpath:library.yaml", factory = YamlPropertySourceFactory.class)})
public class LibPropertiesConfiguration
{
}
