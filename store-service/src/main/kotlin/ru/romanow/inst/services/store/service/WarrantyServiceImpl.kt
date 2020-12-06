package ru.romanow.inst.services.store.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import ru.romanow.inst.services.common.config.Fallback
import ru.romanow.inst.services.common.utils.JsonSerializer
import ru.romanow.inst.services.common.utils.RestClient
import ru.romanow.inst.services.store.exceptions.WarrantyProcessException
import ru.romanow.inst.services.warranty.model.ErrorResponse
import ru.romanow.inst.services.warranty.model.WarrantyInfoResponse
import java.util.*
import javax.persistence.EntityNotFoundException

@Service
class WarrantyServiceImpl(
    @Value("\${warranty.service.url}")
    private val warrantyServiceUrl: String,
    private val restClient: RestClient,
    private val fallback: Fallback,
    private val factory: CircuitBreakerFactory<Resilience4JConfigBuilder.Resilience4JCircuitBreakerConfiguration, Resilience4JConfigBuilder>
) : WarrantyService {

    override fun getItemWarrantyInfo(itemUid: UUID): Optional<WarrantyInfoResponse> {
        val url = "$warrantyServiceUrl/api/v1/warranty/$itemUid"
        return factory
            .create("getItemWarrantyInfo")
            .run({
                restClient.get(url, WarrantyInfoResponse::class.java)
                    .exceptionMapping(HttpStatus.NOT_FOUND) { EntityNotFoundException(extractErrorMessage(it)) }
                    .commonExceptionMapping { WarrantyProcessException(extractErrorMessage(it)) }
                    .execute()
            }, { throwable -> fallback.apply(HttpMethod.GET, url, throwable) })
    }

    private fun extractErrorMessage(errorResponse: String) =
        JsonSerializer.fromJson(errorResponse, ErrorResponse::class.java).message
}