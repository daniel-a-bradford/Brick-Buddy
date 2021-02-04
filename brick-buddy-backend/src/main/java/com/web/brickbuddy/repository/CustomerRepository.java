package com.web.brickbuddy.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.web.brickbuddy.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
	
	Optional<Customer> findByEmail(String email);
	
	Optional<Customer> findByBlEmail(String blEmail);
	
	List<Customer> findByLocation(long location);
	
	List<Customer> findAllByEmail(String email);

}
