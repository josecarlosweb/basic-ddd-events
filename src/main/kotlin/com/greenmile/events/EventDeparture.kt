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
class EventDeparture(
        private val stopRepository: StopRepository,
        private val eventRepository: EventRepository,
        private val routeRepository: RouteRepository
): IEventsProcessor, Observer {

    private val log = LoggerFactory.getLogger(EventDeparture::class.java)

    /**
     * para detectar se o motorista saiu de um cliente,
     * a coordenada anterior deve estar dentro do raio de atendimento
     * e a coordenada atual deve ser diferente da anterior
     */
    override fun processCoordinate(notificationDto: NotificationDto) {
        val route = routeRepository.getRouteByEquipment_Id(notificationDto.coordinate.equipmentId).get()
        val lastCoordinate = notificationDto.lastCoordinate
        val coordinate = notificationDto.coordinate

        val lastCoordinateIsInGeofence = haversineDistance(lastCoordinate.latitude, lastCoordinate.longitude, coordinate.latitude, coordinate.longitude) <= geofence
        val actualCoordinateIsDifferent = (lastCoordinate.latitude != coordinate.latitude || lastCoordinate.longitude != coordinate.longitude)

        if(lastCoordinateIsInGeofence && actualCoordinateIsDifferent){
            route.stops.filter { stop ->
                stop.departureAt == null && stop.arrivalAt != null
            }.map { stop ->
                val updatedStop = stop.copy(departureAt = Date())
                stopRepository.saveAndFlush(updatedStop)
                log.info("Driver Departured of Stop [{}]", stop)
                val newEvent = Event(eventType = EventType.DEPARTURE, `when` = Date(), stopId = stop.id)
                eventRepository.save(newEvent)
            }
        }
    }

    override fun update(o: Observable?, notificationDto: Any?) {
        if(notificationDto != null){
            processCoordinate(notificationDto as NotificationDto)
        }
    }
}