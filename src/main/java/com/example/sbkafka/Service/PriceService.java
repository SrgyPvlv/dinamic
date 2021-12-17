package com.example.sbkafka.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.sbkafka.Model.Price;
import com.example.sbkafka.Repository.PriceRepository;

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
	
	public Price getById(int id) {
		return priceRepository.getById(id);
	}
	
	public void saveAllPrice(List<Price> prices) {
		priceRepository.saveAllAndFlush(prices);
	}
	
}
