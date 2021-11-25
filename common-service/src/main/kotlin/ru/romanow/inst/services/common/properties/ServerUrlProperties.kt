package ru.romanow.inst.services.common.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "services")
data class ServerUrlProperties(
    val orderUrl: String? = null,
    val warehouseUrl: String? = null,
    val warrantyUrl: String? = null
)