package ru.romanow.inst.services.warranty.model

import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class ItemWarrantyRequest(
    @field:NotEmpty(message = "{field.is.empty")
    var reason: String,
    val availableCount: Int
)