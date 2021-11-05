package ru.romanow.inst.services.common.config

import io.opentracing.Tracer
import io.opentracing.noop.NoopTracerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
class TracingConfiguration {

    @Bean
    @Profile("!k8s")
    fun localTracer(): Tracer = NoopTracerFactory.create()
}