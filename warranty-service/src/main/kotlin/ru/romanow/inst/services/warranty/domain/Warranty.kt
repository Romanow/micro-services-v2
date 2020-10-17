package ru.romanow.inst.services.warranty.domain

import com.google.common.base.Objects
import ru.romanow.inst.services.warranty.model.WarrantyStatus
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "warranty", indexes = [
    Index(name = "idx_warranty_item_id", columnList = "item_id", unique = true)
])
class Warranty(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    @Column(name = "item_id", nullable = false, unique = true, length = 40)
    var itemId: UUID? = null,

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

        return Objects.equal(itemId, other.itemId) &&
            status === other.status
    }

    override fun hashCode(): Int {
        return Objects.hashCode(itemId, warrantyDate, status)
    }

    override fun toString(): String {
        return "Warranty(id=$id, itemId=$itemId, warrantyDate=$warrantyDate, status=$status)"
    }
}