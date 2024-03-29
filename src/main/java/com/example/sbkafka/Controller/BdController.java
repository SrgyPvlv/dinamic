package com.example.sbkafka.Controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

import com.example.sbkafka.Model.Order;
import com.example.sbkafka.Model.OrderForm;
import com.example.sbkafka.Service.BsListService;
import com.example.sbkafka.Service.OrderFormService;
import com.example.sbkafka.Service.PriceService;

//работа с БД заказов

@Controller
public class BdController {
	
	int str;int numstr;
		
	@Autowired
	private OrderFormService orderformservice;
	
	@Autowired
	private PriceService priceService;
	
	@Autowired
	private BsListService bsListService;
	
	@GetMapping("/bdShow") //получение инфо из бд заказов
	public String bdShow(@RequestParam(value="str",defaultValue = "0") int str,@RequestParam(value="numstr",defaultValue = "20") int numstr,Model model){

		ArrayList<Order> orderlistFile=new ArrayList<Order>();
		this.str=str;
		this.numstr=numstr;
		Page<OrderForm> orderlist=orderformservice.findAllWithPageAsc(this.str,this.numstr);
		
		for(OrderForm order:orderlist) {
			
            int id=order.getId();int ordernumber=order.getOrdernumber();String bsnumber=order.getBsnumber();
            String datestart=order.getDatestart();String dateend=order.getDateend();
            double calc=order.getCalc();double calcnds=order.getCalcnds();String comm=order.getComm();
            
            Order orderlistarray=new Order(id,ordernumber,bsnumber,datestart,dateend,calc,calcnds,comm);
            orderlistFile.addAll(Arrays.asList(orderlistarray));
         }
		
		DecimalFormat dF = new DecimalFormat( "###,#00.00" );
		
		Double sumorders=orderformservice.sumOfOrders(); String sumordersdF=dF.format(sumorders.doubleValue());//сумма заказов
		int countoforders=orderformservice.countOfOrders();//кол-во заказов
		double orderslimit=priceService.getById(46).getPricesValue(); String orderslimitdF=dF.format(orderslimit);//лимит по деньгам
		double free=orderslimit-(sumorders.doubleValue()); String freedF=dF.format(free);//остаток денег
		
		//получение цен (6 шт.)ложных вызовов из таблицы price
		double f1=priceService.getById(38).getPricesValue();
		double f2=priceService.getById(39).getPricesValue();
		double f3=priceService.getById(40).getPricesValue();
		double f4=priceService.getById(41).getPricesValue();
		double f5=priceService.getById(42).getPricesValue();
		double f6=priceService.getById(43).getPricesValue();
		
		Double minorder=orderformservice.minOfOrder(); String minorderdF=dF.format(minorder.doubleValue());//минимальный заказ
		Double maxorder=orderformservice.maxOfOrder(); String maxorderdF=dF.format(maxorder.doubleValue());//максимальный заказ
		Double avgorder=orderformservice.avgOfOrder(); String avgorderdF=dF.format(avgorder.doubleValue());//средняя сумма заказа
		int countfalse=orderformservice.countOfFalseOut(f1, f2, f3, f4, f5, f6);//кол-во ложных выездов до КАД
		double sumfalse=orderformservice.sumOfFalseOut(f1, f2, f3, f4, f5, f6); String sumfalsedF=dF.format(sumfalse);//затраты на ложные выезды до КАД
		int countdfs=orderformservice.countDfs();//кол-во заказов ДФС
		double sumdfs=orderformservice.sumDfs(); String sumdfsdF=dF.format(sumdfs);//затраты на заказы ДФС
		
		double orderListSumm = 0;
		int orderListSize=0;
		
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
		model.addAttribute("orderListSize", orderListSize);
		model.addAttribute("orderListSumm", orderListSumm);		
		
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
	
	@GetMapping("/bdEditForm") // переход на форму редактирования заказа
	public String edit(@RequestParam("id") int id, Model model) {
	
	OrderForm order=orderformservice.getById(id);
	DateTimeFormatter formatter=DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
	LocalDateTime start=LocalDateTime.parse(order.getDatestart(),formatter);
	LocalDateTime end=LocalDateTime.parse(order.getDateend(),formatter);
	
	model.addAttribute("order", order);
	model.addAttribute("start", start);
	model.addAttribute("end", end);
	   
	   return "editForm";
	}
	
	@PostMapping("/orderEdit") // редактирование заказа с сохранением в БД
	public String orderEdit(@RequestParam("id") int id, @RequestParam("orderNumber") int orderNumber,@RequestParam("bsNumber") String bsNumber,
			@RequestParam("dateStart") String dateStart,@RequestParam("dateEnd") String dateEnd,
			@RequestParam("orderCalc") double orderCalc,@RequestParam("orderCalcNds") double orderCalcNds,
			@RequestParam("comment") String orderComment,
			@RequestParam("jeepPrice") Double jeepPrice,@RequestParam("jeepYesNo") Integer jeepYesNo,
			@RequestParam("jeepOnePrice") Double jeepOnePrice,@RequestParam("orderOutGoPrice") Double orderOutGoPrice,
			@RequestParam("orderCalcHPrice") Double orderCalcHPrice,@RequestParam("orderDiffTime") Double orderDiffTime,
			@RequestParam("timeHoursPrice") Double timeHoursPrice,@RequestParam("owenerType") String owenerType,
			@RequestParam("dguType") Integer dguType,@RequestParam("workType") String workType,
			@RequestParam("orderCalcDPrice") Double orderCalcDPrice,@RequestParam("orderDiffDay") Double orderDiffDay,
			@RequestParam("timeDayPrice") Double timeDayPrice,@RequestParam("orderTransport") Double orderTransport,
			@RequestParam("orderDistance") String orderDistance,@RequestParam("orderKmPrice") Double orderKmPrice,
			@RequestParam("orderNds") Double orderNds) {
	
	orderformservice.editOrderForm(id,orderNumber,bsNumber,dateStart,dateEnd,orderCalc,orderCalcNds,orderComment,jeepPrice,jeepYesNo,jeepOnePrice,
			orderOutGoPrice,orderCalcHPrice,orderDiffTime,timeHoursPrice,owenerType,dguType,workType,orderCalcDPrice,
			orderDiffDay,timeDayPrice,orderTransport,orderDistance,orderKmPrice,orderNds);
	   
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
		double orderListSumm = 0;
		
for(OrderForm order:orderlist) {
			
            int id=order.getId();int ordernumber=order.getOrdernumber();String bsnumber=order.getBsnumber();
            String datestart=order.getDatestart();String dateend=order.getDateend();
            double calc=order.getCalc();double calcnds=order.getCalcnds();String comm=order.getComm();
            
            Order orderlistarray=new Order(id,ordernumber,bsnumber,datestart,dateend,calc,calcnds,comm);
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
		
		int orderListSize=orderlist.size();
		if(orderlist.isEmpty()) {orderListSumm=0.00;} else {
        	orderListSumm=orderlist.stream().map(OrderForm::getCalc).reduce((x,y)->x+y).get().doubleValue();
        	BigDecimal bd = new BigDecimal(orderListSumm).setScale(2, RoundingMode.HALF_UP);
        	orderListSumm = bd.doubleValue();
        }
		
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
		model.addAttribute("orderListSize", orderListSize);
		model.addAttribute("orderListSumm", orderListSumm);
		
		
		return "orderBD";
	}
	
	@GetMapping("/findByBsName")//поиск по № БС
	public String findByName(@RequestParam("bsNumberSearch") String bsNumberSearch, Model model)throws IOException{
		ArrayList<Order> orderlistFile=new ArrayList<Order>();
		List<OrderForm> orderlist=orderformservice.findByBsName(bsNumberSearch);
		double orderListSumm = 0;
		
for(OrderForm order:orderlist) {
			
            int id=order.getId();int ordernumber=order.getOrdernumber();String bsnumber=order.getBsnumber();
            String datestart=order.getDatestart();String dateend=order.getDateend();
            double calc=order.getCalc();double calcnds=order.getCalcnds();String comm=order.getComm();
            
            Order orderlistarray=new Order(id,ordernumber,bsnumber,datestart,dateend,calc,calcnds,comm);
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
		
		int orderListSize=orderlist.size();
		if(orderlist.isEmpty()) {orderListSumm=0.00;} else {
        	orderListSumm=orderlist.stream().map(OrderForm::getCalc).reduce((x,y)->x+y).get().doubleValue();
        	BigDecimal bd = new BigDecimal(orderListSumm).setScale(2, RoundingMode.HALF_UP);
        	orderListSumm = bd.doubleValue();
        }
		
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
		model.addAttribute("orderListSize", orderListSize);
		model.addAttribute("orderListSumm", orderListSumm);
		
		return "orderBD";
	}
	
	@GetMapping("admin/findByOrderNumber") // поиск по № Заказа на странице админа
	public String findByOrderNumberAdmin(@RequestParam("orderNumberSearch") int orderNumberSearch, Model model)throws IOException{
		
		List<OrderForm> orderlist=orderformservice.findByOrderNumber(orderNumberSearch);
		
		model.addAttribute("orderlist", orderlist);
		model.addAttribute("str", this.str);
		model.addAttribute("numstr", this.numstr);
				
		return "orderBdEdit";
		
	}
	
	@GetMapping("admin/findByBsName") // поиск по № БС на странице админа
	public String findByNameAdmin(@RequestParam("bsNumberSearch") String bsNumberSearch, Model model)throws IOException{
		
		List<OrderForm> orderlist=orderformservice.findByBsName(bsNumberSearch);
		
		model.addAttribute("orderlist", orderlist);
		model.addAttribute("str", this.str);
		model.addAttribute("numstr", this.numstr);
				
		return "orderBdEdit";
	}
	@GetMapping("/admin/allOrdersDeleteShow") // переход на страницу предупреждения об удалении всех Заказов
	public String allOrdersDeleteShow() {
	   
	   return "allDeleteWarning";
	}
	
	@PostMapping("/superadmin/allOrdersDelete") // Осторожно!!! Удаление всех заказов из БД.
	public String allOrdersDelete() {
		
	    orderformservice.deleteAll();
		
		return "redirect:/admin/bdEdit";
	}
	
	@GetMapping("/showOrderPage") // показать страницу Заказа
	public String showOrderPage(@RequestParam("id") int id, Model model) {
	
	OrderForm order=orderformservice.getById(id);
	String bsNumber=order.getBsnumber();
	String bsAddress=bsListService.findBsAddress(bsNumber);
	model.addAttribute("order", order);
	model.addAttribute("bsAddress", bsAddress);
	   
	   return "orderPage";
	}
}
