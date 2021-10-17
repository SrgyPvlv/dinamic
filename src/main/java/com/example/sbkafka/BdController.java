package com.example.sbkafka;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BdController {
	
	int str;int numstr;
	
	
	@Autowired
	private OrderFormService orderformservice;
	
	@Autowired
	private PriceService priceService;
	
	@GetMapping("/bdShow") //получение инфо из бд заказов
	public String bdShow(@RequestParam(value="str",defaultValue = "0") int str,@RequestParam(value="numstr",defaultValue = "20") int numstr,Model model){

		ArrayList<Order> orderlistFile=new ArrayList<Order>();
		this.str=str;
		this.numstr=numstr;
		Page<OrderForm> orderlist=orderformservice.findAllWithPageAsc(this.str,this.numstr);
		
		for(OrderForm order:orderlist) {
			
            int id=order.getId();int ordernumber=order.getOrderNumber();String bsnumber=order.getBsNumber();
            String datestart=order.getDateStart();String dateend=order.getDateEnd();
            double calc=order.getCalc();double calcnds=order.getCalcNds();String comm=order.getComm();
            FileDB fileDB=order.getFileDB();
            String fileYesNo;
			if (fileDB==null){fileYesNo="Нет";}else {fileYesNo="Есть";}
            Order orderlistarray=new Order(id,ordernumber,bsnumber,datestart,dateend,calc,calcnds,comm,fileYesNo);
            orderlistFile.addAll(Arrays.asList(orderlistarray));
         }
		
		DecimalFormat dF = new DecimalFormat( "###,###.00" );
		    
		double sumorders=orderformservice.sumOfOrders(); String sumordersdF=dF.format(sumorders);//сумма заказов
		int countoforders=orderformservice.countOfOrders();//кол-во заказов
		double orderslimit=priceService.getById(46).getPricesValue(); String orderslimitdF=dF.format(orderslimit);//лимит по деньгам
		double free=orderslimit-sumorders; String freedF=dF.format(free);//остаток денег
		
		//получение цен (6 шт.)ложных вызовов из таблицы price
		double f1=priceService.getById(38).getPricesValue();
		double f2=priceService.getById(39).getPricesValue();
		double f3=priceService.getById(40).getPricesValue();
		double f4=priceService.getById(41).getPricesValue();
		double f5=priceService.getById(42).getPricesValue();
		double f6=priceService.getById(43).getPricesValue();
		
		double minorder=orderformservice.minOfOrder(); String minorderdF=dF.format(minorder);//минимальный заказ
		double maxorder=orderformservice.maxOfOrder(); String maxorderdF=dF.format(maxorder);//максимальный заказ
		double avgorder=orderformservice.avgOfOrder(); String avgorderdF=dF.format(avgorder);//средняя сумма заказа
		int countfalse=orderformservice.countOfFalseOut(f1, f2, f3, f4, f5, f6);//кол-во ложных выездов до КАД
		double sumfalse=orderformservice.sumOfFalseOut(f1, f2, f3, f4, f5, f6); String sumfalsedF=dF.format(sumfalse);//затраты на ложные выезды до КАД
		int countdfs=orderformservice.countDfs();//кол-во заказов ДФС
		double sumdfs=orderformservice.sumDfs(); String sumdfsdF=dF.format(sumdfs);//затраты на заказы ДФС
		
		model.addAttribute("orderlist", orderlistFile);
		model.addAttribute("sumordersdF", sumordersdF);
		model.addAttribute("countoforders", countoforders);
		model.addAttribute("orderslimitdF", orderslimitdF);
		model.addAttribute("freedF", freedF);
		model.addAttribute("minorderdF", minorderdF);
		model.addAttribute("maxorderdF", maxorderdF);
		model.addAttribute("avgorderdF", avgorderdF);
		model.addAttribute("countfalse", countfalse);
		model.addAttribute("sumfalsedF", sumfalsedF);
		model.addAttribute("countdfs", countdfs);
		model.addAttribute("sumdfsdF", sumdfsdF);
		model.addAttribute("str", this.str);
		model.addAttribute("numstr", this.numstr);
		
		
		return "orderBD";
		
	}
	
	@GetMapping("admin/bdEdit") //получение инфо из бд заказов с возможностью перехода к редактированию
	public String bdEdit(@RequestParam(value="str",defaultValue = "0") int str,@RequestParam(value="numstr",defaultValue = "20") int numstr,Model model){
				
		//List<OrderForm> orderlist=orderformservice.findAllByOrderOrderNumberAsc();
		this.str=str;
		this.numstr=numstr;
		Page<OrderForm> orderlist=orderformservice.findAllWithPageAsc(this.str,this.numstr);
		model.addAttribute("orderlist", orderlist);
		model.addAttribute("str", this.str);
		model.addAttribute("numstr", this.numstr);
		
		return "orderBdEdit";
	}
	
	@GetMapping("/bdEdit") // переход на форму редактирования заказа
	public String edit(@RequestParam("id") int id, Model model) {
	
	OrderForm order=orderformservice.getById(id);
	model.addAttribute("order", order);
	   
	   return "editForm";
	}
	
	@PostMapping("/orderEdit") // редактирование заказа
	public String orderEdit(@RequestParam("id2") int id, @RequestParam("ordernumber2") int ordernumber,@RequestParam("bsnumber2") String bsnumber,//
	@RequestParam("start2") String datestart,@RequestParam("end2") String dateend,//
	@RequestParam("ordercalc2") double calc,@RequestParam("ordercalcnds2") double calcnds,//
	@RequestParam("comm2") String comm) {
	
	orderformservice.editOrderForm(id, ordernumber, bsnumber, datestart, dateend, calc, calcnds, comm);
	   
	   return "okEdit";
	}
	
	@PostMapping("/bdDelete") // удаление заказа
	public String delete(@RequestParam("id") int id,Model model) {
	
	orderformservice.deleteById(id);
	
	String title="Удаление Заказа";
	String note="Заказ удален!";
	model.addAttribute("title", title);
	model.addAttribute("note", note);
	
	   return "okDone";
	}
	
	@GetMapping("/findByOrderNumber") // поиск по № Заказа
	public String findByOrderNumber(@RequestParam("orderNumberSearch") int orderNumberSearch, Model model)throws IOException{
		
		ArrayList<Order> orderlistFile=new ArrayList<Order>();
		List<OrderForm> orderlist=orderformservice.findByOrderNumber(orderNumberSearch);
		
for(OrderForm order:orderlist) {
			
            int id=order.getId();int ordernumber=order.getOrderNumber();String bsnumber=order.getBsNumber();
            String datestart=order.getDateStart();String dateend=order.getDateEnd();
            double calc=order.getCalc();double calcnds=order.getCalcNds();String comm=order.getComm();
            FileDB fileDB=order.getFileDB();
            String fileYesNo;
			if (fileDB==null){fileYesNo="Нет";}else {fileYesNo="Есть";}
            Order orderlistarray=new Order(id,ordernumber,bsnumber,datestart,dateend,calc,calcnds,comm,fileYesNo);
            orderlistFile.addAll(Arrays.asList(orderlistarray));
         }
		
		DecimalFormat dF = new DecimalFormat( "###,###.00" );
		    
		double sumorders=orderformservice.sumOfOrders(); String sumordersdF=dF.format(sumorders);//сумма заказов
		int countoforders=orderformservice.countOfOrders();//кол-во заказов
		double orderslimit=priceService.getById(46).getPricesValue(); String orderslimitdF=dF.format(orderslimit);//лимит по деньгам
		double free=orderslimit-sumorders; String freedF=dF.format(free);//остаток денег
		
		//получение цен (6 шт.)ложных вызовов из таблицы price
		double f1=priceService.getById(38).getPricesValue();
		double f2=priceService.getById(39).getPricesValue();
		double f3=priceService.getById(40).getPricesValue();
		double f4=priceService.getById(41).getPricesValue();
		double f5=priceService.getById(42).getPricesValue();
		double f6=priceService.getById(43).getPricesValue();
		
		double minorder=orderformservice.minOfOrder(); String minorderdF=dF.format(minorder);//минимальный заказ
		double maxorder=orderformservice.maxOfOrder(); String maxorderdF=dF.format(maxorder);//максимальный заказ
		double avgorder=orderformservice.avgOfOrder(); String avgorderdF=dF.format(avgorder);//средняя сумма заказа
		int countfalse=orderformservice.countOfFalseOut(f1, f2, f3, f4, f5, f6);//кол-во ложных выездов до КАД
		double sumfalse=orderformservice.sumOfFalseOut(f1, f2, f3, f4, f5, f6); String sumfalsedF=dF.format(sumfalse);//затраты на ложные выезды до КАД
		int countdfs=orderformservice.countDfs();//кол-во заказов ДФС
		double sumdfs=orderformservice.sumDfs(); String sumdfsdF=dF.format(sumdfs);//затраты на заказы ДФС
		
		model.addAttribute("orderlist", orderlistFile);
		model.addAttribute("sumordersdF", sumordersdF);
		model.addAttribute("countoforders", countoforders);
		model.addAttribute("orderslimitdF", orderslimitdF);
		model.addAttribute("freedF", freedF);
		model.addAttribute("minorderdF", minorderdF);
		model.addAttribute("maxorderdF", maxorderdF);
		model.addAttribute("avgorderdF", avgorderdF);
		model.addAttribute("countfalse", countfalse);
		model.addAttribute("sumfalsedF", sumfalsedF);
		model.addAttribute("countdfs", countdfs);
		model.addAttribute("sumdfsdF", sumdfsdF);
		model.addAttribute("str", this.str);
		model.addAttribute("numstr", this.numstr);
		
		
		return "orderBD";
	}
	
	@GetMapping("/findByBsName")//поиск по номеру БС
	public String findByName(@RequestParam("bsNumberSearch") String bsNumberSearch, Model model)throws IOException{
		ArrayList<Order> orderlistFile=new ArrayList<Order>();
		List<OrderForm> orderlist=orderformservice.findByBsName(bsNumberSearch);
		
for(OrderForm order:orderlist) {
			
            int id=order.getId();int ordernumber=order.getOrderNumber();String bsnumber=order.getBsNumber();
            String datestart=order.getDateStart();String dateend=order.getDateEnd();
            double calc=order.getCalc();double calcnds=order.getCalcNds();String comm=order.getComm();
            FileDB fileDB=order.getFileDB();
            String fileYesNo;
			if (fileDB==null){fileYesNo="Нет";}else {fileYesNo="Есть";}
            Order orderlistarray=new Order(id,ordernumber,bsnumber,datestart,dateend,calc,calcnds,comm,fileYesNo);
            orderlistFile.addAll(Arrays.asList(orderlistarray));
         }
		
		DecimalFormat dF = new DecimalFormat( "###,###.00" );
		    
		double sumorders=orderformservice.sumOfOrders(); String sumordersdF=dF.format(sumorders);//сумма заказов
		int countoforders=orderformservice.countOfOrders();//кол-во заказов
		double orderslimit=priceService.getById(46).getPricesValue(); String orderslimitdF=dF.format(orderslimit);//лимит по деньгам
		double free=orderslimit-sumorders; String freedF=dF.format(free);//остаток денег
		
		//получение цен (6 шт.)ложных вызовов из таблицы price
		double f1=priceService.getById(38).getPricesValue();
		double f2=priceService.getById(39).getPricesValue();
		double f3=priceService.getById(40).getPricesValue();
		double f4=priceService.getById(41).getPricesValue();
		double f5=priceService.getById(42).getPricesValue();
		double f6=priceService.getById(43).getPricesValue();
		
		double minorder=orderformservice.minOfOrder(); String minorderdF=dF.format(minorder);//минимальный заказ
		double maxorder=orderformservice.maxOfOrder(); String maxorderdF=dF.format(maxorder);//максимальный заказ
		double avgorder=orderformservice.avgOfOrder(); String avgorderdF=dF.format(avgorder);//средняя сумма заказа
		int countfalse=orderformservice.countOfFalseOut(f1, f2, f3, f4, f5, f6);//кол-во ложных выездов до КАД
		double sumfalse=orderformservice.sumOfFalseOut(f1, f2, f3, f4, f5, f6); String sumfalsedF=dF.format(sumfalse);//затраты на ложные выезды до КАД
		int countdfs=orderformservice.countDfs();//кол-во заказов ДФС
		double sumdfs=orderformservice.sumDfs(); String sumdfsdF=dF.format(sumdfs);//затраты на заказы ДФС
		
		model.addAttribute("orderlist", orderlistFile);
		model.addAttribute("sumordersdF", sumordersdF);
		model.addAttribute("countoforders", countoforders);
		model.addAttribute("orderslimitdF", orderslimitdF);
		model.addAttribute("freedF", freedF);
		model.addAttribute("minorderdF", minorderdF);
		model.addAttribute("maxorderdF", maxorderdF);
		model.addAttribute("avgorderdF", avgorderdF);
		model.addAttribute("countfalse", countfalse);
		model.addAttribute("sumfalsedF", sumfalsedF);
		model.addAttribute("countdfs", countdfs);
		model.addAttribute("sumdfsdF", sumdfsdF);
		model.addAttribute("str", this.str);
		model.addAttribute("numstr", this.numstr);
		
		
		return "orderBD";
	}
}
