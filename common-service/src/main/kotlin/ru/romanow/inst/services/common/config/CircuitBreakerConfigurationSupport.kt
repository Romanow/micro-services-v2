package ru.romanow.inst.services.common.config

interface CircuitBreakerConfigurationSupport {
    fun ignoredExceptions(): Array<Class<out Throwable>> {
        return arrayOf()
    }
}