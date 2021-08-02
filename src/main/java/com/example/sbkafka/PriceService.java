package com.example.sbkafka;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PriceService {

	@Autowired PriceRepository priceRepository;
	
	public PriceService(PriceRepository priceRepository) {
		this.priceRepository=priceRepository;
	}
	
	public List<Price> findAll(){
		return priceRepository.findAll();
	}
	
	public void savePrice(Price price) {
		priceRepository.saveAndFlush(price);
	}
	
}
