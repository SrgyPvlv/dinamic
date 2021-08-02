package com.example.sbkafka;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PriceRepository extends JpaRepository<Price,Integer>{

	@Query("select Price from price where pricesName=:name")
	

	Price findValueByName(@Param("name") String name);
	
}
