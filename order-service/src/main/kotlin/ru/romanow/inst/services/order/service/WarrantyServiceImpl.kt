package ru.romanow.inst.services.order.service

import org.slf4j.LoggerFactory
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory
import org.springframework.http.HttpMethod
import org.springframework.http.HttpMethod.DELETE
import org.springframework.http.HttpMethod.POST
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import ru.romanow.inst.services.common.config.Fallback
import ru.romanow.inst.services.common.properties.ServerUrlProperties
import ru.romanow.inst.services.common.utils.buildEx
import ru.romanow.inst.services.order.exceptions.WarrantyProcessException
import java.util.*

@Service
class WarrantyServiceImpl(
    private val fallback: Fallback,
    private val warrantyWebClient: WebClient,
    private val properties: ServerUrlProperties,
    private val factory: ReactiveCircuitBreakerFactory<Resilience4JConfigBuilder.Resilience4JCircuitBreakerConfiguration, Resilience4JConfigBuilder>
) : WarrantyService {
    private val logger = LoggerFactory.getLogger(WarrantyServiceImpl::class.java)

    override fun startWarranty(itemUid: UUID) {
        warrantyWebClient
            .post()
            .uri("/{itemUid}", itemUid)
            .retrieve()
            .onStatus({ it.isError }, { response -> buildEx(response) { WarrantyProcessException(it) } })
            .toBodilessEntity()
            .transform {
                factory.create("startWarranty")
                    .run(it) { throwable ->
                        fallback.apply(POST, "${properties.warrantyUrl}/api/v1/warranty/$itemUid", throwable)
                    }
            }
            .block()

        logger.info("Successfully start warranty for item '$itemUid'")
    }

    override fun stopWarranty(itemUid: UUID) {
        warrantyWebClient
            .delete()
            .uri("/{itemUid}", itemUid)
            .retrieve()
            .onStatus({ it.isError }, { response -> buildEx(response) { WarrantyProcessException(it) } })
            .toBodilessEntity()
            .transform {
                factory.create("stopWarranty")
                    .run(it) { throwable ->
                        fallback.apply(DELETE, "${properties.warrantyUrl}/api/v1/warranty/$itemUid", throwable)
                    }
            }
            .block()

        logger.info("Successfully stopped warranty for item '$itemUid'")
    }
}