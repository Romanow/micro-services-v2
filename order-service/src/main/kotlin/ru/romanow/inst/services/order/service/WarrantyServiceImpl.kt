package ru.romanow.inst.services.order.service

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.romanow.inst.services.common.utils.JsonSerializer.fromJson
import ru.romanow.inst.services.common.utils.RestClient
import ru.romanow.inst.services.order.exceptions.WarrantyProcessException
import ru.romanow.inst.services.warranty.model.ErrorResponse
import java.util.*

@Service
class WarrantyServiceImpl(
    @Value("\${warranty.service.url}")
    private val warrantyServiceUrl: String,
    private val restClient: RestClient
) : WarrantyService {
    private val logger = LoggerFactory.getLogger(WarrantyServiceImpl::class.java)

    override fun startWarranty(itemUid: UUID) {
        val url = "$warrantyServiceUrl/api/v1/warranty/$itemUid"
        restClient
            .post(url, null, Void::class.java)
            .commonExceptionMapping { WarrantyProcessException(extractErrorMessage(it)) }
            .execute()
        logger.info("Successfully start warranty for item '$itemUid'")
    }

    override fun stopWarranty(itemUid: UUID) {
        val url = "$warrantyServiceUrl/api/v1/warranty/$itemUid"
        restClient
            .delete(url, Void::class.java)
            .commonExceptionMapping { WarrantyProcessException(extractErrorMessage(it)) }
            .execute()

        logger.info("Successfully stopped warranty for item '$itemUid'")
    }

    private fun extractErrorMessage(errorResponse: String) =
        fromJson(errorResponse, ErrorResponse::class.java).message
}