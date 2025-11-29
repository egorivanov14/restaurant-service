package org.restaurant_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.restaurant_service.dto.DishDto;
import org.restaurant_service.dto.DishRequest;
import org.restaurant_service.service.DishService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/restaurants/{restaurantId}/dishes")
@RequiredArgsConstructor
public class DishController {

    private final DishService dishService;

    // Публичные endpoints

    @GetMapping
    public ResponseEntity<List<DishDto>> getDishesByRestaurant(@PathVariable Long restaurantId) {
        log.info("GET /api/restaurants/{}/dishes - Fetching dishes", restaurantId);
        List<DishDto> dishes = dishService.getDishesByRestaurantId(restaurantId);
        return ResponseEntity.ok(dishes);
    }

    @GetMapping("/{dishId}")
    public ResponseEntity<DishDto> getDishById(@PathVariable Long restaurantId, @PathVariable Long dishId) {
        log.info("GET /api/restaurants/{}/dishes/{}", restaurantId, dishId);
        DishDto dish = dishService.getDishById(dishId);
        return ResponseEntity.ok(dish);
    }

    // Админ endpoints

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DishDto> createDish(
            @PathVariable Long restaurantId,
            @Valid @RequestBody DishRequest request) {
        log.info("POST /api/restaurants/{}/dishes - Creating new dish", restaurantId);
        DishDto created = dishService.createDish(restaurantId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{dishId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DishDto> updateDish(
            @PathVariable Long restaurantId,
            @PathVariable Long dishId,
            @Valid @RequestBody DishRequest request) {
        log.info("PUT /api/restaurants/{}/dishes/{}", restaurantId, dishId);
        DishDto updated = dishService.updateDish(dishId, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{dishId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDish(@PathVariable Long restaurantId, @PathVariable Long dishId) {
        log.info("DELETE /api/restaurants/{}/dishes/{}", restaurantId, dishId);
        dishService.deleteDish(dishId);
        return ResponseEntity.noContent().build();
    }
}