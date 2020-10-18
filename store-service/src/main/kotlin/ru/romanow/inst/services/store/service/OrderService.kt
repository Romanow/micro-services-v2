package ru.romanow.inst.services.store.service

import ru.romanow.inst.services.order.model.CreateOrderResponse
import ru.romanow.inst.services.order.model.OrderInfoResponse
import ru.romanow.inst.services.order.model.OrdersInfoResponse
import ru.romanow.inst.services.store.model.PurchaseRequest
import ru.romanow.inst.services.store.model.WarrantyRequest
import ru.romanow.inst.services.warranty.model.OrderWarrantyResponse
import java.util.*

interface OrderService {
    fun getOrderInfo(userUid: UUID, orderUid: UUID): Optional<OrderInfoResponse>
    fun getOrderInfoByUser(userUid: UUID): Optional<OrdersInfoResponse>
    fun makePurchase(userUid: UUID, request: PurchaseRequest): Optional<CreateOrderResponse>
    fun refundPurchase(orderUid: UUID)
    fun warrantyRequest(orderUid: UUID, request: WarrantyRequest): Optional<OrderWarrantyResponse>
}
