package ru.romanow.inst.services.warehouse.model

import java.util.*

data class ItemInfoResponse(
    var itemId: UUID,
    val model: String,
    val size: SizeChart
)