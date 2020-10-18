package ru.romanow.inst.services.order.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import ru.romanow.inst.services.common.utils.JsonSerializer
import ru.romanow.inst.services.common.utils.RestClient
import ru.romanow.inst.services.order.exceptions.ItemNotAvailableException
import ru.romanow.inst.services.order.exceptions.WarehouseProcessException
import ru.romanow.inst.services.order.exceptions.WarrantyProcessException
import ru.romanow.inst.services.order.model.SizeChart
import ru.romanow.inst.services.warehouse.model.OrderItemRequest
import ru.romanow.inst.services.warehouse.model.OrderItemResponse
import ru.romanow.inst.services.warranty.model.ErrorResponse
import ru.romanow.inst.services.warranty.model.OrderWarrantyRequest
import ru.romanow.inst.services.warranty.model.OrderWarrantyResponse
import java.util.*
import javax.persistence.EntityNotFoundException

@Service
class WarehouseServiceImpl(
    @Value("\${warehouse.service.url}")
    private val warehouseServiceUrl: String,
    private val restClient: RestClient
) : WarehouseService {

    override fun takeItem(orderUid: UUID, model: String, size: SizeChart): Optional<OrderItemResponse> {
        val url = "$warehouseServiceUrl/api/v1/warehouse"
        val request = OrderItemRequest(orderUid, model, size.name)
        return restClient.post(url, request, OrderItemResponse::class.java)
            .exceptionMapping(HttpStatus.NOT_FOUND) { EntityNotFoundException(extractErrorMessage(it)) }
            .exceptionMapping(HttpStatus.CONFLICT) { ItemNotAvailableException(extractErrorMessage(it)) }
            .commonExceptionMapping { WarehouseProcessException(extractErrorMessage(it)) }
            .execute()
    }

    override fun returnItem(itemUid: UUID) {
        val url = "$warehouseServiceUrl/api/v1/warehouse/$itemUid"
        restClient.delete(url, Void::class.java)
            .commonExceptionMapping { WarrantyProcessException(extractErrorMessage(it)) }
            .execute()
    }

    override fun useWarrantyItem(itemUid: UUID, request: OrderWarrantyRequest): Optional<OrderWarrantyResponse> {
        val url = "$warehouseServiceUrl/api/v1/warehouse/$itemUid/warranty"
        return restClient.post(url, request, OrderWarrantyResponse::class.java)
            .exceptionMapping(HttpStatus.NOT_FOUND) { EntityNotFoundException(extractErrorMessage(it)) }
            .exceptionMapping(HttpStatus.UNPROCESSABLE_ENTITY) { WarehouseProcessException(extractErrorMessage(it)) }
            .commonExceptionMapping { WarehouseProcessException(extractErrorMessage(it)) }
            .execute()
    }

    private fun extractErrorMessage(errorResponse: String) =
        JsonSerializer.fromJson(errorResponse, ErrorResponse::class.java).message
}