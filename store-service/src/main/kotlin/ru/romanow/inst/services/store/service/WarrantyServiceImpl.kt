package ru.romanow.inst.services.store.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
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
    private val restClient: RestClient
) : WarrantyService {

    override fun getItemWarrantyInfo(itemUid: UUID): Optional<WarrantyInfoResponse> {
        val url = "$warrantyServiceUrl/api/v1/warranty/$itemUid"
        return restClient.get(url, WarrantyInfoResponse::class.java)
            .exceptionMapping(HttpStatus.NOT_FOUND) { EntityNotFoundException(extractErrorMessage(it)) }
            .commonExceptionMapping { WarrantyProcessException(extractErrorMessage(it)) }
            .execute()
    }

    private fun extractErrorMessage(errorResponse: String) =
        JsonSerializer.fromJson(errorResponse, ErrorResponse::class.java).message
}