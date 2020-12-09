package com.greenmile.stream

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.greenmile.domain.Coordinate
import com.greenmile.domain.createLastCoordinate
import com.greenmile.events.EventArrival
import com.greenmile.events.EventDeparture
import com.greenmile.events.NotificationDto
import com.greenmile.repository.LastCoordinateRepository
import com.greenmile.repository.RouteRepository
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import java.util.*
import javax.annotation.PostConstruct

@Component
class CoordinateProcessor(
        private val routeRepository: RouteRepository,
        private val lastCoordinateRepository: LastCoordinateRepository,
        private val eventArrival: EventArrival,
        private val eventDeparture: EventDeparture
): Observable() {

    private val log = LoggerFactory.getLogger(CoordinateProcessor::class.java)

    fun receiveCoordinate(coordinate: Coordinate){
        log.info("Coordinate received: [{}]", coordinate)
        val routeOptional = routeRepository.getRouteByEquipment_Id(coordinate.equipmentId)
        if(routeOptional.isPresent) {
            val route = routeOptional.get()
            val lastCoordinateOptional = lastCoordinateRepository.getLastCoordinateByEquipment_Id(coordinate.equipmentId)
            val lastCoordinate = if (lastCoordinateOptional.isPresent) {
                val lastCoordinate = lastCoordinateOptional.get()
                val updatedLastCoordinate = lastCoordinate.copy(latitude = coordinate.latitude, longitude = coordinate.longitude, `when` = coordinate.datePing)
                lastCoordinateRepository.save(updatedLastCoordinate)
                lastCoordinate
            } else {
                val newLasCoordinate = createLastCoordinate(coordinate.equipmentId, coordinate.latitude, coordinate.longitude, route)
                lastCoordinateRepository.save(newLasCoordinate)
            }
            setChanged()
            notifyObservers(NotificationDto(coordinate, lastCoordinate))
        }
    }

    @Scheduled(fixedDelay = 100000, initialDelay = 10000)
    fun consumeCoordinates(){
        val mapper = ObjectMapper().registerModule(KotlinModule())

        val jsonContent = "[{\"equipmentId\":10000,\"latitude\":-3.752414,\"longitude\":-38.511576,\"datePing\":1599904800000},{\"equipmentId\":10000,\"latitude\":-3.752526,\"longitude\":-38.512504,\"datePing\":1599904920000},{\"equipmentId\":10000,\"latitude\":-3.752581,\"longitude\":-38.513015,\"datePing\":1599905040000},{\"equipmentId\":10000,\"latitude\":-3.75262,\"longitude\":-38.513433,\"datePing\":1599905160000},{\"equipmentId\":10000,\"latitude\":-3.752637,\"longitude\":-38.513635,\"datePing\":1599905220000},{\"equipmentId\":10000,\"latitude\":-3.752637,\"longitude\":-38.513635,\"datePing\":1599905280000},{\"equipmentId\":10000,\"latitude\":-3.752637,\"longitude\":-38.513635,\"datePing\":1599905340000},{\"equipmentId\":10000,\"latitude\":-3.752637,\"longitude\":-38.513635,\"datePing\":1599905400000},{\"equipmentId\":10000,\"latitude\":-3.752696,\"longitude\":-38.513927,\"datePing\":1599905520000},{\"equipmentId\":10000,\"latitude\":-3.7526674,\"longitude\":-38.5149107,\"datePing\":1599905580000},{\"equipmentId\":10000,\"latitude\":-3.7522729,\"longitude\":-38.5162625,\"datePing\":1599905640000},{\"equipmentId\":10000,\"latitude\":-3.751894,\"longitude\":-38.515098,\"datePing\":1599905700000},{\"equipmentId\":10000,\"latitude\":-3.750796,\"longitude\":-38.515302,\"datePing\":1599905760000},{\"equipmentId\":10000,\"latitude\":-3.749769,\"longitude\":-38.515491,\"datePing\":1599905820000},{\"equipmentId\":10000,\"latitude\":-3.7500412,\"longitude\":-38.5161274,\"datePing\":1599905880000},{\"equipmentId\":10000,\"latitude\":-3.7500412,\"longitude\":-38.5161274,\"datePing\":1599905940000},{\"equipmentId\":10000,\"latitude\":-3.7500412,\"longitude\":-38.5161274,\"datePing\":1599906000000},{\"equipmentId\":10000,\"latitude\":-3.7500412,\"longitude\":-38.5161274,\"datePing\":1599906060000},{\"equipmentId\":10000,\"latitude\":-3.7500412,\"longitude\":-38.5161274,\"datePing\":1599906120000},{\"equipmentId\":10000,\"latitude\":-3.7500412,\"longitude\":-38.5161274,\"datePing\":1599906180000},{\"equipmentId\":10000,\"latitude\":-3.748932,\"longitude\":-38.515718,\"datePing\":1599906240000},{\"equipmentId\":10000,\"latitude\":-3.748671,\"longitude\":-38.516117,\"datePing\":1599906300000},{\"equipmentId\":10000,\"latitude\":-3.748641,\"longitude\":-38.516856,\"datePing\":1599906360000},{\"equipmentId\":10000,\"latitude\":-3.74974,\"longitude\":-38.516425,\"datePing\":1599906480000},{\"equipmentId\":10000,\"latitude\":-3.74974,\"longitude\":-38.516425,\"datePing\":1599906540000},{\"equipmentId\":10000,\"latitude\":-3.74974,\"longitude\":-38.516425,\"datePing\":1599906600000},{\"equipmentId\":10000,\"latitude\":-3.74974,\"longitude\":-38.516425,\"datePing\":1599906660000},{\"equipmentId\":10000,\"latitude\":-3.74974,\"longitude\":-38.516425,\"datePing\":1599906720000},{\"equipmentId\":10000,\"latitude\":-3.7506319,\"longitude\":-38.5178648,\"datePing\":1599906960000},{\"equipmentId\":10000,\"latitude\":-3.7506319,\"longitude\":-38.5178648,\"datePing\":1599907020000},{\"equipmentId\":10000,\"latitude\":-3.7506319,\"longitude\":-38.5178648,\"datePing\":1599907080000},{\"equipmentId\":10000,\"latitude\":-3.7506319,\"longitude\":-38.5178648,\"datePing\":1599907140000}]"
        val listCoordinates = mapper.readValue(jsonContent, Array<Coordinate>::class.java).asList()

        this.addObserver(eventArrival)
        this.addObserver(eventDeparture)

        listCoordinates.forEach { coordinate ->
            Thread.sleep(500)
            receiveCoordinate(coordinate)
        }
    }
}