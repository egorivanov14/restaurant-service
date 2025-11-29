package org.restaurant_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.restaurant_service.dto.RestaurantDto;
import org.restaurant_service.dto.RestaurantRequest;
import org.restaurant_service.entity.Restaurant;
import org.restaurant_service.exception.DuplicateResourceException;
import org.restaurant_service.exception.ResourceNotFoundException;
import org.restaurant_service.mapper.RestaurantMapper;
import org.restaurant_service.repository.RestaurantRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;

    // Публичные методы

    @Transactional(readOnly = true)
    public Page<RestaurantDto> getAllRestaurants(Pageable pageable) {
        log.debug("Fetching all restaurants with pagination: {}", pageable);
        return restaurantRepository.findAll(pageable)
                .map(restaurantMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<RestaurantDto> getRestaurantsByCuisine(String cuisine) {
        log.debug("Fetching restaurants by cuisine: {}", cuisine);
        return restaurantRepository.findByCuisineIgnoreCase(cuisine).stream()
                .map(restaurantMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public RestaurantDto getRestaurantById(Long id) {
        log.debug("Fetching restaurant by id: {}", id);
        Restaurant restaurant = findRestaurantOrThrow(id);
        return restaurantMapper.toDto(restaurant);
    }

    // Админ методы

    public RestaurantDto createRestaurant(RestaurantRequest request) {
        log.info("Creating new restaurant: {}", request.getName());

        if (restaurantRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Restaurant with name '" + request.getName() + "' already exists");
        }

        Restaurant restaurant = restaurantMapper.toEntity(request);
        Restaurant saved = restaurantRepository.save(restaurant);
        log.info("Restaurant created successfully with id: {}", saved.getId());

        return restaurantMapper.toDto(saved);
    }

    public RestaurantDto updateRestaurant(Long id, RestaurantRequest request) {
        log.info("Updating restaurant with id: {}", id);

        Restaurant restaurant = findRestaurantOrThrow(id);

        // Проверка уникальности имени, если имя изменилось
        if (!restaurant.getName().equals(request.getName()) &&
                restaurantRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Restaurant with name '" + request.getName() + "' already exists");
        }

        restaurantMapper.updateEntity(restaurant, request);
        Restaurant updated = restaurantRepository.save(restaurant);
        log.info("Restaurant updated successfully: {}", id);

        return restaurantMapper.toDto(updated);
    }

    public void deleteRestaurant(Long id) {
        log.info("Deleting restaurant with id: {}", id);

        if (!restaurantRepository.existsById(id)) {
            throw new ResourceNotFoundException("Restaurant not found with id: " + id);
        }

        restaurantRepository.deleteById(id);
        log.info("Restaurant deleted successfully: {}", id);
    }

    // Вспомогательные методы

    @Transactional(readOnly = true)
    public Restaurant findRestaurantOrThrow(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return restaurantRepository.existsById(id);
    }
}