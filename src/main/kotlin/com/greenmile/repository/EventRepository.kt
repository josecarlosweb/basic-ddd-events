package com.greenmile.repository

import com.greenmile.domain.Event
import org.springframework.data.jpa.repository.JpaRepository

interface EventRepository: JpaRepository<Event, Int>