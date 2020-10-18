package ru.romanow.inst.services.warehouse.service

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import ru.romanow.inst.services.common.utils.JsonSerializer.fromJson
import ru.romanow.inst.services.common.utils.RestClient
import ru.romanow.inst.services.warehouse.exceptions.WarrantyProcessException
import ru.romanow.inst.services.warranty.model.ErrorResponse
import ru.romanow.inst.services.warranty.model.ItemWarrantyRequest
import ru.romanow.inst.services.warranty.model.OrderWarrantyRequest
import ru.romanow.inst.services.warranty.model.OrderWarrantyResponse
import java.util.*
import javax.persistence.EntityNotFoundException

@Service
class WarrantyServiceImpl(
    @Value("\${warranty.service.url}")
    private val warrantyServiceUrl: String,
    private val warehouseService: WarehouseService,
    private val restClient: RestClient
) : WarrantyService {
    private val logger = LoggerFactory.getLogger(WarehouseServiceImpl::class.java)

    override fun warrantyRequest(orderItemUid: UUID, request: OrderWarrantyRequest): OrderWarrantyResponse {
        logger.info("Warranty request (reason: {}) on item '{}'", request.reason, orderItemUid)
        val availableCount = warehouseService.checkItemAvailableCount(orderItemUid)
        val warrantyRequest = ItemWarrantyRequest(
            reason = request.reason,
            availableCount = availableCount
        )

        logger.info("Request to WarrantyService to check warranty on item '{}'", orderItemUid)
        return requestToWarranty(orderItemUid, warrantyRequest)
            .orElseThrow { WarrantyProcessException("Can't process warranty request for OrderItem '$orderItemUid'") }
    }

    private fun requestToWarranty(orderItemUid: UUID, request: ItemWarrantyRequest): Optional<OrderWarrantyResponse> {
        val url = "$warrantyServiceUrl/api/v1/warranty/$orderItemUid/warranty"
        return restClient
            .post(url, request, OrderWarrantyResponse::class.java)
            .exceptionMapping(HttpStatus.NOT_FOUND) { EntityNotFoundException(extractErrorMessage(it)) }
            .commonExceptionMapping { WarrantyProcessException(extractErrorMessage(it)) }
            .execute()
    }

    private fun extractErrorMessage(errorResponse: String) =
        fromJson(errorResponse, ErrorResponse::class.java).message
}