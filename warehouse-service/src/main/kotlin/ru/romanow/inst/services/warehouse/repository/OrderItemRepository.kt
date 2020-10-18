package ru.romanow.inst.services.warehouse.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import ru.romanow.inst.services.warehouse.domain.OrderItem
import java.util.*

interface OrderItemRepository : JpaRepository<OrderItem, Int> {

    @Modifying
    @Query("update OrderItem set canceled = true where orderItemUid = :orderItemUid")
    fun cancelOrderItem(@Param("orderItemUid") orderItemUid: UUID)
}