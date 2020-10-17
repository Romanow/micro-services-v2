package ru.romanow.inst.services.warranty.service

import ru.romanow.inst.services.warranty.domain.Warranty
import ru.romanow.inst.services.warranty.model.ItemWarrantyRequest
import ru.romanow.inst.services.warranty.model.OrderWarrantyResponse
import ru.romanow.inst.services.warranty.model.WarrantyInfoResponse
import java.util.*

interface WarrantyService {
    fun getWarrantyByItemId(itemId: UUID): Warranty
    fun getWarrantyInfo(itemId: UUID): WarrantyInfoResponse
    fun warrantyRequest(itemId: UUID, request: ItemWarrantyRequest): OrderWarrantyResponse
    fun startWarranty(itemId: UUID)
    fun stopWarranty(itemId: UUID)
}
