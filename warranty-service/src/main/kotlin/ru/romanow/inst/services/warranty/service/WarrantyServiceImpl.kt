package ru.romanow.inst.services.warranty.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.romanow.inst.services.warranty.domain.Warranty
import ru.romanow.inst.services.warranty.model.ItemWarrantyRequest
import ru.romanow.inst.services.warranty.model.OrderWarrantyResponse
import ru.romanow.inst.services.warranty.model.WarrantyDecision
import ru.romanow.inst.services.warranty.model.WarrantyDecision.*
import ru.romanow.inst.services.warranty.model.WarrantyInfoResponse
import ru.romanow.inst.services.warranty.model.WarrantyStatus.*
import ru.romanow.inst.services.warranty.repository.WarrantyRepository
import java.time.LocalDateTime
import java.time.LocalDateTime.now
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*
import javax.persistence.EntityNotFoundException

@Service
class WarrantyServiceImpl(
    private val warrantyRepository: WarrantyRepository
) : WarrantyService {
    private val logger = LoggerFactory.getLogger(WarrantyServiceImpl::class.java)

    @Transactional(readOnly = true)
    override fun getWarrantyByItemUid(itemUid: UUID): Warranty {
        return warrantyRepository
            .findByItemUid(itemUid)
            .orElseThrow { EntityNotFoundException("Warranty not found for itemUid '$itemUid'") }
    }

    @Transactional(readOnly = true)
    override fun getWarrantyInfo(itemUid: UUID): WarrantyInfoResponse {
        return buildWarrantyInfo(getWarrantyByItemUid(itemUid))
    }

    @Transactional
    override fun warrantyRequest(itemUid: UUID, request: ItemWarrantyRequest): OrderWarrantyResponse {
        logger.info("Process warranty request (reason: {}) for item '{}'", request.reason, itemUid)

        val warranty = getWarrantyByItemUid(itemUid)
        var decision = REFUSE
        if (isActiveWarranty(warranty) && warranty.status === ON_WARRANTY) {
            decision = if (request.availableCount > 0) RETURN else FIXING
        }

        logger.info("Warranty decision on item '{}' is {} (count: {}, status: {})",
            itemUid, decision, request.availableCount, warranty.status)

        updateWarranty(warranty, decision, request.reason)
        return OrderWarrantyResponse(
            decision = decision.name,
            warrantyDate = formatDate(warranty.warrantyDate!!)
        )
    }

    @Transactional
    override fun startWarranty(itemUid: UUID) {
        val warranty = Warranty(
            itemUid = itemUid,
            status = ON_WARRANTY,
            warrantyDate = now()
        )

        warrantyRepository.save(warranty)
        logger.info("Start warranty for item '{}'", itemUid)
    }

    @Transactional
    override fun stopWarranty(itemUid: UUID) {
        val deleted = warrantyRepository.stopWarranty(itemUid)
        if (deleted > 0) {
            logger.info("Remove item '{}' from warranty", itemUid)
        }
    }

    private fun updateWarranty(warranty: Warranty, decision: WarrantyDecision, reason: String) {
        warranty.comment = reason
        warranty.status = if (decision === REFUSE) REMOVED_FROM_WARRANTY else USE_WARRANTY

        warrantyRepository.save(warranty)
        logger.info("Update warranty status {} for itemUid '{}'", warranty.status, warranty.itemUid)
    }

    private fun isActiveWarranty(warranty: Warranty) =
        warranty.warrantyDate!!.isAfter(now().minus(1, ChronoUnit.MONTHS))

    private fun buildWarrantyInfo(warranty: Warranty) =
        WarrantyInfoResponse(
            itemUid = warranty.itemUid!!,
            status = warranty.status!!.name,
            warrantyDate = formatDate(warranty.warrantyDate!!)
        )

    private fun formatDate(date: LocalDateTime) = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(date)
}