package com.greenmile

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class DddEventsApplication

fun main(args: Array<String>) {
	runApplication<DddEventsApplication>(*args)

}