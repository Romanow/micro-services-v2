package ru.romanow.inst.services.store.service

import ru.romanow.inst.services.store.model.PurchaseRequest
import ru.romanow.inst.services.store.model.UserOrderResponse
import ru.romanow.inst.services.store.model.UserOrdersResponse
import ru.romanow.inst.services.store.model.WarrantyRequest
import ru.romanow.inst.services.store.model.WarrantyResponse
import java.util.UUID

interface StoreService {
    fun findUserOrders(userId: String): UserOrdersResponse
    fun findUserOrder(userId: String, orderUid: UUID): UserOrderResponse
    fun makePurchase(userId: String, request: PurchaseRequest): UUID
    fun refundPurchase(userId: String, orderUid: UUID)
    fun warrantyRequest(userId: String, orderUid: UUID, request: WarrantyRequest): WarrantyResponse
}
