package com.thogwa.thogwa.backend.repository;
import com.thogwa.thogwa.backend.model.Address;
import com.thogwa.thogwa.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address,Long> {
    Address findByCustomers(User user);
}
