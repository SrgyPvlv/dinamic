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
	
	public void editOrderForm(int id,int ordernumber,String bsnumber,String datestart,String dateend,double calc,double calcnds,String comm,
			double jeepPrice,int jeepYesNo,double jeepOnePrice,double orderOutGoPrice,double orderCalcHPrice,
			double orderDiffTime,double timeHoursPrice,String owenerType,int dguType,String workType,double orderCalcDPrice,
			double orderDiffDay,double timeDayPrice,double orderTransport,String orderDistance,double orderKmPrice,double orderNds) {
		OrderForm edited=orderformrepository.getById(id);
		edited.setOrdernumber(ordernumber);
		edited.setBsnumber(bsnumber);
		edited.setDatestart(datestart);
		edited.setDateend(dateend);
		edited.setCalc(calc);
		edited.setCalcnds(calcnds);
		edited.setComm(comm);
		edited.setJeepprice(jeepPrice);
		edited.setJeepyesno(jeepYesNo);
		edited.setJeeponeprice(jeepOnePrice);
		edited.setOrderoutgoprice(orderOutGoPrice);
		edited.setOrdercalchprice(orderCalcHPrice);
		edited.setOrderdifftime(orderDiffTime);
		edited.setTimehoursprice(timeHoursPrice);
		edited.setOwenertype(owenerType);
		edited.setDgutype(dguType);
		edited.setWorktype(workType);
		edited.setOrdercalcdprice(orderCalcDPrice);
		edited.setOrderdiffday(orderDiffDay);
		edited.setTimedayprice(timeDayPrice);
		edited.setOrdertransport(orderTransport);
		edited.setOrderdistance(orderDistance);
		edited.setOrderkmprice(orderKmPrice);
		edited.setOrdernds(orderNds);
		
		orderformrepository.saveAndFlush(edited);
	}
	
	public int countOfOrders() {	
	    int countorders=orderformrepository.countOfOrders();
	    return countorders;
	}

	public double sumOfOrders() {
		double sumorders=orderformrepository.sumOfOrders();
		return sumorders;
	}
	
	public double minOfOrder() {
		double minorder=orderformrepository.minOfOrder();
		return minorder;
	}
	public double maxOfOrder() {
		double maxorder=orderformrepository.maxOfOrder();
		return maxorder;
	}
	public double avgOfOrder() {
		double avgorder=orderformrepository.avgOfOrder();
		return avgorder;
	}
	public int countOfFalseOut(double f1,double f2,double f3,double f4,double f5,double f6) {	
	    int countfalse=orderformrepository.countOfFalseOut(f1, f2, f3, f4, f5, f6);
	    return countfalse;
	}
	public double sumOfFalseOut(double f1,double f2,double f3,double f4,double f5,double f6) {
		double sumfalse=orderformrepository.sumOfFalseOut(f1, f2, f3, f4, f5, f6);
		return sumfalse;
	}
	public int countDfs() {	
	    int countdfs=orderformrepository.countDfs();
	    return countdfs;
	}
	public double sumDfs() {
		double sumdfs=orderformrepository.sumDfs();
		return sumdfs;
	}
	public List<OrderForm> findByOrderNumber(int ordernum){
		return orderformrepository.findByOrderNumber(ordernum);
	}
	public List<OrderForm> findByBsName(String bsnum){
		return orderformrepository.findByBsName(bsnum);
	}
}
