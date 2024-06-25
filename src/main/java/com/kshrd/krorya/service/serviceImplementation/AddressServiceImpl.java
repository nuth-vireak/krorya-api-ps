package com.kshrd.krorya.service.serviceImplementation;

import com.kshrd.krorya.exception.SearchNotFoundException;
import com.kshrd.krorya.model.entity.Address;
import com.kshrd.krorya.model.request.AddressRequest;
import com.kshrd.krorya.repository.AddressRepository;
import com.kshrd.krorya.service.AddressService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    @Override
    public Address insertAddress(AddressRequest addressRequest, UUID userId) {
        Address address = addressRepository.insertAddress(addressRequest, userId);
        return address;
    }

    @Override
    public Address getAddressByAddressId(UUID addressId, UUID userId) {
        Address address = addressRepository.getAddressByAddressId(addressId, userId);
        if (address == null){
            throw new SearchNotFoundException("The address with ID " + addressId + " does not exist");
        }
        return address;
    }

    @Override
    public List<Address> getAllAddressesByUserId(UUID userId) {
        List<Address> addressList = addressRepository.getAllAddressesByUserId(userId);
        if (addressList.isEmpty()){
            throw new SearchNotFoundException("Address list is empty");
        }
        return addressList;
    }

    @Override
    public Address updateAddressByAddressId(AddressRequest addressRequest, UUID addressId, UUID userId) {
        Address address = getAddressByAddressId(addressId, userId);
        if (address == null){
            throw new SearchNotFoundException("The address with ID " + addressId + " does not exist");
        }
        address = addressRepository.updateAddressByAddressId(addressRequest, addressId, userId);
        return address;
    }

    @Override
    public Address deleteAddressByAddressId(UUID addressId, UUID userId) {
        Address address = getAddressByAddressId(addressId, userId);
        if (address == null){
            throw new SearchNotFoundException("The address with ID " + addressId + " does not exist");
        }
        address = addressRepository.deleteAddressByAddressId(addressId, userId);
        return address;
    }

}
