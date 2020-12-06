package ru.romanow.inst.services.store.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder.Resilience4JCircuitBreakerConfiguration
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus.*
import org.springframework.stereotype.Service
import ru.romanow.inst.services.common.config.Fallback
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
    private val restClient: RestClient,
    private val fallback: Fallback,
    private val factory: CircuitBreakerFactory<Resilience4JCircuitBreakerConfiguration, Resilience4JConfigBuilder>
) : OrderService {
    override fun getOrderInfo(userUid: UUID, orderUid: UUID): Optional<OrderInfoResponse> {
        val url = "$orderServiceUrl/api/v1/orders/$userUid/$orderUid"
        return factory
            .create("getOrderInfo")
            .run({
                restClient.get(url, OrderInfoResponse::class.java)
                    .exceptionMapping(NOT_FOUND) { EntityNotFoundException(extractErrorMessage(it)) }
                    .commonExceptionMapping { OrderProcessException(extractErrorMessage(it)) }
                    .execute()
            }, { throwable -> fallback.apply(HttpMethod.GET, url, throwable) })
    }

    override fun getOrderInfoByUser(userUid: UUID): Optional<OrdersInfoResponse> {
        val url = "$orderServiceUrl/api/v1/orders/$userUid"
        return factory
            .create("getOrderInfoByUser")
            .run({
                restClient.get(url, OrdersInfoResponse::class.java)
                    .commonExceptionMapping { OrderProcessException(extractErrorMessage(it)) }
                    .execute()
            }, { throwable -> fallback.apply(HttpMethod.GET, url, throwable) })
    }

    override fun makePurchase(userUid: UUID, request: PurchaseRequest): Optional<CreateOrderResponse> {
        val url = "$orderServiceUrl/api/v1/orders/$userUid"
        return factory
            .create("makePurchase")
            .run({
                restClient.post(url, request, CreateOrderResponse::class.java)
                    .exceptionMapping(CONFLICT) { ItemNotAvailableException(extractErrorMessage(it)) }
                    .exceptionMapping(UNPROCESSABLE_ENTITY) { OrderProcessException(extractErrorMessage(it)) }
                    .commonExceptionMapping { OrderProcessException(extractErrorMessage(it)) }
                    .execute()
            }, { throwable -> fallback.apply(HttpMethod.POST, url, throwable) })
    }

    override fun refundPurchase(orderUid: UUID) {
        val url = "$orderServiceUrl/api/v1/orders/$orderUid"
        factory
            .create("refundPurchase")
            .run({
                restClient.delete(url, Void::class.java)
                    .exceptionMapping(NOT_FOUND) { EntityNotFoundException(extractErrorMessage(it)) }
                    .commonExceptionMapping { OrderProcessException(extractErrorMessage(it)) }
                    .execute()
            }, { throwable -> fallback.apply(HttpMethod.DELETE, url, throwable) })

    }

    override fun warrantyRequest(orderUid: UUID, request: WarrantyRequest): Optional<OrderWarrantyResponse> {
        val url = "$orderServiceUrl/api/v1/orders/$orderUid/warranty"
        return factory
            .create("warrantyRequest")
            .run({
                restClient
                    .post(url, request, OrderWarrantyResponse::class.java)
                    .exceptionMapping(NOT_FOUND) { EntityNotFoundException(extractErrorMessage(it)) }
                    .exceptionMapping(UNPROCESSABLE_ENTITY) { EntityNotFoundException(extractErrorMessage(it)) }
                    .commonExceptionMapping { OrderProcessException(extractErrorMessage(it)) }
                    .execute()
            }, { throwable -> fallback.apply(HttpMethod.POST, url, throwable) })
    }

    private fun extractErrorMessage(errorResponse: String) =
        JsonSerializer.fromJson(errorResponse, ErrorResponse::class.java).message
}