package com.greenmile.repository

import com.greenmile.domain.Stop
import org.springframework.data.jpa.repository.JpaRepository

interface StopRepository: JpaRepository<Stop, Int>