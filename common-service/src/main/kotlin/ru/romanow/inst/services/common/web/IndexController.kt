package ru.romanow.inst.services.common.web

import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class IndexController(
    @Value("\${spring.application.name}")
    private val applicationName: String
) {

    @GetMapping
    fun index(): String {
        return "Hello from $applicationName"
    }
}