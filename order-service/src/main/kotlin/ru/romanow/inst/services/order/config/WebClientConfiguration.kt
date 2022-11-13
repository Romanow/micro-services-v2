package ru.romanow.inst.services.order.config

import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.security.oauth2.server.resource.web.reactive.function.client.ServletBearerExchangeFilterFunction
import org.springframework.web.reactive.function.client.WebClient
import ru.romanow.inst.services.common.properties.ServerUrlProperties

@Configuration
class WebClientConfiguration {

    @Bean
    @LoadBalanced
    fun warehouseWebClient(properties: ServerUrlProperties): WebClient.Builder =
        WebClient.builder()
            .baseUrl("${properties.warehouseUrl}/api/v1/warehouse")
            .filter(ServletBearerExchangeFilterFunction())
            .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")

    @Bean
    @LoadBalanced
    fun warrantyWebClient(properties: ServerUrlProperties): WebClient.Builder =
        WebClient.builder()
            .baseUrl("${properties.warrantyUrl}/api/v1/warranty")
            .filter(ServletBearerExchangeFilterFunction())
            .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
}