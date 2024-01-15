package ru.romanow.inst.services.order.model

import java.util.UUID

data class OrderInfoResponse(
    var orderUid: UUID,
    val orderDate: String,
    val itemUid: UUID,
    val status: PaymentStatus
)
