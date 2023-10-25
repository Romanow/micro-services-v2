package ru.romanow.inst.services.order.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ru.romanow.inst.services.order.exceptions.WarrantyProcessException
import ru.romanow.inst.services.order.model.CreateOrderRequest
import ru.romanow.inst.services.order.model.CreateOrderResponse
import ru.romanow.inst.services.warranty.model.OrderWarrantyRequest
import ru.romanow.inst.services.warranty.model.OrderWarrantyResponse
import java.util.*

@Service
class OrderManagementServiceImpl(
    private val orderService: OrderService,
    private val warehouseService: WarehouseService,
    private val warrantyService: WarrantyService
) : OrderManagementService {
    private val logger = LoggerFactory.getLogger(OrderManagementServiceImpl::class.java)

    override fun makeOrder(userId: String, request: CreateOrderRequest): CreateOrderResponse {
        val model = request.model
        val size = request.size
        logger.info("Create order (model: $model, sie: $size) for user '$userId'")

        val orderUid = UUID.randomUUID()
        logger.info("Request to WH to take item (model: $model, size: $size) for order '$orderUid'")
        val orderItemResponse = warehouseService.takeItem(orderUid, model, size)
            .orElseThrow { WarrantyProcessException("Can't take item from Warehouse for user '$userId'") }
        val orderItemUid = orderItemResponse.orderItemUid

        logger.info("Request to WarrantyService to start warranty on item '$orderItemUid' for user '$userId'")
        warrantyService.startWarranty(orderItemUid)
        orderService.createOrder(orderUid, userId, orderItemUid)

        return CreateOrderResponse(orderUid)
    }

    override fun refundOrder(orderUid: UUID) {
        logger.info("Return order '$orderUid}'")
        val order = orderService.getOrderByUid(orderUid)

        val itemUid = order.itemUid!!
        logger.info("Request to WH to return item '$itemUid' for order '$orderUid'")
        warehouseService.returnItem(itemUid)

        logger.info("Request to WarrantyService to stop warranty for item '$itemUid' in order '$orderUid'")
        warrantyService.stopWarranty(itemUid)

        orderService.cancelOrder(orderUid)
    }

    override fun useWarranty(orderUid: UUID, request: OrderWarrantyRequest): OrderWarrantyResponse {
        logger.info("Check warranty (reason: ${request.reason}) for order '$orderUid'")
        val order = orderService.getOrderByUid(orderUid)
        val itemUid = order.itemUid

        logger.info("Request to WarrantyService to use warranty for item '$itemUid' in order '$orderUid'")
        return warehouseService
            .useWarrantyItem(itemUid!!, request)
            .orElseThrow { WarrantyProcessException("Can't process warranty request for item '$itemUid' in order '$orderUid'") }
    }
}
