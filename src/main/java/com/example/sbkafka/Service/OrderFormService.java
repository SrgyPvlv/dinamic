package com.example.sbkafka.Service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.sbkafka.Model.OrderForm;
import com.example.sbkafka.Repository.OrderFormRepository;

@Service
public class OrderFormService {

	@Autowired OrderFormRepository orderformrepository;
	
	public void saveOrderForm(OrderForm orderform) {
		orderformrepository.saveAndFlush(orderform);
	}
	
	public List<OrderForm> findAll() {
		return orderformrepository.findAll();
		
	}
	
	public List<OrderForm> findAllByOrderOrderNumberAsc() {
		return orderformrepository.findAllByOrderOrderNumberAsc();	
		
	}
	
	public Page<OrderForm> findAllWithPageAsc(int str,int numstr){
		Pageable pageable=PageRequest.of(str,numstr,Sort.by("ordernumber").ascending());
		return orderformrepository.findAll(pageable);
	}
	
	public OrderForm getById(int id) {
		return orderformrepository.getById(id);
	}
	
	public void deleteById(int id) {
		orderformrepository.deleteById(id);
	}
	
	public void deleteAll() {
		orderformrepository.deleteAll();
	}
	
	public void editOrderForm(int id,int ordernumber,String bsnumber,String comm) {
		OrderForm edited=orderformrepository.getById(id);
		edited.setOrdernumber(ordernumber);
		edited.setBsnumber(bsnumber);
		edited.setComm(comm);
				
		orderformrepository.saveAndFlush(edited);
	}
	
	public int countOfOrders() {	
	    int countorders=orderformrepository.countOfOrders();
	    return countorders;
	}

	public Double sumOfOrders() {
		Double sumorders=orderformrepository.sumOfOrders();
		if (sumorders!=null) {return sumorders;}
		else {return Double.valueOf(0.00);}
	}
	
	public Double minOfOrder() {
		Double minorder=orderformrepository.minOfOrder();
		if (minorder!=null) {return minorder;}
		else {return Double.valueOf(0.00);}
	}
	
	public Double maxOfOrder() {
		Double maxorder=orderformrepository.maxOfOrder();
		if (maxorder!=null) {return maxorder;}
		else {return Double.valueOf(0.00);}
	}
	public Double avgOfOrder() {
		Double avgorder=orderformrepository.avgOfOrder();
		if (avgorder!=null) {return avgorder;}
		else {return Double.valueOf(0.00);}
	}
	public int countOfFalseOut(double f1,double f2,double f3,double f4,double f5,double f6) {	
	    int countfalse=orderformrepository.countOfFalseOut(f1, f2, f3, f4, f5, f6);
	    return countfalse;
	}
	public Double sumOfFalseOut(double f1,double f2,double f3,double f4,double f5,double f6) {
		Double sumfalse=orderformrepository.sumOfFalseOut(f1, f2, f3, f4, f5, f6);
		if (sumfalse!=null) {return sumfalse;}
		else {return Double.valueOf(0.00);}
	}
	public int countDfs() {	
	    int countdfs=orderformrepository.countDfs();
	    return countdfs;
	}
	public Double sumDfs() {
		Double sumdfs=orderformrepository.sumDfs();
		if (sumdfs!=null) {return sumdfs;}
		else {return Double.valueOf(0.00);}
	}
	public List<OrderForm> findByOrderNumber(int ordernum){
		return orderformrepository.findByOrderNumber(ordernum);
	}
	public List<OrderForm> findByBsName(String bsnum){
		return orderformrepository.findByBsName(bsnum);
	}
}
