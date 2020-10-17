package ru.romanow.inst.services.common;

import org.slf4j.Logger;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.romanow.inst.services.common.properties.ActuatorSecurityProperties;

import javax.annotation.PostConstruct;

import static org.slf4j.LoggerFactory.getLogger;

@Configuration
@ComponentScan("ru.romanow.inst.services.common")
@EnableConfigurationProperties(ActuatorSecurityProperties.class)
public class CommonAutoConfiguration {
    private static final Logger logger = getLogger(CommonAutoConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        logger.info("Common configuration module loaded");
    }
}
