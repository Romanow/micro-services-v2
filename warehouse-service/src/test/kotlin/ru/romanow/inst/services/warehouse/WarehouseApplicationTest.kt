package ru.romanow.inst.services.warehouse

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import ru.romanow.inst.services.warehouse.config.DatabaseTestConfiguration

@SpringBootTest
@Import(DatabaseTestConfiguration::class)
internal class WarehouseApplicationTest {

    @Test
    fun test() {
    }
}