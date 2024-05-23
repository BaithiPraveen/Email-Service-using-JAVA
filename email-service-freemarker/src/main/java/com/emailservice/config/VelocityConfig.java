package com.emailservice.config;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Properties;
@Configuration
public class VelocityConfig {

    @Bean
    @Primary
    public VelocityEngine velocityEngine() {
        Properties properties = new Properties();
        properties.setProperty("resource.loader", "class");
        properties.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.init(properties);

        return velocityEngine;
    }
}
