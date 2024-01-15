package ru.romanow.inst.services.warehouse.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import ru.romanow.inst.services.warehouse.model.SizeChart
import java.util.Objects

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

        return Objects.equals(model, other.model) && size === other.size
    }

    override fun hashCode(): Int {
        return Objects.hash(model, size)
    }

    override fun toString(): String {
        return "Item(id=$id, model=$model, size=$size, availableCount=$availableCount)"
    }
}
