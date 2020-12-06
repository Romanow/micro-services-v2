package ru.romanow.inst.services.common.config

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import io.github.resilience4j.timelimiter.TimeLimiterConfig
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder
import org.springframework.cloud.client.circuitbreaker.Customizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import java.time.Duration
import java.util.*

@Configuration
class CircuitBreakerConfiguration {
    private val logger = LoggerFactory.getLogger(CircuitBreakerConfiguration::class.java)

    @Value("\${circuit.breaker.default.timeout}")
    private var defaultTimeout: Long = 0

    @Bean
    @ConditionalOnMissingBean(CircuitBreakerConfigurationSupport::class)
    fun circuitBreakerConfigurationSupport(): CircuitBreakerConfigurationSupport {
        return object : CircuitBreakerConfigurationSupport {}
    }

    @Bean
    fun defaultCustomizer(
        circuitBreakerRegistry: CircuitBreakerRegistry,
        circuitBreakerConfigurationSupport: CircuitBreakerConfigurationSupport
    ): Customizer<Resilience4JCircuitBreakerFactory> {
        val timeLimiterConfig = TimeLimiterConfig
            .custom()
            .timeoutDuration(Duration.ofSeconds(defaultTimeout))
            .build()
        val circuitBreakerConfig = CircuitBreakerConfig
            .custom()
            .failureRateThreshold(10f)
            .slowCallRateThreshold(5f)
            .slowCallRateThreshold(2f)
            .ignoreExceptions(*circuitBreakerConfigurationSupport.ignoredExceptions())
            .build()
        return Customizer { factory: Resilience4JCircuitBreakerFactory ->
            factory.configureCircuitBreakerRegistry(circuitBreakerRegistry)
            factory.configureDefault { id: String ->
                Resilience4JConfigBuilder(id)
                    .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
                    .timeLimiterConfig(timeLimiterConfig)
                    .circuitBreakerConfig(circuitBreakerConfig)
                    .build()
            }
        }
    }

    @Bean
    fun fallback(circuitBreakerConfigurationSupport: CircuitBreakerConfigurationSupport): Fallback {
        return object : Fallback {
            override fun <T> apply(method: HttpMethod, url: String, throwable: Throwable, vararg params: Any): Optional<T> {
                logger.warn("Request to {} '{}' failed with exception: {}. (params: '{}')", method.name, url, throwable.message, params)
                if (throwable.javaClass in circuitBreakerConfigurationSupport.ignoredExceptions()) {
                    throw (throwable as RuntimeException)
                }
                return Optional.empty()
            }
        }
    }

}