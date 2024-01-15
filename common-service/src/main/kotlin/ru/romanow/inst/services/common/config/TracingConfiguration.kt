package ru.romanow.inst.services.common.config

import io.micrometer.tracing.Tracer
import io.micrometer.tracing.annotation.DefaultNewSpanParser
import io.micrometer.tracing.annotation.ImperativeMethodInvocationProcessor
import io.micrometer.tracing.annotation.MethodInvocationProcessor
import io.micrometer.tracing.annotation.NewSpanParser
import io.micrometer.tracing.annotation.SpanAspect
import io.opentelemetry.context.propagation.TextMapPropagator
import io.opentelemetry.extension.trace.propagation.JaegerPropagator
import org.springframework.beans.factory.BeanFactory
import org.springframework.boot.actuate.autoconfigure.tracing.ConditionalOnEnabledTracing
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
@ConditionalOnEnabledTracing
class TracingConfiguration {

    @Bean
    fun jaegerPropagator(): TextMapPropagator = JaegerPropagator.getInstance()

    @Bean
    fun newSpanParser() = DefaultNewSpanParser()

    @Bean
    fun methodInvocationProcessor(
        newSpanParser: NewSpanParser,
        tracer: Tracer,
        beanFactory: BeanFactory
    ): MethodInvocationProcessor {
        return ImperativeMethodInvocationProcessor(
            newSpanParser,
            tracer,
            { beanFactory.getBean(it) },
            { beanFactory.getBean(it) }
        )
    }

    @Bean
    fun spanAspect(methodInvocationProcessor: MethodInvocationProcessor) = SpanAspect(methodInvocationProcessor)
}
