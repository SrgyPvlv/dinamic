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
	
	public List<OrderForm> findAllByOrderOrderNumberAsc() {
		return orderformrepository.findAllByOrderOrderNumberAsc();	
		
	}
	
	public OrderForm getById(int id) {
		return orderformrepository.getById(id);
	}
	
	public void deleteById(int id) {
		orderformrepository.deleteById(id);
	}
	
	public void editOrderForm(int id,String ordernumber,String bsnumber,String datestart,String dateend,//
			String calc,String calcnds,String comm) {
		OrderForm edited=orderformrepository.getById(id);
		edited.setOrderNumber(ordernumber);
		edited.setBsNumber(bsnumber);
		edited.setDateStart(datestart);
		edited.setDateEnd(dateend);
		edited.setCalc(calc);
		edited.setCalcNds(calcnds);
		edited.setComm(comm);
		orderformrepository.saveAndFlush(edited);
		
	}
	
}
