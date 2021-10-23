package ru.romanow.inst.services.common.config

import io.opentracing.Tracer
import io.opentracing.noop.NoopTracerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.web.client.RestTemplate
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import ru.romanow.inst.services.common.utils.RestClient


@EnableWebMvc
@Configuration
class WebConfiguration : WebMvcConfigurer {

    @Value("\${springdoc.api-docs.path}")
    private lateinit var openApiPath: String

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping(openApiPath).allowedMethods("GET")
    }

    @Bean
    fun restTemplate() = RestTemplate()

    @Bean
    fun restClient() = RestClient(restTemplate())

    @Bean
    @Primary
    fun jaegerTracer(): Tracer {
        return NoopTracerFactory.create()
    }
}