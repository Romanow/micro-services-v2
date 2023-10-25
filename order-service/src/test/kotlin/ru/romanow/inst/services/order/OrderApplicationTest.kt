package ru.romanow.inst.services.order

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import ru.romanow.inst.services.order.config.DatabaseTestConfiguration

@SpringBootTest
@Import(DatabaseTestConfiguration::class)
internal class OrderApplicationTest {

    @Test
    fun test() {
    }
}
