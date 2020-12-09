package com.greenmile.util

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class HaversineKtTest {

    @Test
    fun haversineDistanceTest() {
        val latOrigin = 38.898556
        val lonOrigin = -77.037852

        val latDestination = 38.897147
        val lonDestination = -77.043934

        val distance = haversineDistance(latOrigin, lonOrigin, latDestination, lonDestination)
        Assertions.assertEquals(distance, 0.549310944307586)
    }
}