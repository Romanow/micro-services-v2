package ru.romanow.inst.services.store.service

import org.springframework.http.HttpMethod.GET
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import ru.romanow.inst.services.common.config.CircuitBreakerFactory
import ru.romanow.inst.services.common.config.Fallback
import ru.romanow.inst.services.common.properties.CircuitBreakerConfigurationProperties
import ru.romanow.inst.services.common.properties.ServerUrlProperties
import ru.romanow.inst.services.common.utils.buildEx
import ru.romanow.inst.services.store.exceptions.WarrantyProcessException
import ru.romanow.inst.services.warranty.model.WarrantyInfoResponse
import java.util.*
import javax.persistence.EntityNotFoundException

@Service
class WarrantyServiceImpl(
    private val fallback: Fallback,
    private val warrantyWebClient: WebClient,
    private val serverUrlProperties: ServerUrlProperties,
    private val circuitBreakerProperties: CircuitBreakerConfigurationProperties,
    private val factory: CircuitBreakerFactory,
) : WarrantyService {

    override fun getItemWarrantyInfo(itemUid: UUID): Optional<WarrantyInfoResponse> {
        return warrantyWebClient
            .get()
            .uri("/{itemUid}", itemUid)
            .retrieve()
            .onStatus({ it == HttpStatus.NOT_FOUND }, { response -> buildEx(response) { EntityNotFoundException(it) } })
            .onStatus({ it.isError }, { response -> buildEx(response) { WarrantyProcessException(it) } })
            .bodyToMono(WarrantyInfoResponse::class.java)
            .transform {
                if (circuitBreakerProperties.enabled) {
                    factory.create("getItemWarrantyInfo").run(it) { throwable ->
                        fallback.apply(GET, "${serverUrlProperties.warrantyUrl}/api/v1/warranty/$itemUid", throwable)
                    }
                } else {
                    return@transform it
                }
            }
            .blockOptional()
    }
}