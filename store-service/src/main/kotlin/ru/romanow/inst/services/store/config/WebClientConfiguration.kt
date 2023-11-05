package ru.romanow.inst.services.store.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.security.oauth2.server.resource.web.reactive.function.client.ServletBearerExchangeFilterFunction
import org.springframework.web.reactive.function.client.WebClient
import ru.romanow.inst.services.common.properties.ServerUrlProperties


@Configuration
class WebClientConfiguration {

    @Bean
    fun orderWebClient(builder: WebClient.Builder, properties: ServerUrlProperties): WebClient =
        builder
            .baseUrl("${properties.orderUrl}/api/v1/orders")
            .filter(ServletBearerExchangeFilterFunction())
            .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
            .build()

    @Bean
    fun warehouseWebClient(builder: WebClient.Builder, properties: ServerUrlProperties): WebClient =
        builder
            .baseUrl("${properties.warehouseUrl}/api/v1/warehouse")
            .filter(ServletBearerExchangeFilterFunction())
            .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
            .build()

    @Bean
    fun warrantyWebClient(builder: WebClient.Builder, properties: ServerUrlProperties): WebClient =
        builder
            .baseUrl("${properties.warrantyUrl}/api/v1/warranty")
            .filter(ServletBearerExchangeFilterFunction())
            .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
            .build()
}
