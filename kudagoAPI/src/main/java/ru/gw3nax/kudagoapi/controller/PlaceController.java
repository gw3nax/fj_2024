package ru.gw3nax.kudagoapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.gw3nax.kudagoapi.controller.dto.PlaceRequest;
import ru.gw3nax.kudagoapi.controller.dto.PlaceResponse;
import ru.gw3nax.kudagoapi.service.PlaceService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/places")
public class PlaceController {
    private final PlaceService placeService;

    @PostMapping()
    PlaceResponse createPlace(@RequestBody PlaceRequest place) {
        return placeService.save(place);
    }

    @PutMapping("/place")
    PlaceResponse updatePlace(@RequestBody PlaceRequest place) {
        return placeService.update(place);
    }

    @DeleteMapping("/place/{id}")
    void deletePlace(@PathVariable Long id) {
        placeService.delete(id);
    }

    @GetMapping
    List<PlaceResponse> findAllPlaces() {
        return placeService.findAll();
    }

    @GetMapping("/place/{id}")
    PlaceResponse findPlaceById(@PathVariable Long id) {
        return placeService.findById(id);
    }
}
