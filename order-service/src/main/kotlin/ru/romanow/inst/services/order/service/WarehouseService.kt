package ru.romanow.inst.services.order.service

import ru.romanow.inst.services.order.model.SizeChart
import ru.romanow.inst.services.warehouse.model.OrderItemResponse
import ru.romanow.inst.services.warranty.model.OrderWarrantyRequest
import ru.romanow.inst.services.warranty.model.OrderWarrantyResponse
import java.util.*

interface WarehouseService {
    fun takeItem(orderUid: UUID, model: String, size: SizeChart): Optional<OrderItemResponse>
    fun returnItem(itemUid: UUID)
    fun useWarrantyItem(itemUid: UUID, request: OrderWarrantyRequest): Optional<OrderWarrantyResponse>
}