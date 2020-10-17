package ru.romanow.inst.services.warehouse.model

import java.util.*

data class OrderItemResponse(
    val orderItemUid: UUID,
    val itemUid: UUID,
    val model: String,
    val size: SizeChart
)