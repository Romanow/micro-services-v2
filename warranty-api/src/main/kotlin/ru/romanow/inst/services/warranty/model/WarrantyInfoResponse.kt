package ru.romanow.inst.services.warranty.model

import java.util.*

data class WarrantyInfoResponse(
    var itemUid: UUID,
    val warrantyDate: String,
    val status: String
)