package org.restaurant_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestaurantRequest {

    @NotBlank(message = "Name is required")
    private String name;

    private String cuisine;

    private String address;

}
