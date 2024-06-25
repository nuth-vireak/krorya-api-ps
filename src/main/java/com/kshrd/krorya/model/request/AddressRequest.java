package com.kshrd.krorya.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressRequest {
    @NotBlank(message = "Location should not be blank")
    @NotNull(message = "Location should not be null")
//    @Pattern(regexp = "^0\\d{8,9}$", message = "Phone number must start with 0 and have 9 or 10 total digit")
    private String location;

    @NotBlank(message = "Latitude not be blank")
    @NotNull(message = "Latitude should not be null")
    private String latitude;

    @NotBlank(message = "Longitude should not be blank")
    @NotNull(message = "Longitude should not be null")
    private String longitude;
}
