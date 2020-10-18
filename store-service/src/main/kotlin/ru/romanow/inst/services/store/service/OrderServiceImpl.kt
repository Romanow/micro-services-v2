package ru.romanow.inst.services.store.service

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus.*
import org.springframework.stereotype.Service
import ru.romanow.inst.services.common.utils.JsonSerializer
import ru.romanow.inst.services.common.utils.RestClient
import ru.romanow.inst.services.order.model.CreateOrderResponse
import ru.romanow.inst.services.order.model.OrderInfoResponse
import ru.romanow.inst.services.order.model.OrdersInfoResponse
import ru.romanow.inst.services.store.exceptions.ItemNotAvailableException
import ru.romanow.inst.services.store.exceptions.OrderProcessException
import ru.romanow.inst.services.store.model.PurchaseRequest
import ru.romanow.inst.services.store.model.WarrantyRequest
import ru.romanow.inst.services.warranty.model.ErrorResponse
import ru.romanow.inst.services.warranty.model.OrderWarrantyResponse
import java.util.*
import javax.persistence.EntityNotFoundException

@Service
class OrderServiceImpl(
    @Value("\${order.service.url}")
    private val orderServiceUrl: String,
    private val restClient: RestClient
) : OrderService {
    private val logger = LoggerFactory.getLogger(OrderServiceImpl::class.java)

    override fun getOrderInfo(userUid: UUID, orderUid: UUID): Optional<OrderInfoResponse> {
        val url = "$orderServiceUrl/api/v1/orders/$userUid/$orderUid"
        return restClient.get(url, OrderInfoResponse::class.java)
            .exceptionMapping(NOT_FOUND) { EntityNotFoundException(extractErrorMessage(it)) }
            .commonExceptionMapping { OrderProcessException(extractErrorMessage(it)) }
            .execute()
    }

    override fun getOrderInfoByUser(userUid: UUID): Optional<OrdersInfoResponse> {
        val url = "$orderServiceUrl/api/v1/orders/$userUid"
        return restClient.get(url, OrdersInfoResponse::class.java)
            .commonExceptionMapping { OrderProcessException(extractErrorMessage(it)) }
            .execute()
    }

    override fun makePurchase(userUid: UUID, request: PurchaseRequest): Optional<CreateOrderResponse> {
        val url = "$orderServiceUrl/api/v1/orders/$userUid"
        return restClient.post(url, request, CreateOrderResponse::class.java)
            .exceptionMapping(CONFLICT) { ItemNotAvailableException(extractErrorMessage(it)) }
            .exceptionMapping(UNPROCESSABLE_ENTITY) { OrderProcessException(extractErrorMessage(it)) }
            .commonExceptionMapping { OrderProcessException(extractErrorMessage(it)) }
            .execute()
    }

    override fun refundPurchase(orderUid: UUID) {
        val url = "$orderServiceUrl/api/v1/orders/$orderUid"
        restClient.delete(url, Void::class.java)
            .exceptionMapping(NOT_FOUND) { EntityNotFoundException(extractErrorMessage(it)) }
            .commonExceptionMapping { OrderProcessException(extractErrorMessage(it)) }
            .execute()
    }

    override fun warrantyRequest(orderUid: UUID, request: WarrantyRequest): Optional<OrderWarrantyResponse> {
        val url = "$orderServiceUrl/api/v1/orders/$orderUid/warranty"
        return restClient
            .post(url, request, OrderWarrantyResponse::class.java)
            .exceptionMapping(NOT_FOUND) { EntityNotFoundException(extractErrorMessage(it)) }
            .exceptionMapping(UNPROCESSABLE_ENTITY) { EntityNotFoundException(extractErrorMessage(it)) }
            .commonExceptionMapping { OrderProcessException(extractErrorMessage(it)) }
            .execute()
    }

    private fun extractErrorMessage(errorResponse: String) =
        JsonSerializer.fromJson(errorResponse, ErrorResponse::class.java).message
}