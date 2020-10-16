package ru.romanow.inst.services.warranty.model

import java.util.*

data class WarrantyInfoResponse(
    var itemId: UUID,
    val warrantyDate: String,
    val status: WarrantyStatus
)