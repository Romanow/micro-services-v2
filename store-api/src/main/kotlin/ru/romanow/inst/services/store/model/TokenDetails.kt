package ru.romanow.inst.services.store.model

data class TokenDetails(
    val token: String,
    val refreshToken: String?,
    val tokenType: String
)
