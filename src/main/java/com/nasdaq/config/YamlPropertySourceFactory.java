package com.nasdaq.config;

import java.io.IOException;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;
import org.springframework.stereotype.Component;

@EnableAutoConfiguration
@Component
public class YamlPropertySourceFactory implements PropertySourceFactory {

  @Override
  public PropertySource<?> createPropertySource(String name, EncodedResource encodedResource) throws IOException {
      YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
      factory.setResources(encodedResource.getResource());
      return new PropertiesPropertySource(encodedResource.getResource().getFilename(), factory.getObject());
  }
}