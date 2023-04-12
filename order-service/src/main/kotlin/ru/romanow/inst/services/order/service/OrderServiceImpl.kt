package ru.romanow.inst.services.order.service

import jakarta.persistence.EntityNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.romanow.inst.services.order.domain.Order
import ru.romanow.inst.services.order.model.OrderInfoResponse
import ru.romanow.inst.services.order.model.OrdersInfoResponse
import ru.romanow.inst.services.order.model.PaymentStatus
import ru.romanow.inst.services.order.repository.OrderRepository
import java.time.LocalDateTime
import java.time.LocalDateTime.now
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.stream.Collectors.toCollection

@Service
class OrderServiceImpl(
    private val orderRepository: OrderRepository
) : OrderService {
    private val logger = LoggerFactory.getLogger(OrderServiceImpl::class.java)

    @Transactional(readOnly = true)
    override fun getOrderByUid(orderUid: UUID): Order =
        orderRepository
            .findByOrderUid(orderUid)
            .orElseThrow { EntityNotFoundException("Order '$orderUid' not found") }

    @Transactional(readOnly = true)
    override fun getUserOrder(userId: String, orderUid: UUID): OrderInfoResponse =
        orderRepository
            .findByUserIdAndOrderUid(userId, orderUid)
            .map { buildOrderInfo(it) }
            .orElseThrow { EntityNotFoundException("Not found order '$orderUid' for user '$userId'") }

    @Transactional(readOnly = true)
    override fun getUserOrders(userId: String): OrdersInfoResponse =
        orderRepository
            .findByUserId(userId)
            .stream()
            .map { buildOrderInfo(it) }
            .collect(toCollection { OrdersInfoResponse() })

    @Transactional
    override fun createOrder(orderUid: UUID, userId: String, itemUid: UUID) {
        val order = Order(
            userId = userId,
            orderUid = orderUid,
            orderDate = now(),
            status = PaymentStatus.PAID,
            itemUid = itemUid
        )

        orderRepository.save(order)
        logger.debug("Create order '$orderUid' for user '$userId' and item '$itemUid'")
    }

    @Transactional
    override fun cancelOrder(orderUid: UUID) {
        val deleted = orderRepository.deleteOrder(orderUid)
        if (deleted > 0) {
            logger.debug("Deleted '$orderUid' order")
        }
    }

    @Transactional(readOnly = true)
    override fun checkOrder(orderUid: UUID) = orderRepository.findByOrderUid(orderUid).isPresent

    private fun buildOrderInfo(order: Order) =
        OrderInfoResponse(
            orderUid = order.orderUid!!,
            itemUid = order.itemUid!!,
            status = order.status!!,
            orderDate = formatDate(order.orderDate!!)
        )

    private fun formatDate(date: LocalDateTime) = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(date)
}