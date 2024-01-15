package ru.romanow.inst.services.store.service

import ru.romanow.inst.services.warranty.model.WarrantyInfoResponse
import java.util.Optional
import java.util.UUID

interface WarrantyService {
    fun getItemWarrantyInfo(itemUid: UUID): Optional<WarrantyInfoResponse>
}
