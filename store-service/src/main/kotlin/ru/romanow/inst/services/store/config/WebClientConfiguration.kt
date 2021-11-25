package ru.romanow.inst.services.store.config

import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.web.reactive.function.client.WebClient
import ru.romanow.inst.services.common.properties.ServerUrlProperties

@Configuration
class WebClientConfiguration {

    @Bean
    fun orderWebClient(properties: ServerUrlProperties): WebClient.Builder =
        WebClient.builder()
            .baseUrl("${properties.orderUrl}/api/v1/orders")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")

    @Bean
    @LoadBalanced
    fun warehouseWebClient(properties: ServerUrlProperties): WebClient.Builder =
        WebClient.builder()
            .baseUrl("${properties.warehouseUrl}/api/v1/warehouse")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")

    @Bean
    @LoadBalanced
    fun warrantyWebClient(properties: ServerUrlProperties): WebClient.Builder =
        WebClient.builder()
            .baseUrl("${properties.warrantyUrl}/api/v1/warranty")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
}