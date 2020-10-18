package ru.romanow.inst.services.order.service

import ru.romanow.inst.services.order.domain.Order
import ru.romanow.inst.services.order.model.OrderInfoResponse
import ru.romanow.inst.services.order.model.OrdersInfoResponse
import java.util.*

interface OrderService {
    fun getOrderByUid(orderUid: UUID): Order
    fun getUserOrder(userUid: UUID, orderUid: UUID): OrderInfoResponse
    fun getUserOrders(userUid: UUID): OrdersInfoResponse
    fun createOrder(orderUid: UUID, userUid: UUID, itemUid: UUID)
    fun cancelOrder(orderUid: UUID)
    fun checkOrder(orderUid: UUID): Boolean
}
