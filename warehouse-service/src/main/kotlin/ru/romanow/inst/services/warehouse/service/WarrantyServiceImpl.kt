package ru.romanow.inst.services.warehouse.service

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import ru.romanow.inst.services.common.utils.JsonSerializer
import ru.romanow.inst.services.common.utils.JsonSerializer.fromJson
import ru.romanow.inst.services.warehouse.exceptions.WarrantyProcessException
import ru.romanow.inst.services.warranty.model.ErrorResponse
import ru.romanow.inst.services.warranty.model.ItemWarrantyRequest
import ru.romanow.inst.services.warranty.model.OrderWarrantyRequest
import ru.romanow.inst.services.warranty.model.OrderWarrantyResponse
import java.lang.RuntimeException
import java.util.*
import java.util.Optional.ofNullable
import javax.persistence.EntityNotFoundException

@Service
class WarrantyServiceImpl(
    private val warehouseService: WarehouseService,
    private val restTemplate: RestTemplate,
    @Value("\${warranty.service.url}")
    private val warrantyServiceUrl: String
) : WarrantyService {
    private val logger = LoggerFactory.getLogger(WarehouseServiceImpl::class.java)

    override fun warrantyRequest(orderItemUid: UUID, request: OrderWarrantyRequest): OrderWarrantyResponse {
        logger.info("Warranty request (reason: {}) on item '{}'", request.reason, orderItemUid)
        val availableCount = warehouseService.checkItemAvailableCount(orderItemUid)
        val warrantyRequest = ItemWarrantyRequest(
            reason = request.reason,
            availableCount = availableCount
        )

        return requestToWarranty(orderItemUid, warrantyRequest)
            .orElseThrow { WarrantyProcessException("Can't process warranty request") }
    }

    private fun requestToWarranty(orderItemUid: UUID, request: ItemWarrantyRequest): Optional<OrderWarrantyResponse> {
        val url = "$warrantyServiceUrl/api/v1/warranty/$orderItemUid/warranty"
        try {
            logger.info("Request to WarrantyService to check warranty on item '{}'", orderItemUid)
            return ofNullable(restTemplate.postForObject(url, request, OrderWarrantyResponse::class.java))
        } catch (exception: HttpClientErrorException.NotFound) {
            val errorResponse = fromJson(exception.responseBodyAsString, ErrorResponse::class.java)
            throw EntityNotFoundException(errorResponse.message)
        } catch (exception: HttpServerErrorException) {
            val errorResponse = fromJson(exception.responseBodyAsString, ErrorResponse::class.java)
            throw WarrantyProcessException(errorResponse.message)
        }
    }
}