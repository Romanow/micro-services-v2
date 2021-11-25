package ru.romanow.inst.services.common.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "circuit.breaker")
data class CircuitBreakerConfigurationProperties(
    val defaultTimeout: Long
)