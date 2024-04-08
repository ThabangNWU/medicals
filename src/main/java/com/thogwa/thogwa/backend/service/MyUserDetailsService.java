package com.thogwa.thogwa.backend.service;

import com.thogwa.thogwa.backend.model.User;
import com.thogwa.thogwa.backend.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    CustomerRepository customerRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = customerRepository.findByUsername(username);
        if (user == null) {
            throw  new UsernameNotFoundException("user not found");
        }
        return new UserPrincipal(user);
    }
}
