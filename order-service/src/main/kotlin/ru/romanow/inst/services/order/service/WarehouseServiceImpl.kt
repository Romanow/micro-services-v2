package ru.romanow.inst.services.order.service

import org.springframework.http.HttpStatus.*
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
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
    private val warehouseWebClient: WebClient
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
            .blockOptional()
    }

    override fun returnItem(itemUid: UUID) {
        warehouseWebClient
            .delete()
            .uri("/{itemUid}", itemUid)
            .retrieve()
            .onStatus({ it.isError }, { response -> buildEx(response) { WarehouseProcessException(it) } })
            .toBodilessEntity()
            .block()
    }

    override fun useWarrantyItem(itemUid: UUID, request: OrderWarrantyRequest): Optional<OrderWarrantyResponse> {
        return warehouseWebClient
            .post()
            .uri("/{itemUid}/warranty", itemUid)
            .retrieve()
            .onStatus({ it == NOT_FOUND }, { response -> buildEx(response) { EntityNotFoundException(it) } })
            .onStatus({ it == UNPROCESSABLE_ENTITY }, { response -> buildEx(response) { WarehouseProcessException(it) } })
            .onStatus({ it.isError }, { response -> buildEx(response) { WarehouseProcessException(it) } })
            .bodyToMono(OrderWarrantyResponse::class.java)
            .blockOptional()
    }
}