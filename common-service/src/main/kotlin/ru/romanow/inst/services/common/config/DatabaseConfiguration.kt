package ru.romanow.inst.services.common.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.auditing.CurrentDateTimeProvider
import org.springframework.data.auditing.DateTimeProvider
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import javax.sql.DataSource

@Configuration
@ConditionalOnBean(DataSource::class)
@EnableJpaAuditing(dateTimeProviderRef = "dateTimeProvider")
class DatabaseConfiguration {

    @Bean
    fun dateTimeProvider(): DateTimeProvider {
        return CurrentDateTimeProvider.INSTANCE
    }
}