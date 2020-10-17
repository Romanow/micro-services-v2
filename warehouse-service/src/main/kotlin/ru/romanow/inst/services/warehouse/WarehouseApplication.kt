package ru.romanow.inst.services.warehouse

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class WarehouseApplication

fun main(args: Array<String>) {
    SpringApplication.run(WarehouseApplication::class.java, *args)
}