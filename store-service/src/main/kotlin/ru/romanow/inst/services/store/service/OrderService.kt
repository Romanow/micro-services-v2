package ru.romanow.inst.services.store.service

import ru.romanow.inst.services.order.model.CreateOrderResponse
import ru.romanow.inst.services.order.model.OrderInfoResponse
import ru.romanow.inst.services.order.model.OrdersInfoResponse
import ru.romanow.inst.services.store.model.PurchaseRequest
import ru.romanow.inst.services.store.model.WarrantyRequest
import ru.romanow.inst.services.warranty.model.OrderWarrantyResponse
import java.util.Optional
import java.util.UUID

interface OrderService {
    fun getOrderInfo(userId: String, orderUid: UUID): Optional<OrderInfoResponse>
    fun getOrderInfoByUser(userId: String): Optional<OrdersInfoResponse>
    fun makePurchase(userId: String, request: PurchaseRequest): Optional<CreateOrderResponse>
    fun refundPurchase(orderUid: UUID)
    fun warrantyRequest(orderUid: UUID, request: WarrantyRequest): Optional<OrderWarrantyResponse>
}
