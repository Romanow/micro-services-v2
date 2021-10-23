package ru.romanow.inst.services.order.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfiguration {

    @Bean
    fun warehouseWebClient(@Value("\${warehouse.service.url}") warehouseUrl: String): WebClient =
        WebClient.builder()
            .baseUrl("$warehouseUrl/api/v1/warehouse")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
            .build()

    @Bean
    fun warrantyWebClient(@Value("\${warranty.service.url}") warrantyUrl: String): WebClient =
        WebClient.builder()
            .baseUrl("$warrantyUrl/api/v1/warranty")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
            .build()
}