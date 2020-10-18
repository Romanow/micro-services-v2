package ru.romanow.inst.services.common

import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import ru.romanow.inst.services.common.properties.ActuatorSecurityProperties
import javax.annotation.PostConstruct

@Configuration
@ComponentScan("ru.romanow.inst.services.common")
@EnableConfigurationProperties(ActuatorSecurityProperties::class)
class CommonAutoConfiguration {
    private val logger = LoggerFactory.getLogger(CommonAutoConfiguration::class.java)

    @PostConstruct
    fun postConstruct() {
        logger.info("Common configuration module loaded")
    }
}