package com.web.brickbuddy.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.web.brickbuddy.model.StockedPart;

public interface StockedPartRepository extends JpaRepository<StockedPart, Long> {
	
	
	String keywordSearch="SELECT sp FROM StockedPart sp WHERE sp.name =:keyword OR sp.name LIKE (CONCAT('%',:keyword,'%'))";
	
	@Query("FROM StockedPart WHERE number=?1")
	Optional<StockedPart> findMinifig(String number);
	
	@Query("FROM StockedPart WHERE number=?1 AND color_num=?2")
	Optional<StockedPart> findPart(String number, String colorNum);
	
	@Query(keywordSearch)
	List<StockedPart> findByName(String keyword);
	
}
