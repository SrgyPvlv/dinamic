package com.example.sbkafka.Controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.annotation.SessionScope;

import com.example.sbkafka.Model.OrderForm;
import com.example.sbkafka.Service.BsListService;
import com.example.sbkafka.Service.OrderFormService;
import com.example.sbkafka.Service.PriceService;
import java.time.*;
import java.time.format.DateTimeFormatter;

//расчет заказа (сметный расчет), запись заказа в БД

@Controller
@SessionScope
public class MainController {
	
	Double transport,diffTimeHours,diffTimeDay,orderPrice,outGoPrice;
	double timeHoursPrice;
	double timeDayPrice;
	double transPrice1;
	double jeep1;
	static String outputFileName;
		
	@Autowired
	private PriceService priceService;
	
	@Autowired
	private OrderFormService orderformservice;
	
	@Autowired
	private BsListService bsListService;
	
	@GetMapping("/calcOrder")
    public String showOrder() {
		
        return "index";
    }

	@PostMapping("/calcOrder") //расчет заказа (сметный расчет)
    public String saveOrder(@RequestParam(name="id",required=false,defaultValue="-10") int id, @RequestParam(value="ordernumber") int orderNumber, @RequestParam(value="bsnumber") String bsNumber,//
    		@RequestParam(value="start") String timeStart,@RequestParam(value="end") String timeEnd,//
    		@RequestParam(value="distance") String orderDistance,//
    		@RequestParam(value="dgutype") int dguType,@RequestParam(value="jeep") int jeepYesNo,//
    		@RequestParam(value="worktype") String workType,@RequestParam(value="owener") String owenerType,
    		@RequestParam(value="comment",required=false) String comment,Model model) throws ParseException {
		
		double TRANSPORT_PRICE = priceService.getById(44).getPricesValue();
		double JEEP_PRICE = priceService.getById(45).getPricesValue();
		Double kmPrice=TRANSPORT_PRICE,jeepPrice=JEEP_PRICE;
    	Double trans=calcTransport(orderDistance,kmPrice);
    	Double jeep=calcJeep(jeepYesNo,jeepPrice);
    	Double diffTime=calcDiffTimeHours(timeStart,timeEnd);
    	Double diffDay=calcDiffTimeDay(timeStart, timeEnd);
    	Double calcHPrice=calcOrderHours(dguType,workType,jeep,diffTime,diffDay,trans,owenerType);
    	Double calcDPrice=calcOrderDays(dguType,workType,jeep,diffTime,diffDay,trans,owenerType);
    	Double calc=calcOrder(dguType,workType,jeep,diffTime,diffDay,trans,owenerType);
    	Double calcNds=calc*1.2;BigDecimal bd = new BigDecimal(calcNds).setScale(2, RoundingMode.HALF_UP);calcNds = bd.doubleValue();
    	Double nds=calc*0.2;BigDecimal bd1 = new BigDecimal(nds).setScale(2, RoundingMode.HALF_UP);nds = bd1.doubleValue();
    	
    	
    	LocalDateTime t1=LocalDateTime.parse(timeStart);
		LocalDateTime t2=LocalDateTime.parse(timeEnd);
		DateTimeFormatter formatter=DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    	String dateStart=formatter.format(t1);
    	String dateEnd=formatter.format(t2);
    	
    	String bsAddress=bsListService.findBsAddress(bsNumber);
    	
    	model.addAttribute("id", id);
    	model.addAttribute("orderNumber", orderNumber);
    	model.addAttribute("bsNumber", bsNumber);
    	model.addAttribute("dateStart", dateStart);
    	model.addAttribute("dateEnd", dateEnd);
    	model.addAttribute("orderDistance", orderDistance);
    	model.addAttribute("orderTransport", transPrice1);
    	model.addAttribute("orderDiffTime", diffTime);
    	model.addAttribute("orderDiffDay", diffDay);
    	model.addAttribute("orderCalcHPrice", calcHPrice);
    	model.addAttribute("orderCalcDPrice", calcDPrice);
    	model.addAttribute("orderCalc", calc);
    	model.addAttribute("orderCalcNds", calcNds);
    	model.addAttribute("orderNds", nds);
    	model.addAttribute("orderKmPrice", kmPrice);
    	model.addAttribute("orderOutGoPrice", outGoPrice);
    	model.addAttribute("jeepPrice", jeep1);
    	model.addAttribute("jeepYesNo", jeepYesNo);
    	model.addAttribute("jeepOnePrice", jeepPrice);
    	model.addAttribute("timeHoursPrice", timeHoursPrice);
    	model.addAttribute("timeDayPrice", timeDayPrice);
    	model.addAttribute("dguType", dguType);
    	model.addAttribute("workType", workType);
    	model.addAttribute("owenerType", owenerType);
    	model.addAttribute("bsAddress", bsAddress);
    	model.addAttribute("orderComment", comment);
        if (id==-10) {return "index";} else {return "editFormFinal";}
        }
public Double calcTransport(String orderdistance,double km) {
		
		double dist=Double.parseDouble(orderdistance);
		double transport=dist*km;
		BigDecimal bd = new BigDecimal(transport).setScale(2, RoundingMode.HALF_UP);
		transport = bd.doubleValue();
		
		return transport;
	}	
public Double calcJeep(int jeepYesNo,double jeepPrice) {
	
		double jeep=jeepYesNo*jeepPrice;
		
		return jeep;
	}	
public Double calcDiffTimeHours(String timeStart, String timeEnd) {
		
		LocalDateTime t1=LocalDateTime.parse(timeStart);
		LocalDateTime t2=LocalDateTime.parse(timeEnd);
		Duration difftime=Duration.between(t1, t2);
		double diffTimeMinutes=difftime.toMinutes();
		double diffTimeHours=diffTimeMinutes/60;
		BigDecimal bd = new BigDecimal(diffTimeHours).setScale(2, RoundingMode.HALF_UP);
		diffTimeHours = bd.doubleValue();
		
		return diffTimeHours;	
	}
public Double calcDiffTimeDay(String timeStart, String timeEnd) {
	
	LocalDateTime t1=LocalDateTime.parse(timeStart);
	LocalDateTime t2=LocalDateTime.parse(timeEnd);
	Duration difftime=Duration.between(t1, t2);
	double diffTimeMinutes=difftime.toMinutes();
	double diffTimeHours=diffTimeMinutes/60;
	if (diffTimeHours>=24) {
		diffTimeDay=diffTimeHours/24;
		BigDecimal bd = new BigDecimal(diffTimeDay).setScale(2, RoundingMode.HALF_UP);
		diffTimeDay = bd.doubleValue();} 
	else {diffTimeDay=0.0;}
	return diffTimeDay;	
}
public Double calcOrder(int dguType,String workType,double jeep,double tHours,double tDays,double transPrice,String owener) {
	
	double DGU3_CONTRACTOR_TIMEHOURES_WORKALARM = priceService.getById(2).getPricesValue();
	double DGU8_CONTRACTOR_TIMEHOURES_WORKALARM = priceService.getById(3).getPricesValue();
	double DGU16_CONTRACTOR_TIMEHOURES_WORKALARM = priceService.getById(4).getPricesValue();

	double DGU3_CLIENT_TIMEHOURES_WORKALARM = priceService.getById(5).getPricesValue();
	double DGU8_CLIENT_TIMEHOURES_WORKALARM = priceService.getById(6).getPricesValue();
	double DGU16_CLIENT_TIMEHOURES_WORKALARM = priceService.getById(7).getPricesValue();

	double DGU3_CONTRACTOR_TIMEHOURES_WORKPLAN = priceService.getById(8).getPricesValue();
	double DGU8_CONTRACTOR_TIMEHOURES_WORKPLAN = priceService.getById(9).getPricesValue();
	double DGU16_CONTRACTOR_TIMEHOURES_WORKPLAN = priceService.getById(10).getPricesValue();

	double DGU3_CLIENT_TIMEHOURES_WORKPLAN = priceService.getById(11).getPricesValue();
	double DGU8_CLIENT_TIMEHOURES_WORKPLAN = priceService.getById(12).getPricesValue();
	double DGU16_CLIENT_TIMEHOURES_WORKPLAN = priceService.getById(13).getPricesValue();

	double DGU3_CONTRACTOR_TIMEDAYS1_WORKALARM = priceService.getById(14).getPricesValue();
	double DGU8_CONTRACTOR_TIMEDAYS1_WORKALARM = priceService.getById(15).getPricesValue();
	double DGU16_CONTRACTOR_TIMEDAYS1_WORKALARM = priceService.getById(16).getPricesValue();

	double DGU3_CLIENT_TIMEDAYS1_WORKALARM = priceService.getById(17).getPricesValue();
	double DGU8_CLIENT_TIMEDAYS1_WORKALARM = priceService.getById(18).getPricesValue();
	double DGU16_CLIENT_TIMEDAYS1_WORKALARM = priceService.getById(19).getPricesValue();

	double DGU3_CONTRACTOR_TIMEDAYS1_WORKPLAN = priceService.getById(20).getPricesValue();
	double DGU8_CONTRACTOR_TIMEDAYS1_WORKPLAN = priceService.getById(21).getPricesValue();
	double DGU16_CONTRACTOR_TIMEDAYS1_WORKPLAN = priceService.getById(22).getPricesValue();

	double DGU3_CLIENT_TIMEDAYS1_WORKPLAN = priceService.getById(23).getPricesValue();
	double DGU8_CLIENT_TIMEDAYS1_WORKPLAN = priceService.getById(24).getPricesValue();
	double DGU16_CLIENT_TIMEDAYS1_WORKPLAN = priceService.getById(25).getPricesValue();

	double DGU3_CONTRACTOR_TIMEDAYS3_WORKPLAN = priceService.getById(26).getPricesValue();
	double DGU8_CONTRACTOR_TIMEDAYS3_WORKPLAN = priceService.getById(27).getPricesValue();
	double DGU16_CONTRACTOR_TIMEDAYS3_WORKPLAN = priceService.getById(28).getPricesValue();

	double DGU3_CLIENT_TIMEDAYS3_WORKPLAN = priceService.getById(29).getPricesValue();
	double DGU8_CLIENT_TIMEDAYS3_WORKPLAN = priceService.getById(30).getPricesValue();
	double DGU16_CLIENT_TIMEDAYS3_WORKPLAN = priceService.getById(31).getPricesValue();

	double DGU3_CONTRACTOR_DEPARTURE = priceService.getById(32).getPricesValue();
	double DGU8_CONTRACTOR_DEPARTURE = priceService.getById(33).getPricesValue();
	double DGU16_CONTRACTOR_DEPARTURE = priceService.getById(34).getPricesValue();

	double DGU3_CLIENT_DEPARTURE = priceService.getById(35).getPricesValue();
	double DGU8_CLIENT_DEPARTURE = priceService.getById(36).getPricesValue();
	double DGU16_CLIENT_DEPARTURE = priceService.getById(37).getPricesValue();

	double DGU3_CONTRACTOR_DEPARTURE_FALSE = priceService.getById(38).getPricesValue();
	double DGU8_CONTRACTOR_DEPARTURE_FALSE = priceService.getById(39).getPricesValue();
	double DGU16_CONTRACTOR_DEPARTURE_FALSE = priceService.getById(40).getPricesValue();

	double DGU3_CLIENT_DEPARTURE_FALSE = priceService.getById(41).getPricesValue();
	double DGU8_CLIENT_DEPARTURE_FALSE = priceService.getById(42).getPricesValue();
	double DGU16_CLIENT_DEPARTURE_FALSE = priceService.getById(43).getPricesValue();
	
	int dguType1=dguType;
	String workType1=workType,owener1=owener;
	double tHours1=tHours,tDays1=tDays,orderPrice=0,outgo=0;
	jeep1=jeep;
	transPrice1=transPrice;
	if(owener1.equals("po")) {
	if (workType1.equals("emergency")){
		if(tDays1<1.0) {
			switch(dguType1) {case 3:timeHoursPrice=DGU3_CONTRACTOR_TIMEHOURES_WORKALARM;timeDayPrice=0;outgo=DGU3_CONTRACTOR_DEPARTURE;break;
			                  case 8:timeHoursPrice=DGU8_CONTRACTOR_TIMEHOURES_WORKALARM;timeDayPrice=0;outgo=DGU8_CONTRACTOR_DEPARTURE;break;
			                  case 16:timeHoursPrice=DGU16_CONTRACTOR_TIMEHOURES_WORKALARM;timeDayPrice=0;outgo=DGU16_CONTRACTOR_DEPARTURE;break;}}
				if(tDays1>=1.0&&tDays1<=3) {
					switch(dguType1) {case 3:timeHoursPrice=0;timeDayPrice=DGU3_CONTRACTOR_TIMEDAYS1_WORKALARM;outgo=DGU3_CONTRACTOR_DEPARTURE;break;
					                  case 8:timeHoursPrice=0;timeDayPrice=DGU8_CONTRACTOR_TIMEDAYS1_WORKALARM;outgo=DGU8_CONTRACTOR_DEPARTURE;break;
					                  case 16:timeHoursPrice=0;timeDayPrice=DGU16_CONTRACTOR_TIMEDAYS1_WORKALARM;outgo=DGU16_CONTRACTOR_DEPARTURE;break;}}
				if(tDays1>3) {
					timeHoursPrice=0;timeDayPrice=0;outgo=0;transPrice1=0;jeep1=0;
					}
	}
	
	if (workType1.equals("plan")){
		if(tDays1<1.0) {
	switch(dguType1) {case 3:timeHoursPrice=DGU3_CONTRACTOR_TIMEHOURES_WORKPLAN;timeDayPrice=0;outgo=0;break;
	                  case 8:timeHoursPrice=DGU8_CONTRACTOR_TIMEHOURES_WORKPLAN;timeDayPrice=0;outgo=0;break;
	                  case 16:timeHoursPrice=DGU16_CONTRACTOR_TIMEHOURES_WORKPLAN;timeDayPrice=0;outgo=0;break;}}
		if(tDays1>=1.0&&tDays1<=3) {
			switch(dguType1) {case 3:timeHoursPrice=0;timeDayPrice=DGU3_CONTRACTOR_TIMEDAYS1_WORKPLAN;outgo=0;break;
			                  case 8:timeHoursPrice=0;timeDayPrice=DGU8_CONTRACTOR_TIMEDAYS1_WORKPLAN;outgo=0;break;
			                  case 16:timeHoursPrice=0;timeDayPrice=DGU16_CONTRACTOR_TIMEDAYS1_WORKPLAN;outgo=0;break;}}
		if(tDays1>3) {
			switch(dguType1) {case 3:timeHoursPrice=0;timeDayPrice=DGU3_CONTRACTOR_TIMEDAYS3_WORKPLAN;outgo=0;break;
			                  case 8:timeHoursPrice=0;timeDayPrice=DGU8_CONTRACTOR_TIMEDAYS3_WORKPLAN;outgo=0;break;
			                  case 16:timeHoursPrice=0;timeDayPrice=DGU16_CONTRACTOR_TIMEDAYS3_WORKPLAN;outgo=0;break;}}
	}
	
	if (workType1.equals("planinitial")){
		if(tDays1<1.0) {
	switch(dguType1) {case 3:timeHoursPrice=DGU3_CONTRACTOR_TIMEHOURES_WORKPLAN;timeDayPrice=0;outgo=DGU3_CONTRACTOR_DEPARTURE;break;
	                  case 8:timeHoursPrice=DGU8_CONTRACTOR_TIMEHOURES_WORKPLAN;timeDayPrice=0;outgo=DGU8_CONTRACTOR_DEPARTURE;break;
	                  case 16:timeHoursPrice=DGU16_CONTRACTOR_TIMEHOURES_WORKPLAN;timeDayPrice=0;outgo=DGU16_CONTRACTOR_DEPARTURE;break;}}
		if(tDays1>=1.0&&tDays1<=3) {
			switch(dguType1) {case 3:timeHoursPrice=0;timeDayPrice=DGU3_CONTRACTOR_TIMEDAYS1_WORKPLAN;outgo=DGU3_CONTRACTOR_DEPARTURE;break;
			                  case 8:timeHoursPrice=0;timeDayPrice=DGU8_CONTRACTOR_TIMEDAYS1_WORKPLAN;outgo=DGU8_CONTRACTOR_DEPARTURE;break;
			                  case 16:timeHoursPrice=0;timeDayPrice=DGU16_CONTRACTOR_TIMEDAYS1_WORKPLAN;outgo=DGU16_CONTRACTOR_DEPARTURE;break;}}
		if(tDays1>3) {
			switch(dguType1) {case 3:timeHoursPrice=0;timeDayPrice=DGU3_CONTRACTOR_TIMEDAYS3_WORKPLAN;outgo=DGU3_CONTRACTOR_DEPARTURE;break;
			                  case 8:timeHoursPrice=0;timeDayPrice=DGU8_CONTRACTOR_TIMEDAYS3_WORKPLAN;outgo=DGU8_CONTRACTOR_DEPARTURE;break;
			                  case 16:timeHoursPrice=0;timeDayPrice=DGU16_CONTRACTOR_TIMEDAYS3_WORKPLAN;outgo=DGU16_CONTRACTOR_DEPARTURE;break;}}
	}

	if (workType1.equals("false")){
		switch(dguType1) {case 3:timeHoursPrice=0;timeDayPrice=0;outgo=DGU3_CONTRACTOR_DEPARTURE_FALSE;break;
                          case 8:timeHoursPrice=0;timeDayPrice=0;outgo=DGU8_CONTRACTOR_DEPARTURE_FALSE;break;
                          case 16:timeHoursPrice=0;timeDayPrice=0;outgo=DGU16_CONTRACTOR_DEPARTURE_FALSE;break;}}
	
	outGoPrice=outgo;
	orderPrice=timeHoursPrice*tHours1+timeDayPrice*tDays1+transPrice1+outgo+jeep1;
	BigDecimal bd = new BigDecimal(orderPrice).setScale(2, RoundingMode.HALF_UP);
	orderPrice = bd.doubleValue();}
	
	if(owener1.equals("mts")) {
		if (workType1.equals("emergency")){
			if(tDays1<1.0) {
				switch(dguType1) {case 3:timeHoursPrice=DGU3_CLIENT_TIMEHOURES_WORKALARM;timeDayPrice=0;outgo=DGU3_CLIENT_DEPARTURE;break;
				                  case 8:timeHoursPrice=DGU8_CLIENT_TIMEHOURES_WORKALARM;timeDayPrice=0;outgo=DGU8_CLIENT_DEPARTURE;break;
				                  case 16:timeHoursPrice=DGU16_CLIENT_TIMEHOURES_WORKALARM;timeDayPrice=0;outgo=DGU16_CLIENT_DEPARTURE;break;}}
					if(tDays1>=1.0&&tDays1<=3) {
						switch(dguType1) {case 3:timeHoursPrice=0;timeDayPrice=DGU3_CLIENT_TIMEDAYS1_WORKALARM;outgo=DGU3_CLIENT_DEPARTURE;break;
						                  case 8:timeHoursPrice=0;timeDayPrice=DGU8_CLIENT_TIMEDAYS1_WORKALARM;outgo=DGU8_CLIENT_DEPARTURE;break;
						                  case 16:timeHoursPrice=0;timeDayPrice=DGU16_CLIENT_TIMEDAYS1_WORKALARM;outgo=DGU16_CLIENT_DEPARTURE;break;}}
					if(tDays1>3) {
						timeHoursPrice=0;timeDayPrice=0;outgo=0;transPrice1=0;jeep1=0;
						}
		}
		if (workType1.equals("plan")){
			if(tDays1<1.0) {
		switch(dguType1) {case 3:timeHoursPrice=DGU3_CLIENT_TIMEHOURES_WORKPLAN;timeDayPrice=0;outgo=0;break;
		                  case 8:timeHoursPrice=DGU8_CLIENT_TIMEHOURES_WORKPLAN;timeDayPrice=0;outgo=0;break;
		                  case 16:timeHoursPrice=DGU16_CLIENT_TIMEHOURES_WORKPLAN;timeDayPrice=0;outgo=0;break;}}
			if(tDays1>=1.0&&tDays1<=3) {
				switch(dguType1) {case 3:timeHoursPrice=0;timeDayPrice=DGU3_CLIENT_TIMEDAYS1_WORKPLAN;outgo=0;break;
				                  case 8:timeHoursPrice=0;timeDayPrice=DGU8_CLIENT_TIMEDAYS1_WORKPLAN;outgo=0;break;
				                  case 16:timeHoursPrice=0;timeDayPrice=DGU16_CLIENT_TIMEDAYS1_WORKPLAN;outgo=0;break;}}
			if(tDays1>3) {
				switch(dguType1) {case 3:timeHoursPrice=0;timeDayPrice=DGU3_CLIENT_TIMEDAYS3_WORKPLAN;outgo=0;break;
				                  case 8:timeHoursPrice=0;timeDayPrice=DGU8_CLIENT_TIMEDAYS3_WORKPLAN;outgo=0;break;
				                  case 16:timeHoursPrice=0;timeDayPrice=DGU16_CLIENT_TIMEDAYS3_WORKPLAN;outgo=0;break;}}
		}
		
		if (workType1.equals("planinitial")){
			if(tDays1<1.0) {
		switch(dguType1) {case 3:timeHoursPrice=DGU3_CLIENT_TIMEHOURES_WORKPLAN;timeDayPrice=0;outgo=DGU3_CLIENT_DEPARTURE;break;
		                  case 8:timeHoursPrice=DGU8_CLIENT_TIMEHOURES_WORKPLAN;timeDayPrice=0;outgo=DGU8_CLIENT_DEPARTURE;break;
		                  case 16:timeHoursPrice=DGU16_CLIENT_TIMEHOURES_WORKPLAN;timeDayPrice=0;outgo=DGU16_CLIENT_DEPARTURE;break;}}
			if(tDays1>=1.0&&tDays1<=3) {
				switch(dguType1) {case 3:timeHoursPrice=0;timeDayPrice=DGU3_CLIENT_TIMEDAYS1_WORKPLAN;outgo=DGU3_CLIENT_DEPARTURE;break;
				                  case 8:timeHoursPrice=0;timeDayPrice=DGU8_CLIENT_TIMEDAYS1_WORKPLAN;outgo=DGU8_CLIENT_DEPARTURE;break;
				                  case 16:timeHoursPrice=0;timeDayPrice=DGU16_CLIENT_TIMEDAYS1_WORKPLAN;outgo=DGU16_CLIENT_DEPARTURE;break;}}
			if(tDays1>3) {
				switch(dguType1) {case 3:timeHoursPrice=0;timeDayPrice=DGU3_CLIENT_TIMEDAYS3_WORKPLAN;outgo=DGU3_CLIENT_DEPARTURE;break;
				                  case 8:timeHoursPrice=0;timeDayPrice=DGU8_CLIENT_TIMEDAYS3_WORKPLAN;outgo=DGU8_CLIENT_DEPARTURE;break;
				                  case 16:timeHoursPrice=0;timeDayPrice=DGU16_CLIENT_TIMEDAYS3_WORKPLAN;outgo=DGU16_CLIENT_DEPARTURE;break;}}
		}

		if (workType1.equals("false")){
			switch(dguType1) {case 3:timeHoursPrice=0;timeDayPrice=0;outgo=DGU3_CLIENT_DEPARTURE_FALSE;break;
	                          case 8:timeHoursPrice=0;timeDayPrice=0;outgo=DGU8_CLIENT_DEPARTURE_FALSE;break;
	                          case 16:timeHoursPrice=0;timeDayPrice=0;outgo=DGU16_CLIENT_DEPARTURE_FALSE;break;}}
		
		outGoPrice=outgo;
		orderPrice=timeHoursPrice*tHours1+timeDayPrice*tDays1+transPrice1+outgo+jeep1;
		BigDecimal bd = new BigDecimal(orderPrice).setScale(2, RoundingMode.HALF_UP);
		orderPrice = bd.doubleValue();
		}
	return orderPrice;
}
public Double calcOrderHours(int dguType,String workType,double jeep,double tHours,double tDays,double transPrice,String owener) {
	
	double DGU3_CONTRACTOR_TIMEHOURES_WORKALARM = priceService.getById(2).getPricesValue();
	double DGU8_CONTRACTOR_TIMEHOURES_WORKALARM = priceService.getById(3).getPricesValue();
	double DGU16_CONTRACTOR_TIMEHOURES_WORKALARM = priceService.getById(4).getPricesValue();

	double DGU3_CLIENT_TIMEHOURES_WORKALARM = priceService.getById(5).getPricesValue();
	double DGU8_CLIENT_TIMEHOURES_WORKALARM = priceService.getById(6).getPricesValue();
	double DGU16_CLIENT_TIMEHOURES_WORKALARM = priceService.getById(7).getPricesValue();

	double DGU3_CONTRACTOR_TIMEHOURES_WORKPLAN = priceService.getById(8).getPricesValue();
	double DGU8_CONTRACTOR_TIMEHOURES_WORKPLAN = priceService.getById(9).getPricesValue();
	double DGU16_CONTRACTOR_TIMEHOURES_WORKPLAN = priceService.getById(10).getPricesValue();

	double DGU3_CLIENT_TIMEHOURES_WORKPLAN = priceService.getById(11).getPricesValue();
	double DGU8_CLIENT_TIMEHOURES_WORKPLAN = priceService.getById(12).getPricesValue();
	double DGU16_CLIENT_TIMEHOURES_WORKPLAN = priceService.getById(13).getPricesValue();

	double DGU3_CONTRACTOR_DEPARTURE = priceService.getById(32).getPricesValue();
	double DGU8_CONTRACTOR_DEPARTURE = priceService.getById(33).getPricesValue();
	double DGU16_CONTRACTOR_DEPARTURE = priceService.getById(34).getPricesValue();

	double DGU3_CLIENT_DEPARTURE = priceService.getById(35).getPricesValue();
	double DGU8_CLIENT_DEPARTURE = priceService.getById(36).getPricesValue();
	double DGU16_CLIENT_DEPARTURE = priceService.getById(37).getPricesValue();

	double DGU3_CONTRACTOR_DEPARTURE_FALSE = priceService.getById(38).getPricesValue();
	double DGU8_CONTRACTOR_DEPARTURE_FALSE = priceService.getById(39).getPricesValue();
	double DGU16_CONTRACTOR_DEPARTURE_FALSE = priceService.getById(40).getPricesValue();

	double DGU3_CLIENT_DEPARTURE_FALSE = priceService.getById(41).getPricesValue();
	double DGU8_CLIENT_DEPARTURE_FALSE = priceService.getById(42).getPricesValue();
	double DGU16_CLIENT_DEPARTURE_FALSE = priceService.getById(43).getPricesValue();
	
	int dguType1=dguType;
	String workType1=workType,owener1=owener;
	double tHours1=tHours,tDays1=tDays,orderPriceHours=0,outgo=0;
	if(owener1.equals("po")) {
	if (workType1.equals("emergency")){
		if(tDays1<1.0) {
			switch(dguType1) {case 3:timeHoursPrice=DGU3_CONTRACTOR_TIMEHOURES_WORKALARM;outgo=DGU3_CONTRACTOR_DEPARTURE;break;
			                  case 8:timeHoursPrice=DGU8_CONTRACTOR_TIMEHOURES_WORKALARM;outgo=DGU8_CONTRACTOR_DEPARTURE;break;
			                  case 16:timeHoursPrice=DGU16_CONTRACTOR_TIMEHOURES_WORKALARM;outgo=DGU16_CONTRACTOR_DEPARTURE;break;}}
				if(tDays1>=1.0&&tDays1<=3) {
					switch(dguType1) {case 3:timeHoursPrice=0;outgo=DGU3_CONTRACTOR_DEPARTURE;break;
					                  case 8:timeHoursPrice=0;outgo=DGU8_CONTRACTOR_DEPARTURE;break;
					                  case 16:timeHoursPrice=0;outgo=DGU16_CONTRACTOR_DEPARTURE;break;}}
				if(tDays1>3) {
					timeHoursPrice=0;outgo=0;
					}
	}
	if (workType1.equals("plan")){
		if(tDays1<1.0) {
	switch(dguType1) {case 3:timeHoursPrice=DGU3_CONTRACTOR_TIMEHOURES_WORKPLAN;outgo=0;break;
	                  case 8:timeHoursPrice=DGU8_CONTRACTOR_TIMEHOURES_WORKPLAN;outgo=0;break;
	                  case 16:timeHoursPrice=DGU16_CONTRACTOR_TIMEHOURES_WORKPLAN;outgo=0;break;}}
		if(tDays1>=1.0&&tDays1<=3) {
			switch(dguType1) {case 3:timeHoursPrice=0;outgo=0;break;
			                  case 8:timeHoursPrice=0;outgo=0;break;
			                  case 16:timeHoursPrice=0;outgo=0;break;}}
		if(tDays1>3) {
			switch(dguType1) {case 3:timeHoursPrice=0;outgo=0;break;
			                  case 8:timeHoursPrice=0;outgo=0;break;
			                  case 16:timeHoursPrice=0;outgo=0;break;}}
	}
	
	if (workType1.equals("planinitial")){
		if(tDays1<1.0) {
	switch(dguType1) {case 3:timeHoursPrice=DGU3_CONTRACTOR_TIMEHOURES_WORKPLAN;outgo=DGU3_CONTRACTOR_DEPARTURE;break;
	                  case 8:timeHoursPrice=DGU8_CONTRACTOR_TIMEHOURES_WORKPLAN;outgo=DGU8_CONTRACTOR_DEPARTURE;break;
	                  case 16:timeHoursPrice=DGU16_CONTRACTOR_TIMEHOURES_WORKPLAN;outgo=DGU16_CONTRACTOR_DEPARTURE;break;}}
		if(tDays1>=1.0&&tDays1<=3) {
			switch(dguType1) {case 3:timeHoursPrice=0;outgo=DGU3_CONTRACTOR_DEPARTURE;break;
			                  case 8:timeHoursPrice=0;outgo=DGU8_CONTRACTOR_DEPARTURE;break;
			                  case 16:timeHoursPrice=0;outgo=DGU16_CONTRACTOR_DEPARTURE;break;}}
		if(tDays1>3) {
			switch(dguType1) {case 3:timeHoursPrice=0;outgo=DGU3_CONTRACTOR_DEPARTURE;break;
			                  case 8:timeHoursPrice=0;outgo=DGU8_CONTRACTOR_DEPARTURE;break;
			                  case 16:timeHoursPrice=0;outgo=DGU16_CONTRACTOR_DEPARTURE;break;}}
	}

	if (workType1.equals("false")){
		switch(dguType1) {case 3:timeHoursPrice=0;outgo=DGU3_CONTRACTOR_DEPARTURE_FALSE;break;
                          case 8:timeHoursPrice=0;outgo=DGU8_CONTRACTOR_DEPARTURE_FALSE;break;
                          case 16:timeHoursPrice=0;outgo=DGU16_CONTRACTOR_DEPARTURE_FALSE;break;}}
		
		outGoPrice=outgo;
		orderPriceHours=timeHoursPrice*tHours1;
		BigDecimal bd = new BigDecimal(orderPriceHours).setScale(2, RoundingMode.HALF_UP);
		orderPriceHours = bd.doubleValue();}
		
	if(owener1.equals("mts")) {
		if (workType1.equals("emergency")){
			if(tDays1<1.0) {
				switch(dguType1) {case 3:timeHoursPrice=DGU3_CLIENT_TIMEHOURES_WORKALARM;outgo=DGU3_CLIENT_DEPARTURE;break;
				                  case 8:timeHoursPrice=DGU8_CLIENT_TIMEHOURES_WORKALARM;outgo=DGU8_CLIENT_DEPARTURE;break;
				                  case 16:timeHoursPrice=DGU16_CLIENT_TIMEHOURES_WORKALARM;outgo=DGU16_CLIENT_DEPARTURE;break;}}
					if(tDays1>=1.0&&tDays1<=3) {
						switch(dguType1) {case 3:timeHoursPrice=0;outgo=DGU3_CLIENT_DEPARTURE;break;
						                  case 8:timeHoursPrice=0;outgo=DGU8_CLIENT_DEPARTURE;break;
						                  case 16:timeHoursPrice=0;outgo=DGU16_CLIENT_DEPARTURE;break;}}
					if(tDays1>3) {
						timeHoursPrice=0;outgo=0;
						}
		}
		if (workType1.equals("plan")){
			if(tDays1<1.0) {
		switch(dguType1) {case 3:timeHoursPrice=DGU3_CLIENT_TIMEHOURES_WORKPLAN;outgo=0;break;
		                  case 8:timeHoursPrice=DGU8_CLIENT_TIMEHOURES_WORKPLAN;outgo=0;break;
		                  case 16:timeHoursPrice=DGU16_CLIENT_TIMEHOURES_WORKPLAN;outgo=0;break;}}
			if(tDays1>=1.0&&tDays1<=3) {
				switch(dguType1) {case 3:timeHoursPrice=0;outgo=0;break;
				                  case 8:timeHoursPrice=0;outgo=0;break;
				                  case 16:timeHoursPrice=0;outgo=0;break;}}
			if(tDays1>3) {
				switch(dguType1) {case 3:timeHoursPrice=0;outgo=0;break;
				                  case 8:timeHoursPrice=0;outgo=0;break;
				                  case 16:timeHoursPrice=0;outgo=0;break;}}
		}
		
		if (workType1.equals("planinitial")){
			if(tDays1<1.0) {
		switch(dguType1) {case 3:timeHoursPrice=DGU3_CLIENT_TIMEHOURES_WORKPLAN;outgo=DGU3_CLIENT_DEPARTURE;break;
		                  case 8:timeHoursPrice=DGU8_CLIENT_TIMEHOURES_WORKPLAN;outgo=DGU8_CLIENT_DEPARTURE;break;
		                  case 16:timeHoursPrice=DGU16_CLIENT_TIMEHOURES_WORKPLAN;outgo=DGU16_CLIENT_DEPARTURE;break;}}
			if(tDays1>=1.0&&tDays1<=3) {
				switch(dguType1) {case 3:timeHoursPrice=0;outgo=DGU3_CLIENT_DEPARTURE;break;
				                  case 8:timeHoursPrice=0;outgo=DGU8_CLIENT_DEPARTURE;break;
				                  case 16:timeHoursPrice=0;outgo=DGU16_CLIENT_DEPARTURE;break;}}
			if(tDays1>3) {
				switch(dguType1) {case 3:timeHoursPrice=0;outgo=DGU3_CLIENT_DEPARTURE;break;
				                  case 8:timeHoursPrice=0;outgo=DGU8_CLIENT_DEPARTURE;break;
				                  case 16:timeHoursPrice=0;outgo=DGU16_CLIENT_DEPARTURE;break;}}
		}

		if (workType1.equals("false")){
			switch(dguType1) {case 3:timeHoursPrice=0;outgo=DGU3_CLIENT_DEPARTURE_FALSE;break;
	                          case 8:timeHoursPrice=0;outgo=DGU8_CLIENT_DEPARTURE_FALSE;break;
	                          case 16:timeHoursPrice=0;outgo=DGU16_CLIENT_DEPARTURE_FALSE;break;}}
	
	outGoPrice=outgo;
	orderPriceHours=timeHoursPrice*tHours1;
	BigDecimal bd = new BigDecimal(orderPriceHours).setScale(2, RoundingMode.HALF_UP);
	orderPriceHours = bd.doubleValue();}
	return orderPriceHours;
}
public Double calcOrderDays(int dguType,String workType,double jeep,double tHours,double tDays,double transPrice,String owener) {
	
	double DGU3_CONTRACTOR_TIMEDAYS1_WORKALARM = priceService.getById(14).getPricesValue();
	double DGU8_CONTRACTOR_TIMEDAYS1_WORKALARM = priceService.getById(15).getPricesValue();
	double DGU16_CONTRACTOR_TIMEDAYS1_WORKALARM = priceService.getById(16).getPricesValue();

	double DGU3_CLIENT_TIMEDAYS1_WORKALARM = priceService.getById(17).getPricesValue();
	double DGU8_CLIENT_TIMEDAYS1_WORKALARM = priceService.getById(18).getPricesValue();
	double DGU16_CLIENT_TIMEDAYS1_WORKALARM = priceService.getById(19).getPricesValue();

	double DGU3_CONTRACTOR_TIMEDAYS1_WORKPLAN = priceService.getById(20).getPricesValue();
	double DGU8_CONTRACTOR_TIMEDAYS1_WORKPLAN = priceService.getById(21).getPricesValue();
	double DGU16_CONTRACTOR_TIMEDAYS1_WORKPLAN = priceService.getById(22).getPricesValue();

	double DGU3_CLIENT_TIMEDAYS1_WORKPLAN = priceService.getById(23).getPricesValue();
	double DGU8_CLIENT_TIMEDAYS1_WORKPLAN = priceService.getById(24).getPricesValue();
	double DGU16_CLIENT_TIMEDAYS1_WORKPLAN = priceService.getById(25).getPricesValue();

	double DGU3_CONTRACTOR_TIMEDAYS3_WORKPLAN = priceService.getById(26).getPricesValue();
	double DGU8_CONTRACTOR_TIMEDAYS3_WORKPLAN = priceService.getById(27).getPricesValue();
	double DGU16_CONTRACTOR_TIMEDAYS3_WORKPLAN = priceService.getById(28).getPricesValue();

	double DGU3_CLIENT_TIMEDAYS3_WORKPLAN = priceService.getById(29).getPricesValue();
	double DGU8_CLIENT_TIMEDAYS3_WORKPLAN = priceService.getById(30).getPricesValue();
	double DGU16_CLIENT_TIMEDAYS3_WORKPLAN = priceService.getById(31).getPricesValue();

	double DGU3_CONTRACTOR_DEPARTURE = priceService.getById(32).getPricesValue();
	double DGU8_CONTRACTOR_DEPARTURE = priceService.getById(33).getPricesValue();
	double DGU16_CONTRACTOR_DEPARTURE = priceService.getById(34).getPricesValue();

	double DGU3_CLIENT_DEPARTURE = priceService.getById(35).getPricesValue();
	double DGU8_CLIENT_DEPARTURE = priceService.getById(36).getPricesValue();
	double DGU16_CLIENT_DEPARTURE = priceService.getById(37).getPricesValue();

	double DGU3_CONTRACTOR_DEPARTURE_FALSE = priceService.getById(38).getPricesValue();
	double DGU8_CONTRACTOR_DEPARTURE_FALSE = priceService.getById(39).getPricesValue();
	double DGU16_CONTRACTOR_DEPARTURE_FALSE = priceService.getById(40).getPricesValue();

	double DGU3_CLIENT_DEPARTURE_FALSE = priceService.getById(41).getPricesValue();
	double DGU8_CLIENT_DEPARTURE_FALSE = priceService.getById(42).getPricesValue();
	double DGU16_CLIENT_DEPARTURE_FALSE = priceService.getById(43).getPricesValue();
	
	int dguType1=dguType;
	String workType1=workType,owener1=owener;
	double tDays1=tDays,orderPriceDays=0,outgo=0;
	if(owener1.equals("po")) {
	if (workType1.equals("emergency")){
		if(tDays1<1.0) {
			switch(dguType1) {case 3:timeDayPrice=0;outgo=DGU3_CONTRACTOR_DEPARTURE;break;
			                  case 8:timeDayPrice=0;outgo=DGU8_CONTRACTOR_DEPARTURE;break;
			                  case 16:timeDayPrice=0;outgo=DGU16_CONTRACTOR_DEPARTURE;break;}}
				if(tDays1>=1.0&&tDays1<=3) {
					switch(dguType1) {case 3:timeDayPrice=DGU3_CONTRACTOR_TIMEDAYS1_WORKALARM;outgo=DGU3_CONTRACTOR_DEPARTURE;break;
					                  case 8:timeDayPrice=DGU8_CONTRACTOR_TIMEDAYS1_WORKALARM;outgo=DGU8_CONTRACTOR_DEPARTURE;break;
					                  case 16:timeDayPrice=DGU16_CONTRACTOR_TIMEDAYS1_WORKALARM;outgo=DGU16_CONTRACTOR_DEPARTURE;break;}}
				if(tDays1>3) {
					timeDayPrice=0;outgo=0;
					}
	}
	if (workType1.equals("plan")){
		if(tDays1<1.0) {
	switch(dguType1) {case 3:timeDayPrice=0;outgo=0;break;
	                  case 8:timeDayPrice=0;outgo=0;break;
	                  case 16:timeDayPrice=0;outgo=0;break;}}
		if(tDays1>=1.0&&tDays1<=3) {
			switch(dguType1) {case 3:timeDayPrice=DGU3_CONTRACTOR_TIMEDAYS1_WORKPLAN;outgo=0;break;
			                  case 8:timeDayPrice=DGU8_CONTRACTOR_TIMEDAYS1_WORKPLAN;outgo=0;break;
			                  case 16:timeDayPrice=DGU16_CONTRACTOR_TIMEDAYS1_WORKPLAN;outgo=0;break;}}
		if(tDays1>3) {
			switch(dguType1) {case 3:timeDayPrice=DGU3_CONTRACTOR_TIMEDAYS3_WORKPLAN;outgo=0;break;
			                  case 8:timeDayPrice=DGU8_CONTRACTOR_TIMEDAYS3_WORKPLAN;outgo=0;break;
			                  case 16:timeDayPrice=DGU16_CONTRACTOR_TIMEDAYS3_WORKPLAN;outgo=0;break;}}
	}
	
	if (workType1.equals("planinitial")){
		if(tDays1<1.0) {
	switch(dguType1) {case 3:timeDayPrice=0;outgo=DGU3_CONTRACTOR_DEPARTURE;break;
	                  case 8:timeDayPrice=0;outgo=DGU8_CONTRACTOR_DEPARTURE;break;
	                  case 16:timeDayPrice=0;outgo=DGU16_CONTRACTOR_DEPARTURE;break;}}
		if(tDays1>=1.0&&tDays1<=3) {
			switch(dguType1) {case 3:timeDayPrice=DGU3_CONTRACTOR_TIMEDAYS1_WORKPLAN;outgo=DGU3_CONTRACTOR_DEPARTURE;break;
			                  case 8:timeDayPrice=DGU8_CONTRACTOR_TIMEDAYS1_WORKPLAN;outgo=DGU8_CONTRACTOR_DEPARTURE;break;
			                  case 16:timeDayPrice=DGU16_CONTRACTOR_TIMEDAYS1_WORKPLAN;outgo=DGU16_CONTRACTOR_DEPARTURE;break;}}
		if(tDays1>3) {
			switch(dguType1) {case 3:timeDayPrice=DGU3_CONTRACTOR_TIMEDAYS3_WORKPLAN;outgo=DGU3_CONTRACTOR_DEPARTURE;break;
			                  case 8:timeDayPrice=DGU8_CONTRACTOR_TIMEDAYS3_WORKPLAN;outgo=DGU8_CONTRACTOR_DEPARTURE;break;
			                  case 16:timeDayPrice=DGU16_CONTRACTOR_TIMEDAYS3_WORKPLAN;outgo=DGU16_CONTRACTOR_DEPARTURE;break;}}
	}

	if (workType1.equals("false")){
		switch(dguType1) {case 3:timeDayPrice=0;outgo=DGU3_CONTRACTOR_DEPARTURE_FALSE;break;
                          case 8:timeDayPrice=0;outgo=DGU8_CONTRACTOR_DEPARTURE_FALSE;break;
                          case 16:timeDayPrice=0;outgo=DGU16_CONTRACTOR_DEPARTURE_FALSE;break;}}
		outGoPrice=outgo;
		orderPriceDays=timeDayPrice*tDays1;
		BigDecimal bd = new BigDecimal(orderPriceDays).setScale(2, RoundingMode.HALF_UP);
		orderPriceDays = bd.doubleValue();
		}
		
	if(owener1.equals("mts")) {
		if (workType1.equals("emergency")){
			if(tDays1<1.0) {
				switch(dguType1) {case 3:timeDayPrice=0;outgo=DGU3_CLIENT_DEPARTURE;break;
				                  case 8:timeDayPrice=0;outgo=DGU8_CLIENT_DEPARTURE;break;
				                  case 16:timeDayPrice=0;outgo=DGU16_CLIENT_DEPARTURE;break;}}
					if(tDays1>=1.0&&tDays1<=3) {
						switch(dguType1) {case 3:timeDayPrice=DGU3_CLIENT_TIMEDAYS1_WORKALARM;outgo=DGU3_CLIENT_DEPARTURE;break;
						                  case 8:timeDayPrice=DGU8_CLIENT_TIMEDAYS1_WORKALARM;outgo=DGU8_CLIENT_DEPARTURE;break;
						                  case 16:timeDayPrice=DGU16_CLIENT_TIMEDAYS1_WORKALARM;outgo=DGU16_CLIENT_DEPARTURE;break;}}
					if(tDays1>3) {
						timeDayPrice=0;outgo=0;
						}
		}
		if (workType1.equals("plan")){
			if(tDays1<1.0) {
		switch(dguType1) {case 3:timeDayPrice=0;outgo=0;break;
		                  case 8:timeDayPrice=0;outgo=0;break;
		                  case 16:timeDayPrice=0;outgo=0;break;}}
			if(tDays1>=1.0&&tDays1<=3) {
				switch(dguType1) {case 3:timeDayPrice=DGU3_CLIENT_TIMEDAYS1_WORKPLAN;outgo=0;break;
				                  case 8:timeDayPrice=DGU8_CLIENT_TIMEDAYS1_WORKPLAN;outgo=0;break;
				                  case 16:timeDayPrice=DGU16_CLIENT_TIMEDAYS1_WORKPLAN;outgo=0;break;}}
			if(tDays1>3) {
				switch(dguType1) {case 3:timeDayPrice=DGU3_CLIENT_TIMEDAYS3_WORKPLAN;outgo=0;break;
				                  case 8:timeDayPrice=DGU8_CLIENT_TIMEDAYS3_WORKPLAN;outgo=0;break;
				                  case 16:timeDayPrice=DGU16_CLIENT_TIMEDAYS3_WORKPLAN;outgo=0;break;}}
		}
		
		if (workType1.equals("planinitial")){
			if(tDays1<1.0) {
		switch(dguType1) {case 3:timeDayPrice=0;outgo=DGU3_CLIENT_DEPARTURE;break;
		                  case 8:timeDayPrice=0;outgo=DGU8_CLIENT_DEPARTURE;break;
		                  case 16:timeDayPrice=0;outgo=DGU16_CLIENT_DEPARTURE;break;}}
			if(tDays1>=1.0&&tDays1<=3) {
				switch(dguType1) {case 3:timeDayPrice=DGU3_CLIENT_TIMEDAYS1_WORKPLAN;outgo=DGU3_CLIENT_DEPARTURE;break;
				                  case 8:timeDayPrice=DGU8_CLIENT_TIMEDAYS1_WORKPLAN;outgo=DGU8_CLIENT_DEPARTURE;break;
				                  case 16:timeDayPrice=DGU16_CLIENT_TIMEDAYS1_WORKPLAN;outgo=DGU16_CLIENT_DEPARTURE;break;}}
			if(tDays1>3) {
				switch(dguType1) {case 3:timeDayPrice=DGU3_CLIENT_TIMEDAYS3_WORKPLAN;outgo=DGU3_CLIENT_DEPARTURE;break;
				                  case 8:timeDayPrice=DGU8_CLIENT_TIMEDAYS3_WORKPLAN;outgo=DGU8_CLIENT_DEPARTURE;break;
				                  case 16:timeDayPrice=DGU16_CLIENT_TIMEDAYS3_WORKPLAN;outgo=DGU16_CLIENT_DEPARTURE;break;}}
		}

		if (workType1.equals("false")){
			switch(dguType1) {case 3:timeDayPrice=0;outgo=DGU3_CLIENT_DEPARTURE_FALSE;break;
	                          case 8:timeDayPrice=0;outgo=DGU8_CLIENT_DEPARTURE_FALSE;break;
	                          case 16:timeDayPrice=0;outgo=DGU16_CLIENT_DEPARTURE_FALSE;break;}}
			
			outGoPrice=outgo;
			orderPriceDays=timeDayPrice*tDays1;
			BigDecimal bd = new BigDecimal(orderPriceDays).setScale(2, RoundingMode.HALF_UP);
			orderPriceDays = bd.doubleValue();
			}
	
	return orderPriceDays;
}
@PostMapping("/recordOrder") //запись заявки и расчетов в БД
public String recordOrder(@RequestParam("orderNumber") int orderNumber,@RequestParam("bsNumber") String bsNumber,
		@RequestParam("dateStart") String dateStart,@RequestParam("dateEnd") String dateEnd,
		@RequestParam("orderCalc") double orderCalc,@RequestParam("orderCalcNds") double orderCalcNds,
		@RequestParam("orderComment") String orderComment,
		@RequestParam("jeepPrice") Double jeepPrice,@RequestParam("jeepYesNo") Integer jeepYesNo,
		@RequestParam("jeepOnePrice") Double jeepOnePrice,@RequestParam("orderOutGoPrice") Double orderOutGoPrice,
		@RequestParam("orderCalcHPrice") Double orderCalcHPrice,@RequestParam("orderDiffTime") Double orderDiffTime,
		@RequestParam("timeHoursPrice") Double timeHoursPrice,@RequestParam("owenerType") String owenerType,
		@RequestParam("dguType") Integer dguType,@RequestParam("workType") String workType,
		@RequestParam("orderCalcDPrice") Double orderCalcDPrice,@RequestParam("orderDiffDay") Double orderDiffDay,
		@RequestParam("timeDayPrice") Double timeDayPrice,@RequestParam("orderTransport") Double orderTransport,
		@RequestParam("orderDistance") String orderDistance,@RequestParam("orderKmPrice") Double orderKmPrice,
		@RequestParam("orderNds") Double orderNds,Model model) {
	
OrderForm orderForm=new OrderForm(orderNumber,bsNumber,dateStart,dateEnd,orderCalc,orderCalcNds,orderComment,jeepPrice,jeepYesNo,jeepOnePrice,
		orderOutGoPrice,orderCalcHPrice,orderDiffTime,timeHoursPrice,owenerType,dguType,workType,orderCalcDPrice,
		orderDiffDay,timeDayPrice,orderTransport,orderDistance,orderKmPrice,orderNds);

orderformservice.saveOrderForm(orderForm);

    return "index";
}
}