package ru.romanow.inst.services.warehouse.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.security.oauth2.server.resource.web.reactive.function.client.ServletBearerExchangeFilterFunction
import org.springframework.web.reactive.function.client.WebClient
import ru.romanow.inst.services.common.properties.ServerUrlProperties

@Configuration
class WebClientConfiguration {

    @Bean
    fun warrantyWebClient(builder: WebClient.Builder, properties: ServerUrlProperties): WebClient =
        builder
            .baseUrl("${properties.warrantyUrl}/api/v1/warranty")
            .filter(ServletBearerExchangeFilterFunction())
            .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
            .build()
}
