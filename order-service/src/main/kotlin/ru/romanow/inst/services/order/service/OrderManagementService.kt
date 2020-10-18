package ru.romanow.inst.services.order.service

import ru.romanow.inst.services.order.model.CreateOrderRequest
import ru.romanow.inst.services.warranty.model.OrderWarrantyRequest
import ru.romanow.inst.services.warranty.model.OrderWarrantyResponse
import java.util.*

interface OrderManagementService {
    fun makeOrder(userUid: UUID, request: CreateOrderRequest): UUID
    fun refundOrder(orderUid: UUID)
    fun useWarranty(orderUid: UUID, request: OrderWarrantyRequest): OrderWarrantyResponse
}