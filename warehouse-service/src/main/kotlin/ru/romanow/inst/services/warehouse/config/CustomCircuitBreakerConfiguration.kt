package ru.romanow.inst.services.warehouse.config

import org.springframework.context.annotation.Configuration
import ru.romanow.inst.services.common.config.CircuitBreakerConfigurationSupport
import ru.romanow.inst.services.warehouse.exceptions.ItemNotAvailableException
import ru.romanow.inst.services.warehouse.exceptions.WarrantyProcessException
import javax.persistence.EntityNotFoundException

@Configuration
class CustomCircuitBreakerConfiguration : CircuitBreakerConfigurationSupport {
    override fun ignoredExceptions(): Array<Class<out Throwable>> {
        return arrayOf(
            EntityNotFoundException::class.java,
            ItemNotAvailableException::class.java,
            WarrantyProcessException::class.java
        )
    }
}