package ru.romanow.inst.services.store.config

import jakarta.persistence.EntityNotFoundException
import org.springframework.context.annotation.Configuration
import ru.romanow.inst.services.common.config.CircuitBreakerConfigurationSupport
import ru.romanow.inst.services.store.exceptions.ItemNotAvailableException
import ru.romanow.inst.services.store.exceptions.OrderProcessException
import ru.romanow.inst.services.store.exceptions.WarehouseProcessException
import ru.romanow.inst.services.store.exceptions.WarrantyProcessException

@Configuration
class CustomCircuitBreakerConfiguration : CircuitBreakerConfigurationSupport {
    override fun ignoredExceptions(): Array<Class<out Throwable>> {
        return arrayOf(
            EntityNotFoundException::class.java,
            ItemNotAvailableException::class.java,
            OrderProcessException::class.java,
            WarehouseProcessException::class.java,
            WarrantyProcessException::class.java
        )
    }
}