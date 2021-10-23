package ru.romanow.inst.services.order.domain

import ru.romanow.inst.services.order.model.PaymentStatus
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "orders", indexes = [
    Index(name = "idx_orders_user_uid", columnList = "user_uid"),
    Index(name = "idx_orders_order_uid", columnList = "order_uid", unique = true),
    Index(name = "idx_orders_user_uid_and_order_uid", columnList = "user_uid, order_uid")
])
class Order(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    @Column(name = "user_uid", nullable = false)
    var userUid: UUID? = null,

    @Column(name = "order_uid", nullable = false, unique = true)
    var orderUid: UUID? = null,

    @Column(name = "item_uid", nullable = false)
    var itemUid: UUID? = null,

    @Column(name = "order_date", nullable = false)
    var orderDate: LocalDateTime? = null,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var status: PaymentStatus? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Order

        return Objects.equals(orderUid, other.orderUid) &&
            Objects.equals(itemUid, other.itemUid) &&
            status === other.status
    }

    override fun hashCode(): Int {
        return Objects.hash(orderUid, itemUid, status)
    }

    override fun toString(): String {
        return "Order(id=$id, userUid=$userUid, orderUid=$orderUid, itemUid=$itemUid, orderDate=$orderDate, status=$status)"
    }
}
