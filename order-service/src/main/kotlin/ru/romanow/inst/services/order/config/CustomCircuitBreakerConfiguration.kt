package ru.romanow.inst.services.order.config

import org.springframework.context.annotation.Configuration
import ru.romanow.inst.services.common.config.CircuitBreakerConfigurationSupport
import ru.romanow.inst.services.order.exceptions.ItemNotAvailableException
import ru.romanow.inst.services.order.exceptions.WarehouseProcessException
import ru.romanow.inst.services.order.exceptions.WarrantyProcessException
import javax.persistence.EntityNotFoundException

@Configuration
class CustomCircuitBreakerConfiguration : CircuitBreakerConfigurationSupport {
    override fun ignoredExceptions(): Array<Class<out Throwable>> {
        return arrayOf(
            EntityNotFoundException::class.java,
            ItemNotAvailableException::class.java,
            WarehouseProcessException::class.java,
            WarrantyProcessException::class.java
        )
    }
}