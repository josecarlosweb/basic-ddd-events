package com.greenmile.events

import com.greenmile.domain.Event
import com.greenmile.domain.enum.EventType
import com.greenmile.repository.EventRepository
import com.greenmile.repository.RouteRepository
import com.greenmile.repository.StopRepository
import com.greenmile.util.haversineDistance
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.*

@Component
class EventArrival(
        private val stopRepository: StopRepository,
        private val eventRepository: EventRepository,
        private val routeRepository: RouteRepository
) : IEventsProcessor, Observer {

    private val log = LoggerFactory.getLogger(EventArrival::class.java)

    /**
     * para detectar se o motorista chegou em algum cliente precisamos saber se
     * 1. esta dentro do raio de atendimento e
     * 2. se essa e a coordenada anterior são iguais
     */
    override fun processCoordinate(notificationDto: NotificationDto) {
        val lastCoordinate = notificationDto.lastCoordinate
        val coordinate = notificationDto.coordinate
        if (lastCoordinate.latitude == coordinate.latitude && lastCoordinate.longitude == coordinate.longitude) {
            val route = routeRepository.getRouteByEquipment_Id(notificationDto.coordinate.equipmentId).get()
            route.stops.filter { stop ->
                val distance = haversineDistance(stop.latitude, stop.longitude, coordinate.latitude, coordinate.longitude)
                stop.arrivalAt == null && distance <= geofence
            }.map { stop ->
                val updatedStop = stop.copy(arrivalAt = Date())
                stopRepository.saveAndFlush(updatedStop)
                log.info("Driver Arrival on Stop [{}]", stop)
                val newEvent = Event(eventType = EventType.ARRIVE, `when` = Date(), stopId = stop.id)
                eventRepository.save(newEvent)
            }
        }
    }

    override fun update(o: Observable?, notificationDto: Any?) {
        if (notificationDto != null) {
            processCoordinate(notificationDto as NotificationDto)
        }
    }

}