package ru.romanow.inst.services.store.model

import java.util.UUID

data class WarrantyResponse(
    var orderUid: UUID,
    val warrantyDate: String,
    val decision: String
)
