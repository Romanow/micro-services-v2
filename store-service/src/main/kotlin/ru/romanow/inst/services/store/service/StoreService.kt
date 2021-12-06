package ru.romanow.inst.services.store.service

import ru.romanow.inst.services.store.model.*
import java.util.*

interface StoreService {
    fun findUserOrders(userId: String): UserOrdersResponse
    fun findUserOrder(userId: String, orderUid: UUID): UserOrderResponse
    fun makePurchase(userId: String, request: PurchaseRequest): UUID
    fun refundPurchase(userId: String, orderUid: UUID)
    fun warrantyRequest(userId: String, orderUid: UUID, request: WarrantyRequest): WarrantyResponse
}