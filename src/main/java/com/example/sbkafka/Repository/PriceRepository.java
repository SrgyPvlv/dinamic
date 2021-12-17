package com.example.sbkafka.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.sbkafka.Model.Price;

public interface PriceRepository extends JpaRepository<Price,Integer>{

	
}
