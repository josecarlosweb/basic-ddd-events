package com.greenmile.repository

import com.greenmile.domain.Route
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RouteRepository: JpaRepository<Route, Int> {

    fun getRouteByEquipment_Id(equipmentId: Int): Optional<Route>

}