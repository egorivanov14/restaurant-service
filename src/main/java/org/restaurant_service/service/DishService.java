package org.restaurant_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.restaurant_service.dto.DishDto;
import org.restaurant_service.dto.DishRequest;
import org.restaurant_service.entity.Dish;
import org.restaurant_service.entity.Restaurant;
import org.restaurant_service.exception.ResourceNotFoundException;
import org.restaurant_service.mapper.DishMapper;
import org.restaurant_service.repository.DishRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DishService {

    private final DishRepository dishRepository;
    private final DishMapper dishMapper;
    private final RestaurantService restaurantService;

    // Публичные методы

    @Transactional(readOnly = true)
    public List<DishDto> getDishesByRestaurantId(Long restaurantId) {
        log.debug("Fetching dishes for restaurant: {}", restaurantId);

        if (!restaurantService.existsById(restaurantId)) {
            throw new ResourceNotFoundException("Restaurant not found with id: " + restaurantId);
        }

        return dishRepository.findByRestaurantId(restaurantId).stream()
                .map(dishMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public DishDto getDishById(Long id) {
        log.debug("Fetching dish by id: {}", id);
        Dish dish = findDishOrThrow(id);
        return dishMapper.toDto(dish);
    }

    // Админ методы

    public DishDto createDish(Long restaurantId, DishRequest request) {
        log.info("Creating new dish for restaurant {}: {}", restaurantId, request.getName());

        Restaurant restaurant = restaurantService.findRestaurantOrThrow(restaurantId);

        Dish dish = dishMapper.toEntity(request);
        dish.setRestaurant(restaurant);

        Dish saved = dishRepository.save(dish);
        log.info("Dish created successfully with id: {}", saved.getId());

        return dishMapper.toDto(saved);
    }

    public DishDto updateDish(Long id, DishRequest request) {
        log.info("Updating dish with id: {}", id);

        Dish dish = findDishOrThrow(id);
        dishMapper.updateEntity(dish, request);

        Dish updated = dishRepository.save(dish);
        log.info("Dish updated successfully: {}", id);

        return dishMapper.toDto(updated);
    }

    public void deleteDish(Long id) {
        log.info("Deleting dish with id: {}", id);

        if (!dishRepository.existsById(id)) {
            throw new ResourceNotFoundException("Dish not found with id: " + id);
        }

        dishRepository.deleteById(id);
        log.info("Dish deleted successfully: {}", id);
    }

    // Вспомогательные методы

    @Transactional(readOnly = true)
    public Dish findDishOrThrow(Long id) {
        return dishRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dish not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return dishRepository.existsById(id);
    }
}