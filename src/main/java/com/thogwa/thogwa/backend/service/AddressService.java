package com.thogwa.thogwa.backend.service;

import com.thogwa.thogwa.backend.model.Address;
import com.thogwa.thogwa.backend.model.User;
import com.thogwa.thogwa.backend.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CustomerService customerService;
    public Address addShipmentAddress(Address address, String username) {
        // Check if the address already exists for the user
        User findUserByUsername = customerService.findByUsername(username);
        Address existingAddresses = addressRepository.findByCustomers(findUserByUsername);

        // If the address does not exist, create and save it
        Address newAddress = new Address();
        newAddress.setCustomers(findUserByUsername);
        newAddress.setCustomerId(findUserByUsername.getId());
        newAddress.setState(address.getState());
        newAddress.setCity(address.getCity());
        newAddress.setStreet(address.getStreet());
        newAddress.setPincode(address.getPincode());
        newAddress.setCountry(address.getCountry());

        return addressRepository.save(newAddress);
    }


}
