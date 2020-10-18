package ru.romanow.inst.services.warranty.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import ru.romanow.inst.services.warranty.domain.Warranty
import java.util.*

interface WarrantyRepository : JpaRepository<Warranty, Int> {

    fun findByItemUid(itemUid: UUID): Optional<Warranty>

    @Modifying
    @Query("delete from Warranty w where w.itemUid = :itemUid")
    fun stopWarranty(@Param("itemUid") itemUid: UUID): Int
}