package ru.romanow.inst.services.order.service

import org.springframework.http.HttpMethod.DELETE
import org.springframework.http.HttpMethod.POST
import org.springframework.http.HttpStatus.*
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import ru.romanow.inst.services.common.config.CircuitBreakerFactory
import ru.romanow.inst.services.common.config.Fallback
import ru.romanow.inst.services.common.properties.CircuitBreakerConfigurationProperties
import ru.romanow.inst.services.common.properties.ServerUrlProperties
import ru.romanow.inst.services.common.utils.buildEx
import ru.romanow.inst.services.order.exceptions.ItemNotAvailableException
import ru.romanow.inst.services.order.exceptions.WarehouseProcessException
import ru.romanow.inst.services.order.model.SizeChart
import ru.romanow.inst.services.warehouse.model.OrderItemRequest
import ru.romanow.inst.services.warehouse.model.OrderItemResponse
import ru.romanow.inst.services.warranty.model.OrderWarrantyRequest
import ru.romanow.inst.services.warranty.model.OrderWarrantyResponse
import java.util.*
import javax.persistence.EntityNotFoundException

@Service
class WarehouseServiceImpl(
    private val fallback: Fallback,
    private val warehouseWebClient: WebClient,
    private val serverUrlProperties: ServerUrlProperties,
    private val circuitBreakerProperties: CircuitBreakerConfigurationProperties,
    private val factory: CircuitBreakerFactory,
) : WarehouseService {

    override fun takeItem(orderUid: UUID, model: String, size: SizeChart): Optional<OrderItemResponse> {
        val request = OrderItemRequest(orderUid, model, size.name)
        return warehouseWebClient
            .post()
            .body(BodyInserters.fromValue(request))
            .retrieve()
            .onStatus({ it == NOT_FOUND }, { response -> buildEx(response) { EntityNotFoundException(it) } })
            .onStatus({ it == CONFLICT }, { response -> buildEx(response) { ItemNotAvailableException(it) } })
            .onStatus({ it.isError }, { response -> buildEx(response) { WarehouseProcessException(it) } })
            .bodyToMono(OrderItemResponse::class.java)
            .transform {
                if (circuitBreakerProperties.enabled) {
                    factory.create("takeItem").run(it) { throwable ->
                        fallback.apply(POST, "${serverUrlProperties.warehouseUrl}/api/v1/warehouse", throwable, request)
                    }
                } else {
                    return@transform it
                }
            }
            .blockOptional()
    }

    override fun returnItem(itemUid: UUID) {
        warehouseWebClient
            .delete()
            .uri("/{itemUid}", itemUid)
            .retrieve()
            .onStatus({ it.isError }, { response -> buildEx(response) { WarehouseProcessException(it) } })
            .toBodilessEntity()
            .transform {
                if (circuitBreakerProperties.enabled) {
                    factory.create("returnItem").run(it) { throwable ->
                        fallback
                            .apply(DELETE, "${serverUrlProperties.warehouseUrl}/api/v1/warehouse/$itemUid", throwable)
                    }
                } else {
                    return@transform it
                }
            }
            .block()
    }

    override fun useWarrantyItem(itemUid: UUID, request: OrderWarrantyRequest): Optional<OrderWarrantyResponse> {
        return warehouseWebClient
            .post()
            .uri("/{itemUid}/warranty", itemUid)
            .body(BodyInserters.fromValue(request))
            .retrieve()
            .onStatus({ it == NOT_FOUND }, { response -> buildEx(response) { EntityNotFoundException(it) } })
            .onStatus({ it == UNPROCESSABLE_ENTITY },
                { response -> buildEx(response) { WarehouseProcessException(it) } })
            .onStatus({ it.isError }, { response -> buildEx(response) { WarehouseProcessException(it) } })
            .bodyToMono(OrderWarrantyResponse::class.java)
            .transform {
                if (circuitBreakerProperties.enabled) {
                    factory.create("useWarrantyItem").run(it) { throwable ->
                        fallback.apply(POST, "${serverUrlProperties.warehouseUrl}/api/v1/warehouse/$itemUid/warranty",
                            throwable, request)
                    }
                } else {
                    return@transform it
                }
            }
            .blockOptional()
    }
}