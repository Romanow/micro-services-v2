package ru.romanow.inst.services.store.service

import ru.romanow.inst.services.warehouse.model.ItemInfoResponse
import java.util.Optional
import java.util.UUID

interface WarehouseService {
    fun getItemInfo(itemUid: UUID): Optional<ItemInfoResponse>
}
