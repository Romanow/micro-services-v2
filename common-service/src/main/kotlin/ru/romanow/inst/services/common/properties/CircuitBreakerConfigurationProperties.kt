package ru.romanow.inst.services.common.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import java.time.Duration

@ConstructorBinding
@ConfigurationProperties(prefix = "circuit-breaker")
data class CircuitBreakerConfigurationProperties(
    val enabled: Boolean,
    val defaultTimeout: Duration,
)