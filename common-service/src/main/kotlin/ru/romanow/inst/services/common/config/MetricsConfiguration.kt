package ru.romanow.inst.services.common.config

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import io.github.resilience4j.micrometer.tagged.TaggedCircuitBreakerMetrics
import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct

@Configuration
class MetricsConfiguration {

    @PostConstruct
    fun circuitBreakerMetrics() {
        val meterRegistry = SimpleMeterRegistry()
        val circuitBreakerRegistry = CircuitBreakerRegistry.ofDefaults()

        TaggedCircuitBreakerMetrics
            .ofCircuitBreakerRegistry(circuitBreakerRegistry)
            .bindTo(meterRegistry)
    }
}