package ru.romanow.inst.services.warehouse.model

import java.util.*

data class OrderItemResponse(
    val orderItemUid: UUID,
    val orderUid: UUID,
    val model: String,
    val size: String
)