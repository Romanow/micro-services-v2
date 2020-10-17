package ru.romanow.inst.services.warehouse.domain

import com.google.common.base.Objects
import ru.romanow.inst.services.warehouse.model.SizeChart
import javax.persistence.*

@Entity
@Table(name = "items")
class Item(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @Column(nullable = false)
    var model: String? = null,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var size: SizeChart? = null,

    @Column(name = "available_count", nullable = false)
    var availableCount: Int = 0
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Item

        return Objects.equal(model, other.model) && size === other.size
    }

    override fun hashCode(): Int {
        return Objects.hashCode(model, size)
    }

    override fun toString(): String {
        return "Item(id=$id, model=$model, size=$size, availableCount=$availableCount)"
    }
}
