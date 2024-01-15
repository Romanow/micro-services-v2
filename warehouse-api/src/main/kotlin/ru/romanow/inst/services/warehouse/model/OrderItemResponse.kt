package ru.romanow.inst.services.warehouse.model

import java.util.UUID

data class OrderItemResponse(
    val orderItemUid: UUID,
    val orderUid: UUID,
    val model: String,
    val size: String
)
