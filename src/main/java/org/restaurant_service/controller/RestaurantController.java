package org.restaurant_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.restaurant_service.dto.RestaurantDto;
import org.restaurant_service.dto.RestaurantRequest;
import org.restaurant_service.service.RestaurantService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    // Публичные endpoints

    @GetMapping
    public ResponseEntity<Page<RestaurantDto>> getAllRestaurants(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("GET /api/restaurants - Fetching all restaurants");
        Page<RestaurantDto> restaurants = restaurantService.getAllRestaurants(pageable);
        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/search")
    public ResponseEntity<List<RestaurantDto>> getRestaurantsByCuisine(@RequestParam String cuisine) {
        log.info("GET /api/restaurants/search?cuisine={}", cuisine);
        List<RestaurantDto> restaurants = restaurantService.getRestaurantsByCuisine(cuisine);
        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDto> getRestaurantById(@PathVariable Long id) {
        log.info("GET /api/restaurants/{}", id);
        RestaurantDto restaurant = restaurantService.getRestaurantById(id);
        return ResponseEntity.ok(restaurant);
    }

    // Админ endpoints

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RestaurantDto> createRestaurant(@Valid @RequestBody RestaurantRequest request) {
        log.info("POST /api/restaurants - Creating new restaurant: {}", request.getName());
        RestaurantDto created = restaurantService.createRestaurant(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RestaurantDto> updateRestaurant(
            @PathVariable Long id,
            @Valid @RequestBody RestaurantRequest request) {
        log.info("PUT /api/restaurants/{} - Updating restaurant", id);
        RestaurantDto updated = restaurantService.updateRestaurant(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id) {
        log.info("DELETE /api/restaurants/{} - Deleting restaurant", id);
        restaurantService.deleteRestaurant(id);
        return ResponseEntity.noContent().build();
    }
}