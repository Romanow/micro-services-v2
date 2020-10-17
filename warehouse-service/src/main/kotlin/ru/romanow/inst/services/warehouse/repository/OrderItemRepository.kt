package ru.romanow.inst.services.warehouse.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import ru.romanow.inst.services.warehouse.domain.OrderItem
import java.util.*

interface OrderItemRepository : JpaRepository<OrderItem, Int> {
    fun findByOrderItemUid(uid: UUID): Optional<OrderItem>

    @Modifying
    @Query("delete from OrderItem where orderItemUid = :orderItemUid")
    fun returnOrderItem(@Param("orderItemUid") orderItemUid: UUID)
}