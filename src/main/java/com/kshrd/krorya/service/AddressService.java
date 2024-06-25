package com.kshrd.krorya.service;

import com.kshrd.krorya.model.entity.Address;
import com.kshrd.krorya.model.request.AddressRequest;

import java.util.List;
import java.util.UUID;

public interface AddressService {
    Address insertAddress(AddressRequest addressRequest, UUID userId);

    List<Address> getAllAddressesByUserId(UUID userId);

    Address getAddressByAddressId(UUID addressId, UUID userId);

    Address updateAddressByAddressId(AddressRequest addressRequest, UUID addressId, UUID userId);

    Address deleteAddressByAddressId(UUID addressId, UUID userId);


}
