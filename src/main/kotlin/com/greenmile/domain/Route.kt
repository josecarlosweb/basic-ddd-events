package com.greenmile.domain

import java.util.*
import javax.persistence.*

@Entity(name = "route")
data class Route(
        @Id val id: Int,
        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(name = "equipment_id", referencedColumnName = "id")
        val equipment: Equipment,
        val name: String,
        @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.EAGER)
        @JoinColumn(name = "stop_id", referencedColumnName = "id")
        val stops: List<Stop>,
        val datePlan: Date
)