package ru.romanow.inst.services.warranty.model

data class OrderWarrantyResponse(
    var warrantyDate: String,
    val decision: String
)