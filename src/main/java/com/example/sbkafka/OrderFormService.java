package com.example.sbkafka;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderFormService {

	@Autowired OrderFormRepository orderformrepository;
	
	public OrderFormService(OrderFormRepository orderformrepository) {
		
		this.orderformrepository=orderformrepository;
	}
	
	public void saveOrderForm(OrderForm orderform) {
		orderformrepository.saveAndFlush(orderform);
	}
	
	public List<OrderForm> findAll() {
		return orderformrepository.findAll();
	}
	
	public OrderForm getById(int id) {
		return orderformrepository.getById(id);
	}
	
	public void deleteById(int id) {
		orderformrepository.deleteById(id);
	}
	
}
