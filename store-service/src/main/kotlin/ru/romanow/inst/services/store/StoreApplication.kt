package ru.romanow.inst.services.store

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration

@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class])
class StoreApplication

fun main(args: Array<String>) {
    SpringApplication.run(StoreApplication::class.java, *args)
}
