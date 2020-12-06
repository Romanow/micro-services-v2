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
import ru.romanow.inst.services.store.exceptions.WarehouseProcessException
import ru.romanow.inst.services.warehouse.model.ItemInfoResponse
import ru.romanow.inst.services.warranty.model.ErrorResponse
import java.util.*
import javax.persistence.EntityNotFoundException

@Service
class WarehouseServiceImpl(
    @Value("\${warehouse.service.url}")
    private val warehouseServiceUrl: String,
    private val restClient: RestClient,
    private val fallback: Fallback,
    private val factory: CircuitBreakerFactory<Resilience4JConfigBuilder.Resilience4JCircuitBreakerConfiguration, Resilience4JConfigBuilder>
) : WarehouseService {

    override fun getItemInfo(itemUid: UUID): Optional<ItemInfoResponse> {
        val url = "$warehouseServiceUrl/api/v1/warehouse/$itemUid"
        return factory
            .create("getItemInfo")
            .run({
                restClient.get(url, ItemInfoResponse::class.java)
                    .exceptionMapping(HttpStatus.NOT_FOUND) { EntityNotFoundException(extractErrorMessage(it)) }
                    .commonExceptionMapping { WarehouseProcessException(extractErrorMessage(it)) }
                    .execute()
            }, { throwable -> fallback.apply(HttpMethod.GET, url, throwable) })
    }

    private fun extractErrorMessage(errorResponse: String) =
        JsonSerializer.fromJson(errorResponse, ErrorResponse::class.java).message
}