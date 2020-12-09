package com.greenmile.domain

import java.util.*
import javax.persistence.*

@Entity
data class LastCoordinate(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Int? = null,
        @OneToOne
        @JoinColumn(name = "equipment_id", referencedColumnName = "id")
        val equipment: Equipment,
        val latitude: Double,
        val longitude: Double,
        val `when`: Date = Date(),
        @OneToOne
        @JoinColumn(name = "rote_id", referencedColumnName = "id")
        val route: Route
)

fun createLastCoordinate(equipmentId: Int, latitude: Double, longitude: Double, route: Route): LastCoordinate {
        val equipment = Equipment(id = equipmentId)
        return LastCoordinate(null, equipment, latitude,longitude, Date(), route)
}