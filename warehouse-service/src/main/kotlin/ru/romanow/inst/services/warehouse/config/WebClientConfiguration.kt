package ru.romanow.inst.services.warehouse.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfiguration {

    @Bean
    fun warrantyWebClient(@Value("\${warranty.service.url}") warrantyUrl: String): WebClient =
        WebClient.builder()
            .baseUrl("$warrantyUrl/api/v1/warranty")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
            .build()
}