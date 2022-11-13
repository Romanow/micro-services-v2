package ru.romanow.inst.services.common

import io.opentracing.contrib.spring.tracer.configuration.TracerAutoConfiguration
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.AutoConfigureBefore
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.ComponentScan
import ru.romanow.inst.services.common.properties.ActuatorSecurityProperties
import ru.romanow.inst.services.common.properties.CircuitBreakerConfigurationProperties
import ru.romanow.inst.services.common.properties.ServerUrlProperties
import javax.annotation.PostConstruct

@ComponentScan("ru.romanow.inst.services.common")
@AutoConfigureBefore(TracerAutoConfiguration::class)
@EnableConfigurationProperties(value = [
    ActuatorSecurityProperties::class,
    CircuitBreakerConfigurationProperties::class,
    ServerUrlProperties::class
])
class CommonAutoConfiguration {
    private val logger = LoggerFactory.getLogger(CommonAutoConfiguration::class.java)

    @PostConstruct
    fun postConstruct() {
        logger.info("Common configuration module loaded")
    }
}