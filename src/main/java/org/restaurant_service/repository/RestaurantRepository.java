package org.restaurant_service.repository;

import org.restaurant_service.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    boolean existsByName(String name);
    List<Restaurant> findByCuisineIgnoreCase(String cuisine);
}
