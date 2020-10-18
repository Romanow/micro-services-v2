package ru.romanow.inst.services.warehouse.domain

import com.google.common.base.Objects
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "order_item", indexes = [
    Index(name = "idx_order_item_item_id", columnList = "item_id"),
    Index(name = "idx_order_item_order_uid", columnList = "order_uid"),
    Index(name = "idx_order_item_item_uid", columnList = "order_item_uid", unique = true)
])
class OrderItem(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    @Column(name = "order_uid", nullable = false, length = 40)
    var orderUid: UUID? = null,

    @Column(name = "order_item_uid", nullable = false, length = 40, unique = true)
    var orderItemUid: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", foreignKey = ForeignKey(name = "fk_order_item_item_id"))
    var item: Item? = null,

    @Column
    var canceled: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OrderItem

        return Objects.equal(orderUid, other.orderUid) &&
            Objects.equal(orderItemUid, other.orderItemUid)
    }

    override fun hashCode(): Int {
        return Objects.hashCode(orderUid, orderItemUid)
    }

    override fun toString(): String {
        return "OrderItem(id=$id, orderUid=$orderUid, orderItemUid=$orderItemUid, canceled=$canceled)"
    }
}