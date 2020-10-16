package ru.romanow.inst.services.warehouse.model

import java.util.*
import javax.validation.constraints.NotEmpty

data class OrderItemRequest(
    var orderId: UUID,
    @field:NotEmpty(message = "{field.is.empty")
    val model: String,
    val size: SizeChart
)