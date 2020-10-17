package ru.romanow.inst.services.common.config

import org.flywaydb.core.Flyway
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer
import org.springframework.boot.autoconfigure.flyway.FlywayProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import javax.sql.DataSource

@Configuration
@EnableConfigurationProperties(FlywayProperties::class)
class DatabaseConfiguration {

    @Bean
    @Autowired
    @DependsOn("entityManagerFactory")
    fun flyway(dataSource: DataSource, flywayProperties: FlywayProperties): Flyway {
        return Flyway
            .configure()
            .dataSource(dataSource)
            .locations(* flywayProperties.locations.toTypedArray())
            .outOfOrder(flywayProperties.isOutOfOrder)
            .baselineOnMigrate(flywayProperties.isBaselineOnMigrate)
            .schemas(* flywayProperties.schemas.toTypedArray())
            .load()
    }

    @Bean
    @Autowired
    fun flywayInitializer(dataSource: DataSource, flywayProperties: FlywayProperties): FlywayMigrationInitializer {
        return FlywayMigrationInitializer(flyway(dataSource, flywayProperties))
    }
}