package org.restaurant_service.dto;

import lombok.*;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantDto {
    private Long id;
    private String name;

    @NotBlank(message = "Cuisine is required")
    private String cuisine;

    @NotBlank(message = "Address is required")
    private String address;

    @Builder.Default
    @ToString.Exclude
    private List<DishDto> dishes = new ArrayList<>();
}
