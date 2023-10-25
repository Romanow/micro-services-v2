package ru.romanow.inst.services.common.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "services")
data class ServerUrlProperties(
    val orderUrl: String? = null,
    val warehouseUrl: String? = null,
    val warrantyUrl: String? = null
)
