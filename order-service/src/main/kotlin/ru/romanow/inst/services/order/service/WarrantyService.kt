package ru.romanow.inst.services.order.service

import java.util.*

interface WarrantyService {
    fun startWarranty(itemUid: UUID)
    fun stopWarranty(itemUid: UUID)
}