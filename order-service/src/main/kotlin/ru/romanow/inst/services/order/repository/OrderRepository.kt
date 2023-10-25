package ru.romanow.inst.services.order.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import ru.romanow.inst.services.order.domain.Order
import java.util.*

interface OrderRepository : JpaRepository<Order, Int> {
    fun findByOrderUid(orderUid: UUID): Optional<Order>

    fun findByUserIdAndOrderUid(userId: String, orderUid: UUID): Optional<Order>

    fun findByUserId(userId: String): List<Order>

    @Modifying
    @Query("delete from Order p where p.orderUid = :orderUid")
    fun deleteOrder(@Param("orderUid") orderUid: UUID): Int
}
