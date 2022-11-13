package ru.romanow.inst.services.common.config

import org.springframework.http.HttpMethod
import reactor.core.publisher.Mono
import java.util.*

@FunctionalInterface
interface Fallback {
    fun <T> apply(method: HttpMethod, url: String, throwable: Throwable, vararg params: Any): Mono<T>
}