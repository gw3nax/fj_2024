package ru.gw3nax.kudagoapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.gw3nax.kudagoapi.controller.dto.EventRequest;
import ru.gw3nax.kudagoapi.controller.dto.EventResponse;
import ru.gw3nax.kudagoapi.service.EventService;

import java.util.List;

@RestController
@RequestMapping("/api/v2/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @PostMapping()
    public EventResponse createEvent(@RequestBody EventRequest event) {
        return eventService.save(event);
    }

    @PutMapping("/event")
    public EventResponse updateEvent(@RequestBody EventRequest event) {
        return eventService.update(event);
    }

    @DeleteMapping("/event/{id}")
    public void deleteEvent(@PathVariable Long id) {
        eventService.delete(id);
    }

    @GetMapping("/")
    public List<EventResponse> getAllEvents(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long place,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate) {
        return eventService.findAllWithFilters(name, place, fromDate, toDate);
    }

    @GetMapping
    public List<EventResponse> getAllEvents() {
        return eventService.findAll();
    }

    @GetMapping("/event/{id}")
    public EventResponse getEventById(@PathVariable Long id) {
        return eventService.findById(id);
    }

}
