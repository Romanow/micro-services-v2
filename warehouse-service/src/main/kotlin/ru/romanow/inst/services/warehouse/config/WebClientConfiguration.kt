package ru.romanow.inst.services.warehouse.config

import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.web.reactive.function.client.WebClient
import ru.romanow.inst.services.common.properties.ServerUrlProperties

@Configuration
class WebClientConfiguration {

    @Bean
    @LoadBalanced
    fun warrantyWebClient(properties: ServerUrlProperties): WebClient.Builder =
        WebClient.builder()
            .baseUrl("${properties.warrantyUrl}/api/v1/warranty")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
}