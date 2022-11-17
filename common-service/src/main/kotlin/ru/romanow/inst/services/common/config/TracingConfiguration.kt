package ru.romanow.inst.services.common.config

import io.jaegertracing.internal.JaegerTracer
import io.jaegertracing.internal.MDCScopeManager
import io.opentracing.Tracer
import io.opentracing.contrib.java.spring.jaeger.starter.TracerBuilderCustomizer
import io.opentracing.noop.NoopTracerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * В TracingConfiguration для `profile` != k8s переопределяется `Tracer` на `NoopTracer`.
 * Файл `TracerAutoConfiguration` является auto configuration и создается раньше `TracingConfiguration`,
 * т.к. он является просто `@Configuration`, из-за этого метод `public Tracer getTracer()` отрабатывает несмотря на аннотацию
 * `@ConditionalOnMissingBean(Tracer.class)`. Для задания правильного порядка используется
 * `@AutoConfigureBefore(TracerAutoConfiguration::class)`.
 */
@Configuration
class TracingConfiguration {

    @Bean
    fun expandByMDCScopeManager(): ExpandByMDCScopeManager {
        return ExpandByMDCScopeManager()
    }

    class ExpandByMDCScopeManager : TracerBuilderCustomizer {
        override fun customize(builder: JaegerTracer.Builder) {
            val mdcScopeManager = MDCScopeManager.Builder().build()
            builder.withScopeManager(mdcScopeManager)
        }
    }

    @Bean
    @ConditionalOnProperty("opentracing.jaeger.enabled", havingValue = "false")
    fun jaegerTracer(): Tracer {
        return NoopTracerFactory.create()
    }
}