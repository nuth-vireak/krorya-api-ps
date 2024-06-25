package com.kshrd.krorya.controller;

import com.kshrd.krorya.model.entity.Address;
import com.kshrd.krorya.model.entity.AppUser;
import com.kshrd.krorya.model.entity.CustomUserDetail;
import com.kshrd.krorya.model.request.AddressRequest;
import com.kshrd.krorya.model.response.ApiResponse;
import com.kshrd.krorya.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/addresses")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Address Controller", description = "Endpoint for managing addresses")
public class AddressController {
    private final AddressService addressService;

    @Operation(summary = "Insert user address")
    @PostMapping
    public ResponseEntity<ApiResponse<Address>> insertAddress(@Valid @RequestBody AddressRequest addressRequest){
        UUID userId = getUsernameOfCurrentUser();
        Address address = addressService.insertAddress(addressRequest, userId);
        return ResponseEntity.ok(
                ApiResponse.<Address>builder()
                        .message("Address is inserted successfully!")
                        .status(HttpStatus.CREATED)
                        .code(201)
                        .payload(address)
                        .localDateTime(LocalDateTime.now())
                        .build()
        );
    }

    @Operation(summary = "Get all addresses of current user")
    @GetMapping
    public ResponseEntity<ApiResponse<List<Address>>> getAllAddressesOfUser(){
        UUID userId = getUsernameOfCurrentUser();
        List<Address> addressList = addressService.getAllAddressesByUserId(userId);
        return ResponseEntity.ok(
                ApiResponse.<List<Address>>builder()
                        .message("All addresses of user " + userId + " is fetched successfully")
                        .status(HttpStatus.OK)
                        .code(200)
                        .payload(addressList)
                        .localDateTime(LocalDateTime.now())
                        .build()
        );
    }

    @Operation(summary = "Get address by address id")
    @GetMapping("{addressId}")
    public ResponseEntity<ApiResponse<Address>> getAddressByAddressId(
            @Parameter(description = "Address ID to update address", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
            @PathVariable UUID addressId){
        UUID userId = getUsernameOfCurrentUser();
        Address address = addressService.getAddressByAddressId(addressId, userId);
        return ResponseEntity.ok(
                ApiResponse.<Address>builder()
                        .message("Address with ID " + addressId + " is fetched successfully!")
                        .status(HttpStatus.OK)
                        .code(200)
                        .payload(address)
                        .localDateTime(LocalDateTime.now())
                        .build()
        );
    }

    @Operation(summary = "Update user's address")
    @PutMapping("{addressId}")
    public ResponseEntity<ApiResponse<Address>> updateAddressAddressId(
            @Parameter(description = "Address ID to update address", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
            @PathVariable UUID addressId,
            @Valid @RequestBody AddressRequest addressRequest){
        UUID userId = getUsernameOfCurrentUser();
        Address address = addressService.updateAddressByAddressId(addressRequest, addressId, userId);
        return ResponseEntity.ok(
                ApiResponse.<Address>builder()
                        .message("Address with ID " + addressId + " is updated successfully!")
                        .status(HttpStatus.OK)
                        .code(200)
                        .payload(address)
                        .localDateTime(LocalDateTime.now())
                        .build()
        );
    }

    @Operation(summary = "Delete user address by address id")
    @DeleteMapping("{addressId}")
    public ResponseEntity<ApiResponse<Address>> deleteAddressByAddressId(
            @Parameter(description = "Address ID to update address", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
            @PathVariable UUID addressId){
        UUID userId = getUsernameOfCurrentUser();
        Address address = addressService.deleteAddressByAddressId(addressId, userId);
        return ResponseEntity.ok(
                ApiResponse.<Address>builder()
                        .message("Address with ID " + addressId + " is deleted successfully!")
                        .status(HttpStatus.OK)
                        .code(200)
                        .payload(address)
                        .localDateTime(LocalDateTime.now())
                        .build()
        );
    }



    UUID getUsernameOfCurrentUser() {
        CustomUserDetail userDetails = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AppUser appUser = userDetails.getAppUser();
        System.out.println("current user is : " + userDetails);
        return appUser.getUserId();
    }
}
