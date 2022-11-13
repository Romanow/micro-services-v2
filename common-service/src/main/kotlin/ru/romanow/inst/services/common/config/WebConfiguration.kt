package ru.romanow.inst.services.common.config

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.web.filter.AbstractRequestLoggingFilter
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import javax.servlet.http.HttpServletRequest


@EnableWebMvc
@Configuration
class WebConfiguration : WebMvcConfigurer {
    private val logger = LoggerFactory.getLogger(WebConfiguration::class.java)

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods(
                HttpMethod.GET.name,
                HttpMethod.POST.name,
                HttpMethod.PUT.name,
                HttpMethod.PATCH.name,
                HttpMethod.DELETE.name
            )
    }

    @Bean
    fun logFilter(): AbstractRequestLoggingFilter {
        val filter: AbstractRequestLoggingFilter = object : AbstractRequestLoggingFilter() {
            override fun beforeRequest(request: HttpServletRequest, message: String) {}
            override fun afterRequest(request: HttpServletRequest, message: String) {
                val contains = request.servletPath.contains("/api")
                if (!contains) {
                    return
                }
                logger.info(message)
            }
        }
        filter.setIncludeQueryString(true)
        filter.setIncludePayload(true)
        filter.setMaxPayloadLength(50000)
        filter.setIncludeHeaders(false)
        filter.setAfterMessagePrefix("REQUEST DATA: ")
        return filter
    }
}