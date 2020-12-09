package com.greenmile.domain

import java.util.*
import javax.persistence.*

@Entity
data class Stop(
        @Id val id: Int,
        val address: String,
        val latitude: Double,
        val longitude: Double,
        val arrivalAt: Date? = null,
        val departureAt: Date? = null,
)