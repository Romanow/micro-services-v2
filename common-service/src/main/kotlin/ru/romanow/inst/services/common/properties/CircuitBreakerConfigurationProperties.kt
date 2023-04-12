package ru.romanow.inst.services.common.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

@ConfigurationProperties(prefix = "circuit-breaker")
data class CircuitBreakerConfigurationProperties(
    val enabled: Boolean,
    val defaultTimeout: Duration,
)