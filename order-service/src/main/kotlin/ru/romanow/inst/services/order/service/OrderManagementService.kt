package ru.romanow.inst.services.order.service

import ru.romanow.inst.services.order.model.CreateOrderRequest
import ru.romanow.inst.services.order.model.CreateOrderResponse
import ru.romanow.inst.services.warranty.model.OrderWarrantyRequest
import ru.romanow.inst.services.warranty.model.OrderWarrantyResponse
import java.util.UUID

interface OrderManagementService {
    fun makeOrder(userId: String, request: CreateOrderRequest): CreateOrderResponse
    fun refundOrder(orderUid: UUID)
    fun useWarranty(orderUid: UUID, request: OrderWarrantyRequest): OrderWarrantyResponse
}
