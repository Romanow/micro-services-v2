package ru.romanow.inst.services.order.model

import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class CreateOrderRequest(
    @field:NotEmpty(message = "{field.is.empty")
    val model: String,
    val size: SizeChart
)