package ru.romanow.inst.services.warranty.domain

import ru.romanow.inst.services.warranty.model.WarrantyStatus
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(
    name = "warranty", indexes = [
        Index(name = "idx_warranty_item_uid", columnList = "item_uid", unique = true)
    ]
)
class Warranty(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    @Column(name = "item_uid", nullable = false, unique = true)
    var itemUid: UUID? = null,

    @Column(name = "warranty_date", nullable = false)
    var warrantyDate: LocalDateTime? = null,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var status: WarrantyStatus? = null,

    @Column(length = 1024)
    var comment: String? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Warranty

        return Objects.equals(itemUid, other.itemUid) &&
            status === other.status
    }

    override fun hashCode(): Int {
        return Objects.hash(itemUid, warrantyDate, status)
    }

    override fun toString(): String {
        return "Warranty(id=$id, itemUid=$itemUid, warrantyDate=$warrantyDate, status=$status)"
    }
}