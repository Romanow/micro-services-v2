package ru.romanow.inst.services.order.model

import java.util.*

data class OrderInfoResponse(
    var orderId: UUID,
    val orderDate: String,
    val itemId: UUID,
    val status: PaymentStatus
)