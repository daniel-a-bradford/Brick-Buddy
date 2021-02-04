package com.web.brickbuddy.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.web.brickbuddy.model.Vendor;

public interface VendorRepository extends JpaRepository<Vendor, Long> {

	List<Vendor> findByName(String name);
	
}
