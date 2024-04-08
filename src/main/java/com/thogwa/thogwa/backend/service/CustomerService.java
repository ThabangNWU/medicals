package com.thogwa.thogwa.backend.service;

import com.stripe.model.Customer;
import com.thogwa.thogwa.backend.dto.dto.CustomerDto;
import com.thogwa.thogwa.backend.model.ERole;
import com.thogwa.thogwa.backend.model.Role;
import com.thogwa.thogwa.backend.model.User;
import com.thogwa.thogwa.backend.repository.CustomerRepository;
//import com.thogwa.thogwa.backend.repository.RoleRepository;
import com.thogwa.thogwa.backend.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private RoleRepository roleRepository;



    public User save(User userDto) {
        User user = new User();
        String firstName = "";
        String lastName = "";
           lastName = userDto.getLastName().substring(0, 1).toUpperCase() +
                   userDto.getLastName().substring(1).toLowerCase();
             firstName = userDto.getFirstName().substring(0, 1).toUpperCase() +
                     userDto.getFirstName().substring(1).toLowerCase();

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        user.setUsername(userDto.getEmail());
        user.setMobileNumber(userDto.getMobileNumber());

//        Set<String> strRoles = roleStr;
//        Set<Role> roles = new HashSet<>();
//
//        if(strRoles == null) {
//            Role customer = roleRepository.findByName(ERole.ROLE_CUSTOMER).get();
//            roles.add(customer);
//        }
//        else {
//            strRoles.forEach( role -> {
//                switch (role) {
//                    case  "ADMIN" :
//                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN).get();
//                        roles.add(adminRole);
//                }
//            });
//        }
//
//        user.setRoles(roles);
//        customer.setRoles(Arrays.asList(roleRepository.findByName("CUSTOMER")));
        return customerRepository.save(user);
    }


    //Get All Users
    public List<User> findAll(){
        return customerRepository.findAll();
    }

    public Set<Role> saveRole (String username, String role) {
        Set<Role> roles = new HashSet<>();
        if( role.equals("Admin") || role.equals( "admin") || role.equals("ADMIN")) {
            Role role1 = null;
            role1.setName(ERole.ROLE_ADMIN);
            roles.add(role1);
        }
        return roles;
    }

    //Get User By Id
    public User findById(long id) {
        return customerRepository.findById(id).orElse(null);
    }

    //Delete User
    public void delete(long id) {
        customerRepository.deleteById(id);
    }
    public User findByUsername(String username) {
        return customerRepository.findByUsername(username);
    }

}
