package ru.romanow.inst.services.store.service

import jakarta.persistence.EntityNotFoundException
import org.springframework.http.HttpMethod.GET
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import ru.romanow.inst.services.common.config.CircuitBreakerFactory
import ru.romanow.inst.services.common.config.Fallback
import ru.romanow.inst.services.common.properties.CircuitBreakerConfigurationProperties
import ru.romanow.inst.services.common.properties.ServerUrlProperties
import ru.romanow.inst.services.common.utils.buildEx
import ru.romanow.inst.services.store.exceptions.WarehouseProcessException
import ru.romanow.inst.services.warehouse.model.ItemInfoResponse
import java.util.*

@Service
class WarehouseServiceImpl(
    private val fallback: Fallback,
    private val warehouseWebClient: WebClient,
    private val serverUrlProperties: ServerUrlProperties,
    private val circuitBreakerProperties: CircuitBreakerConfigurationProperties,
    private val factory: CircuitBreakerFactory,
) : WarehouseService {

    override fun getItemInfo(itemUid: UUID): Optional<ItemInfoResponse> {
        return warehouseWebClient
            .get()
            .uri("/{itemUid}", itemUid)
            .retrieve()
            .onStatus({ it == NOT_FOUND }, { response -> buildEx(response) { EntityNotFoundException(it) } })
            .onStatus({ it.isError }, { response -> buildEx(response) { WarehouseProcessException(it) } })
            .bodyToMono(ItemInfoResponse::class.java)
            .transform {
                if (circuitBreakerProperties.enabled) {
                    factory.create("getItemInfo").run(it) { throwable ->
                        fallback.apply(GET, "${serverUrlProperties.warehouseUrl}/api/v1/warehouse/$itemUid", throwable)
                    }
                } else {
                    return@transform it
                }
            }
            .blockOptional()
    }
}