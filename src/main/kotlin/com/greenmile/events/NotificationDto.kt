package com.greenmile.events

import com.greenmile.domain.Coordinate
import com.greenmile.domain.LastCoordinate
import com.greenmile.domain.Route

data class NotificationDto(
        val coordinate: Coordinate,
        val lastCoordinate: LastCoordinate
)