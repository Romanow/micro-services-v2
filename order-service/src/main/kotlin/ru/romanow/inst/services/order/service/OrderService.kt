package ru.romanow.inst.services.order.service

import ru.romanow.inst.services.order.domain.Order
import ru.romanow.inst.services.order.model.OrderInfoResponse
import ru.romanow.inst.services.order.model.OrdersInfoResponse
import java.util.UUID

interface OrderService {
    fun getOrderByUid(orderUid: UUID): Order
    fun getUserOrder(userId: String, orderUid: UUID): OrderInfoResponse
    fun getUserOrders(userId: String): OrdersInfoResponse
    fun createOrder(orderUid: UUID, userId: String, itemUid: UUID)
    fun cancelOrder(orderUid: UUID)
    fun checkOrder(orderUid: UUID): Boolean
}
