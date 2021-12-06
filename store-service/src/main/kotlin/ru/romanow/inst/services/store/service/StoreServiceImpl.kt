package ru.romanow.inst.services.store.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ru.romanow.inst.services.store.exceptions.OrderProcessException
import ru.romanow.inst.services.store.exceptions.WarrantyProcessException
import ru.romanow.inst.services.store.model.*
import ru.romanow.inst.services.warranty.model.OrderWarrantyResponse
import java.util.*
import javax.persistence.EntityNotFoundException

@Service
class StoreServiceImpl(
    private val warehouseService: WarehouseService,
    private val orderService: OrderService,
    private val warrantyService: WarrantyService
) : StoreService {
    private val logger = LoggerFactory.getLogger(StoreServiceImpl::class.java)

    override fun findUserOrders(userId: String): UserOrdersResponse {
        val orders: MutableList<UserOrderResponse> = ArrayList()
        logger.info("Request to Order Service for user '{}' orders", userId)
        val userOrders = orderService.getOrderInfoByUser(userId)

        if (userOrders.isPresent) {
            logger.info("User '$userId' has ${userOrders.get().size} orders")

            // TODO переделать на batch-операции
            for (orderInfo in userOrders.get()) {
                val orderUid = orderInfo.orderUid
                val itemUid = orderInfo.itemUid

                logger.info("Processing user '$userId' order '$orderUid'")
                val order = UserOrderResponse(
                    orderUid = orderUid,
                    date = orderInfo.orderDate
                )

                logger.info("Request to Warehouse for item '$itemUid' info by order '$orderUid'")
                warehouseService.getItemInfo(itemUid)
                    .ifPresent {
                        order.model = it.model
                        order.size = SizeChart.valueOf(it.size)
                    }

                logger.info("Request to Warranty for item '$itemUid' info by order '$orderUid'")
                warrantyService.getItemWarrantyInfo(itemUid)
                    .ifPresent {
                        order.warrantyDate = it.warrantyDate
                        order.warrantyStatus = WarrantyStatus.valueOf(it.status)
                    }
                orders.add(order)
            }
        } else {
            logger.warn("User '$userId' has no orders")
        }

        return UserOrdersResponse(orders)
    }

    override fun findUserOrder(userId: String, orderUid: UUID): UserOrderResponse {
        logger.info("Request to Order Service for user '$userId' order '$orderUid'")
        val orderInfo = orderService.getOrderInfo(userId, orderUid)
            .orElseThrow { EntityNotFoundException("Order '$orderUid' not found for user '$userId'") }

        val orderResponse = UserOrderResponse(
            orderUid = orderUid,
            date = orderInfo.orderDate
        )

        logger.info("Processing user '$userId' order '$orderUid'")

        val itemUid = orderInfo.itemUid

        logger.info("Request to Warehouse for item '$itemUid' info by order '$orderUid'")
        warehouseService.getItemInfo(itemUid)
            .ifPresent {
                orderResponse.model = it.model
                orderResponse.size = SizeChart.valueOf(it.size)
            }

        logger.info("Request to Warranty for item '$itemUid' info by order '$orderUid'")
        warrantyService.getItemWarrantyInfo(itemUid)
            .ifPresent {
                orderResponse.warrantyDate = it.warrantyDate
                orderResponse.warrantyStatus = WarrantyStatus.valueOf(it.status)
            }

        return orderResponse
    }

    override fun makePurchase(userId: String, request: PurchaseRequest): UUID {
        logger.info("Request to Order Service for user '$userId' to process order")
        return orderService
            .makePurchase(userId, request)
            .map { it.orderUid }
            .orElseThrow { OrderProcessException("User '$userId' order not created") }
    }

    override fun refundPurchase(userId: String, orderUid: UUID) {
        logger.info("Request to Order Service for user '$userId' to cancel order '$orderUid'")
        orderService.refundPurchase(orderUid)
    }

    override fun warrantyRequest(userId: String, orderUid: UUID, request: WarrantyRequest): WarrantyResponse {
        logger.info("Request to Order Service for user '$userId' and order '$orderUid' to make warranty request (${request.reason}})")
        return orderService
            .warrantyRequest(orderUid, request)
            .map { buildWarrantyResponse(orderUid, it) }
            .orElseThrow { WarrantyProcessException("Warranty processed with exception") }
    }

    private fun buildWarrantyResponse(orderUid: UUID, response: OrderWarrantyResponse) =
        WarrantyResponse(
            orderUid = orderUid,
            warrantyDate = response.warrantyDate,
            decision = response.decision
        )
}
