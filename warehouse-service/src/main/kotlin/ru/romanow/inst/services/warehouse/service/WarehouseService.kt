package ru.romanow.inst.services.warehouse.service

import ru.romanow.inst.services.warehouse.domain.OrderItem
import ru.romanow.inst.services.warehouse.model.ItemInfoResponse
import ru.romanow.inst.services.warehouse.model.OrderItemRequest
import ru.romanow.inst.services.warehouse.model.OrderItemResponse
import java.util.*

interface WarehouseService {
    fun getItemInfo(itemId: UUID): ItemInfoResponse
    fun getOrderItem(itemId: UUID): OrderItem
    fun takeItem(request: OrderItemRequest): OrderItemResponse
    fun returnItem(orderItemUid: UUID)
    fun checkItemAvailableCount(itemId: UUID): Int
}
