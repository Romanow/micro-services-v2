package ru.romanow.inst.services.store

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import ru.romanow.inst.services.store.config.DatabaseTestConfiguration

@SpringBootTest
@Import(DatabaseTestConfiguration::class)
internal class StoreApplicationTest {

    @Test
    fun test() {
    }
}