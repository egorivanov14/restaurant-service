package org.restaurant_service.mapper;

import org.mapstruct.*;
import org.restaurant_service.dto.DishDto;
import org.restaurant_service.dto.DishRequest;
import org.restaurant_service.entity.Dish;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DishMapper {

    @Mapping(target = "restaurantId", source = "restaurant.id")
    DishDto toDto(Dish dish);

    @Mapping(target = "id", ignore = true)
    Dish toEntity(DishRequest request);

    void updateEntity(@MappingTarget Dish dish, DishRequest request);
}
