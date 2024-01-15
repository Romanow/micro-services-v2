package ru.romanow.inst.services.warehouse.service

import ru.romanow.inst.services.warranty.model.OrderWarrantyRequest
import ru.romanow.inst.services.warranty.model.OrderWarrantyResponse
import java.util.UUID

interface WarrantyService {
    fun warrantyRequest(orderItemUid: UUID, request: OrderWarrantyRequest): OrderWarrantyResponse
}
