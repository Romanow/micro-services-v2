package ru.romanow.inst.services.warranty.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import ru.romanow.inst.services.warranty.domain.Warranty
import java.util.*

interface WarrantyRepository : JpaRepository<Warranty, Int> {

    fun findWarrantyByItemId(itemId: UUID): Optional<Warranty>

    @Modifying
    @Query("delete from Warranty where itemId = :itemId")
    fun stopWarranty(@Param("itemId") itemId: UUID): Int
}