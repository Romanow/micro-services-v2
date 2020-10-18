package ru.romanow.inst.services.warranty.service

import ru.romanow.inst.services.warranty.domain.Warranty
import ru.romanow.inst.services.warranty.model.ItemWarrantyRequest
import ru.romanow.inst.services.warranty.model.OrderWarrantyResponse
import ru.romanow.inst.services.warranty.model.WarrantyInfoResponse
import java.util.*

interface WarrantyService {
    fun getWarrantyByItemUid(itemUid: UUID): Warranty
    fun getWarrantyInfo(itemUid: UUID): WarrantyInfoResponse
    fun warrantyRequest(itemUid: UUID, request: ItemWarrantyRequest): OrderWarrantyResponse
    fun startWarranty(itemUid: UUID)
    fun stopWarranty(itemUid: UUID)
}
