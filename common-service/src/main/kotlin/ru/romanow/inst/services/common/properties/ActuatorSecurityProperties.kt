package ru.romanow.inst.services.common.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotEmpty

@Validated
@ConfigurationProperties(prefix = "management.credentials")
data class ActuatorSecurityProperties(
    @field:NotEmpty val user: String,
    @field:NotEmpty val passwd: String,
    @field:NotEmpty val role: String
)
