package com.web.brickbuddy.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.web.brickbuddy.model.WantedPart;

public interface WantedPartRepository extends JpaRepository<WantedPart, Long> {

	String keywordSearch="SELECT sp FROM WantedPart sp WHERE sp.name =:keyword OR sp.name LIKE (CONCAT('%',:keyword,'%'))";
	
	@Query("FROM WantedPart WHERE number=?1")
	Optional<WantedPart> findMinifig(String number);
	
	@Query("FROM WantedPart WHERE number=?1 AND color_num=?2")
	Optional<WantedPart> findPart(String number, String colorNum);
	
	@Query(keywordSearch)
	List<WantedPart> findByName(String keyword);
	
}
