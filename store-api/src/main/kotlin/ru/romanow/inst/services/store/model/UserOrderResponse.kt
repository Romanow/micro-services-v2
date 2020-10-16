package ru.romanow.inst.services.store.model

import java.util.*

data class UserOrderResponse(
    var orderId: UUID,
    val date: String,
    val model: String,
    val size: SizeChart,
    val warrantyDate: String,
    val warrantyStatus: WarrantyStatus
)