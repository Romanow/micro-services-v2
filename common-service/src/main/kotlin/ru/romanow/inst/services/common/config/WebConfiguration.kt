package ru.romanow.inst.services.common.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

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
}