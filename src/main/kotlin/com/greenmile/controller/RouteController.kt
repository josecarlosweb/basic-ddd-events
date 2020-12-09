package com.greenmile.controller

import com.greenmile.domain.Route
import com.greenmile.repository.RouteRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/routes")
class RouteController(
        private val routeRepository: RouteRepository
) {

    @PostMapping
    fun addRoute(@RequestBody route: Route) {
        routeRepository.save(route)
    }

    @PutMapping
    fun updateRoute(@RequestBody route: Route) {
        routeRepository.save(route)
    }

    @DeleteMapping
    fun deleteRoute(@RequestBody route: Route) {
        routeRepository.delete(route)
    }

    @GetMapping
    fun getRoutes(): ResponseEntity<List<Route>> {
        return ResponseEntity(routeRepository.findAll(), HttpStatus.OK)
    }

    @GetMapping("/{id}")
    fun getRouteById(@PathVariable id: Int): ResponseEntity<Route> {
        val optionalRoute = routeRepository.findById(id)
        return if (optionalRoute.isPresent) {
            ResponseEntity(optionalRoute.get(), HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @GetMapping("/ByEquipmentId/{id}")
    fun getRouteByEquipmentId(@PathVariable id: Int): ResponseEntity<Route> {
        val optionalRoute = routeRepository.getRouteByEquipment_Id(id)
        return if (optionalRoute.isPresent) {
            ResponseEntity(optionalRoute.get(), HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

}