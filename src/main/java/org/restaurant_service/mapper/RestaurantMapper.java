package org.restaurant_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.restaurant_service.dto.RestaurantDto;
import org.restaurant_service.dto.RestaurantRequest;
import org.restaurant_service.entity.Restaurant;

@Mapper(componentModel = "spring", uses = DishMapper.class)
public interface RestaurantMapper {

    @Mapping(target = "dishes")
    RestaurantDto toDto(Restaurant restaurant);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dishes", ignore = true)
    Restaurant toEntity(RestaurantRequest request);

    void updateEntity(@MappingTarget Restaurant restaurant, RestaurantRequest request);

}
