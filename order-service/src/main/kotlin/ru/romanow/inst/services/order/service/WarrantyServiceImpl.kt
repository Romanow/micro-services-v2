package ru.romanow.inst.services.order.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import ru.romanow.inst.services.common.utils.buildEx
import ru.romanow.inst.services.order.exceptions.WarrantyProcessException
import java.util.*

@Service
class WarrantyServiceImpl(
    private val warrantyWebClient: WebClient
) : WarrantyService {
    private val logger = LoggerFactory.getLogger(WarrantyServiceImpl::class.java)

    override fun startWarranty(itemUid: UUID) {
        warrantyWebClient
            .post()
            .uri("/{itemUid}", itemUid)
            .retrieve()
            .onStatus({ it.isError }, { response -> buildEx(response) { WarrantyProcessException(it) } })
            .toBodilessEntity()
            .block()

        logger.info("Successfully start warranty for item '$itemUid'")
    }

    override fun stopWarranty(itemUid: UUID) {
        warrantyWebClient
            .delete()
            .uri("/{itemUid}", itemUid)
            .retrieve()
            .onStatus({ it.isError }, { response -> buildEx(response) { WarrantyProcessException(it) } })
            .toBodilessEntity()
            .block()

        logger.info("Successfully stopped warranty for item '$itemUid'")
    }
}