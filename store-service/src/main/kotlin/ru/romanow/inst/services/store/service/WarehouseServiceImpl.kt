package ru.romanow.inst.services.store.service

import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import ru.romanow.inst.services.common.config.Fallback
import ru.romanow.inst.services.common.properties.ServerUrlProperties
import ru.romanow.inst.services.common.utils.buildEx
import ru.romanow.inst.services.store.exceptions.WarehouseProcessException
import ru.romanow.inst.services.warehouse.model.ItemInfoResponse
import java.util.*
import javax.persistence.EntityNotFoundException

@Service
class WarehouseServiceImpl(
    warehouseWebClient: WebClient.Builder,
    private val fallback: Fallback,
    private val properties: ServerUrlProperties,
    private val factory: ReactiveCircuitBreakerFactory<Resilience4JConfigBuilder.Resilience4JCircuitBreakerConfiguration, Resilience4JConfigBuilder>
) : WarehouseService {
    private val webClient: WebClient = warehouseWebClient.build()

    override fun getItemInfo(itemUid: UUID): Optional<ItemInfoResponse> {
        return webClient
            .get()
            .uri("/{itemUid}", itemUid)
            .retrieve()
            .onStatus({ it == NOT_FOUND }, { response -> buildEx(response) { EntityNotFoundException(it) } })
            .onStatus({ it.isError }, { response -> buildEx(response) { WarehouseProcessException(it) } })
            .bodyToMono(ItemInfoResponse::class.java)
            .transform {
                factory.create("getItemInfo")
                    .run(it) { throwable ->
                        fallback.apply(HttpMethod.GET, "${properties.warehouseUrl}/api/v1/warehouse/$itemUid", throwable)
                    }
            }
            .blockOptional()
    }
}