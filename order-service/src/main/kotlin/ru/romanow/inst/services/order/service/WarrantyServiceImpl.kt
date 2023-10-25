package ru.romanow.inst.services.order.service

import org.slf4j.LoggerFactory
import org.springframework.http.HttpMethod.DELETE
import org.springframework.http.HttpMethod.POST
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import ru.romanow.inst.services.common.config.CircuitBreakerFactory
import ru.romanow.inst.services.common.config.Fallback
import ru.romanow.inst.services.common.properties.CircuitBreakerConfigurationProperties
import ru.romanow.inst.services.common.properties.ServerUrlProperties
import ru.romanow.inst.services.common.utils.buildEx
import ru.romanow.inst.services.order.exceptions.WarrantyProcessException
import java.util.*

@Service
class WarrantyServiceImpl(
    private val fallback: Fallback,
    private val warrantyWebClient: WebClient,
    private val serverUrlProperties: ServerUrlProperties,
    private val circuitBreakerProperties: CircuitBreakerConfigurationProperties,
    private val factory: CircuitBreakerFactory,
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
                if (circuitBreakerProperties.enabled) {
                    factory.create("startWarranty").run(it) { throwable ->
                        fallback.apply(POST, "${serverUrlProperties.warrantyUrl}/api/v1/warranty/$itemUid", throwable)
                    }
                } else {
                    return@transform it
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
                if (circuitBreakerProperties.enabled) {
                    factory.create("stopWarranty").run(it) { throwable ->
                        fallback.apply(DELETE, "${serverUrlProperties.warrantyUrl}/api/v1/warranty/$itemUid", throwable)
                    }
                } else {
                    return@transform it
                }
            }
            .block()

        logger.info("Successfully stopped warranty for item '$itemUid'")
    }
}
