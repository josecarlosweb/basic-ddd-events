package com.greenmile.domain

import javax.persistence.*

@Entity
data class Equipment(
        @Id val id: Int,
        val name: String? = null
)