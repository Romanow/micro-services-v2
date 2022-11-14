package ru.romanow.inst.services.store.service

import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory
import org.springframework.http.HttpMethod.GET
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import ru.romanow.inst.services.common.config.Fallback
import ru.romanow.inst.services.common.properties.ServerUrlProperties
import ru.romanow.inst.services.common.utils.buildEx
import ru.romanow.inst.services.store.exceptions.WarrantyProcessException
import ru.romanow.inst.services.warranty.model.WarrantyInfoResponse
import java.util.*
import javax.persistence.EntityNotFoundException

@Service
class WarrantyServiceImpl(
    private val warrantyWebClient: WebClient,
    private val fallback: Fallback,
    private val properties: ServerUrlProperties,
    private val factory: ReactiveCircuitBreakerFactory<Resilience4JConfigBuilder.Resilience4JCircuitBreakerConfiguration, Resilience4JConfigBuilder>,
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
                factory.create("getItemWarrantyInfo")
                    .run(it) { throwable ->
                        fallback.apply(GET, "${properties.warrantyUrl}/api/v1/warranty/$itemUid", throwable)
                    }
            }
            .blockOptional()
    }
}