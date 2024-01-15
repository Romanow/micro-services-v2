package ru.romanow.inst.services.store.service

import jakarta.persistence.EntityNotFoundException
import org.springframework.http.HttpMethod.DELETE
import org.springframework.http.HttpMethod.GET
import org.springframework.http.HttpMethod.POST
import org.springframework.http.HttpStatus.CONFLICT
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import ru.romanow.inst.services.common.config.CircuitBreakerFactory
import ru.romanow.inst.services.common.config.Fallback
import ru.romanow.inst.services.common.properties.CircuitBreakerConfigurationProperties
import ru.romanow.inst.services.common.properties.ServerUrlProperties
import ru.romanow.inst.services.common.utils.buildEx
import ru.romanow.inst.services.order.model.CreateOrderResponse
import ru.romanow.inst.services.order.model.OrderInfoResponse
import ru.romanow.inst.services.order.model.OrdersInfoResponse
import ru.romanow.inst.services.store.exceptions.ItemNotAvailableException
import ru.romanow.inst.services.store.exceptions.OrderProcessException
import ru.romanow.inst.services.store.model.PurchaseRequest
import ru.romanow.inst.services.store.model.WarrantyRequest
import ru.romanow.inst.services.warranty.model.OrderWarrantyResponse
import java.util.Optional
import java.util.UUID

@Service
class OrderServiceImpl(
    private val fallback: Fallback,
    private val orderWebClient: WebClient,
    private val serverUrlProperties: ServerUrlProperties,
    private val circuitBreakerProperties: CircuitBreakerConfigurationProperties,
    private val factory: CircuitBreakerFactory
) : OrderService {

    override fun getOrderInfo(userId: String, orderUid: UUID): Optional<OrderInfoResponse> {
        return orderWebClient
            .get()
            .uri("/{userId}/{orderUid}", userId, orderUid)
            .retrieve()
            .onStatus({ it == NOT_FOUND }, { response -> buildEx(response) { EntityNotFoundException(it) } })
            .onStatus({ it.isError }, { response -> buildEx(response) { OrderProcessException(it) } })
            .bodyToMono(OrderInfoResponse::class.java)
            .transform {
                if (circuitBreakerProperties.enabled) {
                    factory.create("getOrderInfo").run(it) { throwable ->
                        fallback
                            .apply(GET, "${serverUrlProperties.orderUrl}/api/v1/orders/$userId/$orderUid", throwable)
                    }
                } else {
                    return@transform it
                }
            }
            .blockOptional()
    }

    override fun getOrderInfoByUser(userId: String): Optional<OrdersInfoResponse> {
        return orderWebClient
            .get()
            .uri("/{userId}", userId)
            .retrieve()
            .onStatus({ it.isError }, { response -> buildEx(response) { OrderProcessException(it) } })
            .bodyToMono(OrdersInfoResponse::class.java)
            .transform {
                if (circuitBreakerProperties.enabled) {
                    factory.create("getOrderInfoByUser").run(it) { throwable ->
                        fallback.apply(GET, "${serverUrlProperties.orderUrl}/api/v1/orders/$userId", throwable)
                    }
                } else {
                    return@transform it
                }
            }
            .blockOptional()
    }

    override fun makePurchase(userId: String, request: PurchaseRequest): Optional<CreateOrderResponse> {
        return orderWebClient
            .post()
            .uri("/{userId}", userId)
            .body(BodyInserters.fromValue(request))
            .retrieve()
            .onStatus({ it == CONFLICT }, { response -> buildEx(response) { ItemNotAvailableException(it) } })
            .onStatus({ it == UNPROCESSABLE_ENTITY }, { response -> buildEx(response) { OrderProcessException(it) } })
            .onStatus({ it.isError }, { response -> buildEx(response) { OrderProcessException(it) } })
            .bodyToMono(CreateOrderResponse::class.java)
            .transform {
                if (circuitBreakerProperties.enabled) {
                    factory.create("makePurchase").run(it) { throwable ->
                        fallback
                            .apply(POST, "${serverUrlProperties.orderUrl}/api/v1/orders/$userId", throwable, request)
                    }
                } else {
                    return@transform it
                }
            }
            .blockOptional()
    }

    override fun refundPurchase(orderUid: UUID) {
        orderWebClient
            .delete()
            .uri("/{orderUid}", orderUid)
            .retrieve()
            .onStatus({ it == NOT_FOUND }, { response -> buildEx(response) { EntityNotFoundException(it) } })
            .onStatus({ it.isError }, { response -> buildEx(response) { OrderProcessException(it) } })
            .toBodilessEntity()
            .transform {
                if (circuitBreakerProperties.enabled) {
                    factory.create("refundPurchase").run(it) { throwable ->
                        fallback.apply(DELETE, "${serverUrlProperties.orderUrl}/api/v1/orders/$orderUid", throwable)
                    }
                } else {
                    return@transform it
                }
            }
            .block()
    }

    override fun warrantyRequest(orderUid: UUID, request: WarrantyRequest): Optional<OrderWarrantyResponse> {
        return orderWebClient
            .post()
            .uri("/{orderUid}/warranty", orderUid)
            .body(BodyInserters.fromValue(request))
            .retrieve()
            .onStatus({ it == NOT_FOUND }, { response -> buildEx(response) { EntityNotFoundException(it) } })
            .onStatus({ it == UNPROCESSABLE_ENTITY }, { response -> buildEx(response) { OrderProcessException(it) } })
            .onStatus({ it.isError }, { response -> buildEx(response) { OrderProcessException(it) } })
            .bodyToMono(OrderWarrantyResponse::class.java)
            .transform {
                if (circuitBreakerProperties.enabled) {
                    factory.create("warrantyRequest").run(it) { throwable ->
                        fallback.apply(
                            POST,
                            "${serverUrlProperties.orderUrl}/api/v1/orders/$orderUid/warranty",
                            throwable,
                            request
                        )
                    }
                } else {
                    return@transform it
                }
            }
            .blockOptional()
    }
}
