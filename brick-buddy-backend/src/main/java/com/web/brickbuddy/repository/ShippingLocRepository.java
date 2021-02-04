package com.web.brickbuddy.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.web.brickbuddy.model.ShippingLoc;

public interface ShippingLocRepository extends JpaRepository<ShippingLoc, Long> {
	
	Optional<ShippingLoc> findByName(String name);
	
	Optional<ShippingLoc> findByAbrv(String abrv);

}
