package org.restaurant_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DishDto {
    private Long id;
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    private Integer price;

    @NotBlank(message = "Image URL is required")
    private String imageUrl;
    private Long restaurantId;
}
