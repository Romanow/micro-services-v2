package ru.romanow.inst.services.store.service

import ru.romanow.inst.services.store.model.*
import java.util.*

interface StoreService {
    fun findUserOrders(userId: UUID): UserOrdersResponse

    fun findUserOrder(userId: UUID, orderId: UUID): UserOrderResponse

    fun makePurchase(userId: UUID, request: PurchaseRequest): UUID

    fun refundPurchase(userId: UUID, orderId: UUID)

    fun warrantyRequest(userId: UUID, orderId: UUID, request: WarrantyRequest): WarrantyResponse
}