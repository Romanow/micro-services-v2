package ru.romanow.inst.services.store.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
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
    private val restClient: RestClient
) : WarehouseService {

    override fun getItemInfo(itemUid: UUID): Optional<ItemInfoResponse> {
        val url = "$warehouseServiceUrl/api/v1/warehouse/$itemUid"
        return restClient.get(url, ItemInfoResponse::class.java)
            .exceptionMapping(HttpStatus.NOT_FOUND) { EntityNotFoundException(extractErrorMessage(it)) }
            .commonExceptionMapping { WarehouseProcessException(extractErrorMessage(it)) }
            .execute()
    }

    private fun extractErrorMessage(errorResponse: String) =
        JsonSerializer.fromJson(errorResponse, ErrorResponse::class.java).message
}