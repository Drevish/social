package com.drevish.social.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:view.properties")
@PropertySource("classpath:path.properties")
public class PropertiesConfig {
}
