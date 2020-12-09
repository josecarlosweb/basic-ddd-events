package com.greenmile.repository

import com.greenmile.domain.Equipment
import com.greenmile.domain.LastCoordinate
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface LastCoordinateRepository: JpaRepository<LastCoordinate, Int> {

    fun getLastCoordinateByEquipment_Id(equipmentId: Int): Optional<LastCoordinate>

}