package ru.romanow.inst.services.warranty.model

import javax.validation.constraints.NotEmpty

data class ItemWarrantyRequest(
    @field:NotEmpty(message = "{field.is.empty}")
    var reason: String,
    val availableCount: Int
)