package com.greenmile.domain

import com.greenmile.domain.enum.EventType
import java.util.*
import javax.persistence.*

@Entity
data class Event(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Int? = null,
        val eventType: EventType,
        val `when`: Date = Date(),
        val stopId: Int
)