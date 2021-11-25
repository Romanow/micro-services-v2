package ru.romanow.inst.services.warehouse.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.web.reactive.function.client.WebClient
import ru.romanow.inst.services.common.properties.ServerUrlProperties

@Configuration
class WebClientConfiguration {

    @Bean
    fun warrantyWebClient(properties: ServerUrlProperties): WebClient =
        WebClient.builder()
            .baseUrl("${properties.warrantyUrl}/api/v1/warranty")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
            .build()
}