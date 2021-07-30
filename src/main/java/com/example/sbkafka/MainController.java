package com.example.sbkafka;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

@Controller
public class MainController {
	String dateStart,dateEnd;
	int orderNumber;
	String bsNumber;
	Double calc,calcNds;
	String text;
	Double transport,diffTimeHours,diffTimeDay,orderPrice,outGoPrice,kmPrice=12.09,jeepPrice=6500.70;
	
	static String outputFileName;
	public static ArrayList<String> orderArray = new ArrayList<String>();
	
	public static ArrayList<OrderForm> orderFormArray = new ArrayList<OrderForm>();
	
	
	@GetMapping("/calcOrder")
    public String showOrder() {
		
        return "order";
    }

	@PostMapping("/calcOrder")
    public String saveOrder(@RequestParam(value="ordernumber") int orderNumber, @RequestParam(value="bsnumber") String bsNumber,//
    		@RequestParam(value="start") String timeStart,@RequestParam(value="end") String timeEnd,//
    		@RequestParam(value="distance") String orderDistance,//
    		@RequestParam(value="dgutype") int dguType,@RequestParam(value="jeep") int jeepYesNo,//
    		@RequestParam(value="worktype") String workType,@RequestParam(value="owener") String owenerType,Model model) throws ParseException {
 
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
		DateTimeFormatter formatter=DateTimeFormatter.ofPattern("dd-MM-yyyy-HH:mm");
    	String dateStart=formatter.format(t1);
    	String dateEnd=formatter.format(t2);
    	
    	    	    	
    	model.addAttribute("orderNumber", orderNumber);
    	model.addAttribute("bsNumber", bsNumber);
    	model.addAttribute("timeStart", dateStart);
    	model.addAttribute("timeEnd", dateEnd);
    	model.addAttribute("orderDistance", orderDistance);
    	model.addAttribute("orderTransport", trans);
    	model.addAttribute("orderDiffTime", diffTime);
    	model.addAttribute("orderDiffDay", diffDay);
    	model.addAttribute("orderCalcHPrice", calcHPrice);
    	model.addAttribute("orderCalcDPrice", calcDPrice);
    	model.addAttribute("orderCalc", calc);
    	model.addAttribute("orderCalcNds", calcNds);
    	model.addAttribute("orderNds", nds);
    	model.addAttribute("orderKmPrice", kmPrice);
    	model.addAttribute("orderOutGoPrice", outGoPrice);
    	model.addAttribute("jeepPrice", jeep);
            return "order";
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
	int dguType1=dguType;
	String workType1=workType,owener1=owener;
	double timeHoursPrice = 0,timeDayPrice = 0,tHours1=tHours,tDays1=tDays,orderPrice=0,transPrice1=transPrice,outgo=0,jeep1=jeep;
	if(owener1.equals("po")) {
	if (workType1.equals("emergency")){
		if(tDays1<1.0) {
			switch(dguType1) {case 3:timeHoursPrice=790.50;timeDayPrice=0;outgo=1674.00;break;
			                  case 8:timeHoursPrice=930.00;timeDayPrice=0;outgo=1860.00;break;
			                  case 16:timeHoursPrice=930.00;timeDayPrice=0;outgo=1860.00;break;}}
				if(tDays1>=1.0&&tDays1<3) {
					switch(dguType1) {case 3:timeHoursPrice=0;timeDayPrice=18972.00;outgo=1674.00;break;
					                  case 8:timeHoursPrice=0;timeDayPrice=19251.00;outgo=1860.00;break;
					                  case 16:timeHoursPrice=0;timeDayPrice=19251.00;outgo=1860.00;break;}}
				if(tDays1>=3) {
					timeHoursPrice=0;timeDayPrice=0;outgo=0;transPrice1=0;
					}
	}
	if (workType1.equals("plan")){
		if(tDays1<1.0) {
	switch(dguType1) {case 3:timeHoursPrice=744;timeDayPrice=0;outgo=1674.00;break;
	                  case 8:timeHoursPrice=864.90;timeDayPrice=0;outgo=1860.00;break;
	                  case 16:timeHoursPrice=864.90;timeDayPrice=0;outgo=1860.00;break;}}
		if(tDays1>=1.0&&tDays1<3) {
			switch(dguType1) {case 3:timeHoursPrice=0;timeDayPrice=18414;outgo=1674.00;break;
			                  case 8:timeHoursPrice=0;timeDayPrice=18972;outgo=1860.00;break;
			                  case 16:timeHoursPrice=0;timeDayPrice=18972;outgo=1860.00;break;}}
		if(tDays1>=3) {
			switch(dguType1) {case 3:timeHoursPrice=0;timeDayPrice=16740;outgo=1674.00;break;
			                  case 8:timeHoursPrice=0;timeDayPrice=17350.08;outgo=1860.00;break;
			                  case 16:timeHoursPrice=0;timeDayPrice=17350.08;outgo=1860.00;break;}}
	}

	if (workType1.equals("false")){
		switch(dguType1) {case 3:timeHoursPrice=0;timeDayPrice=0;outgo=1860.00;break;
                          case 8:timeHoursPrice=0;timeDayPrice=0;outgo=2157.60;break;
                          case 16:timeHoursPrice=0;timeDayPrice=0;outgo=2157.60;break;}}
	
	outGoPrice=outgo;
	orderPrice=timeHoursPrice*tHours1+timeDayPrice*tDays1+transPrice1+outgo+jeep1;
	BigDecimal bd = new BigDecimal(orderPrice).setScale(2, RoundingMode.HALF_UP);
	orderPrice = bd.doubleValue();}
	
	if(owener1.equals("mts")) {
		if (workType1.equals("emergency")){
			if(tDays1<1.0) {
				switch(dguType1) {case 3:timeHoursPrice=762.60;timeDayPrice=0;outgo=1674.00;break;
				                  case 8:timeHoursPrice=790.50;timeDayPrice=0;outgo=1860.00;break;
				                  case 16:timeHoursPrice=790.50;timeDayPrice=0;outgo=1860.00;break;}}
					if(tDays1>=1.0&&tDays1<3) {
						switch(dguType1) {case 3:timeHoursPrice=0;timeDayPrice=16507.50;outgo=1674.00;break;
						                  case 8:timeHoursPrice=0;timeDayPrice=17530.50;outgo=1860.00;break;
						                  case 16:timeHoursPrice=0;timeDayPrice=18190.80;outgo=1860.00;break;}}
					if(tDays1>=3) {
						timeHoursPrice=0;timeDayPrice=0;outgo=0;transPrice1=0;
						}
		}
		if (workType1.equals("plan")){
			if(tDays1<1.0) {
		switch(dguType1) {case 3:timeHoursPrice=744;timeDayPrice=0;outgo=1674.00;break;
		                  case 8:timeHoursPrice=744;timeDayPrice=0;outgo=1860.00;break;
		                  case 16:timeHoursPrice=744;timeDayPrice=0;outgo=1860.00;break;}}
			if(tDays1>=1.0&&tDays1<3) {
				switch(dguType1) {case 3:timeHoursPrice=0;timeDayPrice=16042.50;outgo=1674.00;break;
				                  case 8:timeHoursPrice=0;timeDayPrice=17065.50;outgo=1860.00;break;
				                  case 16:timeHoursPrice=0;timeDayPrice=17995.50;outgo=1860.00;break;}}
			if(tDays1>=3) {
				switch(dguType1) {case 3:timeHoursPrice=0;timeDayPrice=15624.00;outgo=1674.00;break;
				                  case 8:timeHoursPrice=0;timeDayPrice=16070.40;outgo=1860.00;break;
				                  case 16:timeHoursPrice=0;timeDayPrice=16070.40;outgo=1860.00;break;}}
		}

		if (workType1.equals("false")){
			switch(dguType1) {case 3:timeHoursPrice=0;timeDayPrice=0;outgo=1860.00;break;
	                          case 8:timeHoursPrice=0;timeDayPrice=0;outgo=2157.60;break;
	                          case 16:timeHoursPrice=0;timeDayPrice=0;outgo=2157.60;break;}}
		
		outGoPrice=outgo;
		orderPrice=timeHoursPrice*tHours1+timeDayPrice*tDays1+transPrice1+outgo+jeep1;
		BigDecimal bd = new BigDecimal(orderPrice).setScale(2, RoundingMode.HALF_UP);
		orderPrice = bd.doubleValue();
		}
	return orderPrice;
}
public Double calcOrderHours(int dguType,String workType,double jeep,double tHours,double tDays,double transPrice,String owener) {
	int dguType1=dguType;
	String workType1=workType,owener1=owener;
	double timeHoursPrice = 0,tHours1=tHours,tDays1=tDays,orderPriceHours=0,outgo=0;
	if(owener1.equals("po")) {
		if (workType1.equals("emergency")){
			if(tDays1<1.0) {
				switch(dguType1) {case 3:timeHoursPrice=790.50;outgo=1674.00;break;
				                  case 8:timeHoursPrice=930.00;outgo=1860.00;break;
				                  case 16:timeHoursPrice=930.00;outgo=1860.00;break;}}
					if(tDays1>=1.0&&tDays1<3) {
						switch(dguType1) {case 3:timeHoursPrice=0;outgo=1674.00;break;
						                  case 8:timeHoursPrice=0;outgo=1860.00;break;
						                  case 16:timeHoursPrice=0;outgo=1860.00;break;}}
					if(tDays1>=3) {
						timeHoursPrice=0;outgo=0;
						}
		}
		if (workType1.equals("plan")){
			if(tDays1<1.0) {
		switch(dguType1) {case 3:timeHoursPrice=744;outgo=1674.00;break;
		                  case 8:timeHoursPrice=864.90;outgo=1860.00;break;
		                  case 16:timeHoursPrice=864.90;outgo=1860.00;break;}}
			if(tDays1>=1.0&&tDays1<3) {
				switch(dguType1) {case 3:timeHoursPrice=0;outgo=1674.00;break;
				                  case 8:timeHoursPrice=0;outgo=1860.00;break;
				                  case 16:timeHoursPrice=0;outgo=1860.00;break;}}
			if(tDays1>=3) {
				switch(dguType1) {case 3:timeHoursPrice=0;outgo=1674.00;break;
				                  case 8:timeHoursPrice=0;outgo=1860.00;break;
				                  case 16:timeHoursPrice=0;outgo=1860.00;break;}}
		}

		if (workType1.equals("false")){
			switch(dguType1) {case 3:timeHoursPrice=0;outgo=1860.00;break;
	                          case 8:timeHoursPrice=0;outgo=2157.60;break;
	                          case 16:timeHoursPrice=0;outgo=2157.60;break;}}
		
		outGoPrice=outgo;
		orderPriceHours=timeHoursPrice*tHours1;
		BigDecimal bd = new BigDecimal(orderPriceHours).setScale(2, RoundingMode.HALF_UP);
		orderPriceHours = bd.doubleValue();}
		
		if(owener1.equals("mts")) {
			if (workType1.equals("emergency")){
				if(tDays1<1.0) {
					switch(dguType1) {case 3:timeHoursPrice=762.60;outgo=1674.00;break;
					                  case 8:timeHoursPrice=790.50;outgo=1860.00;break;
					                  case 16:timeHoursPrice=790.50;outgo=1860.00;break;}}
						if(tDays1>=1.0&&tDays1<3) {
							switch(dguType1) {case 3:timeHoursPrice=0;outgo=1674.00;break;
							                  case 8:timeHoursPrice=0;outgo=1860.00;break;
							                  case 16:timeHoursPrice=0;outgo=1860.00;break;}}
						if(tDays1>=3) {
							timeHoursPrice=0;outgo=0;
							}
			}
			if (workType1.equals("plan")){
				if(tDays1<1.0) {
			switch(dguType1) {case 3:timeHoursPrice=744;outgo=1674.00;break;
			                  case 8:timeHoursPrice=744;outgo=1860.00;break;
			                  case 16:timeHoursPrice=744;outgo=1860.00;break;}}
				if(tDays1>=1.0&&tDays1<3) {
					switch(dguType1) {case 3:timeHoursPrice=0;outgo=1674.00;break;
					                  case 8:timeHoursPrice=0;outgo=1860.00;break;
					                  case 16:timeHoursPrice=0;outgo=1860.00;break;}}
				if(tDays1>=3) {
					switch(dguType1) {case 3:timeHoursPrice=0;outgo=1674.00;break;
					                  case 8:timeHoursPrice=0;outgo=1860.00;break;
					                  case 16:timeHoursPrice=0;outgo=1860.00;break;}}
			}

			if (workType1.equals("false")){
				switch(dguType1) {case 3:timeHoursPrice=0;outgo=1860.00;break;
		                          case 8:timeHoursPrice=0;outgo=2157.60;break;
		                          case 16:timeHoursPrice=0;outgo=2157.60;break;}}
	
	outGoPrice=outgo;
	orderPriceHours=timeHoursPrice*tHours1;
	BigDecimal bd = new BigDecimal(orderPriceHours).setScale(2, RoundingMode.HALF_UP);
	orderPriceHours = bd.doubleValue();}
	return orderPriceHours;
}
public Double calcOrderDays(int dguType,String workType,double jeep,double tHours,double tDays,double transPrice,String owener) {
	int dguType1=dguType;
	String workType1=workType,owener1=owener;
	double timeDayPrice = 0,tDays1=tDays,orderPriceDays=0,outgo=0;
	if(owener1.equals("po")) {
		if (workType1.equals("emergency")){
			if(tDays1<1.0) {
				switch(dguType1) {case 3:timeDayPrice=0;outgo=1674.00;break;
				                  case 8:timeDayPrice=0;outgo=1860.00;break;
				                  case 16:timeDayPrice=0;outgo=1860.00;break;}}
					if(tDays1>=1.0&&tDays1<3) {
						switch(dguType1) {case 3:timeDayPrice=18972.00;outgo=1674.00;break;
						                  case 8:timeDayPrice=19251.00;outgo=1860.00;break;
						                  case 16:timeDayPrice=19251.00;outgo=1860.00;break;}}
					if(tDays1>=3) {
						timeDayPrice=0;outgo=0;
						}
		}
		if (workType1.equals("plan")){
			if(tDays1<1.0) {
		switch(dguType1) {case 3:timeDayPrice=0;outgo=1674.00;break;
		                  case 8:timeDayPrice=0;outgo=1860.00;break;
		                  case 16:timeDayPrice=0;outgo=1860.00;break;}}
			if(tDays1>=1.0&&tDays1<3) {
				switch(dguType1) {case 3:timeDayPrice=18414;outgo=1674.00;break;
				                  case 8:timeDayPrice=18972;outgo=1860.00;break;
				                  case 16:timeDayPrice=18972;outgo=1860.00;break;}}
			if(tDays1>=3) {
				switch(dguType1) {case 3:timeDayPrice=16740;outgo=1674.00;break;
				                  case 8:timeDayPrice=17350.08;outgo=1860.00;break;
				                  case 16:timeDayPrice=17350.08;outgo=1860.00;break;}}
		}

		if (workType1.equals("false")){
			switch(dguType1) {case 3:timeDayPrice=0;outgo=1860.00;break;
	                          case 8:timeDayPrice=0;outgo=2157.60;break;
	                          case 16:timeDayPrice=0;outgo=2157.60;break;}}
		outGoPrice=outgo;
		orderPriceDays=timeDayPrice*tDays1;
		BigDecimal bd = new BigDecimal(orderPriceDays).setScale(2, RoundingMode.HALF_UP);
		orderPriceDays = bd.doubleValue();
		}
		
		if(owener1.equals("mts")) {
			if (workType1.equals("emergency")){
				if(tDays1<1.0) {
					switch(dguType1) {case 3:timeDayPrice=0;outgo=1674.00;break;
					                  case 8:timeDayPrice=0;outgo=1860.00;break;
					                  case 16:timeDayPrice=0;outgo=1860.00;break;}}
						if(tDays1>=1.0&&tDays1<3) {
							switch(dguType1) {case 3:timeDayPrice=16507.50;outgo=1674.00;break;
							                  case 8:timeDayPrice=17530.50;outgo=1860.00;break;
							                  case 16:timeDayPrice=18190.80;outgo=1860.00;break;}}
						if(tDays1>=3) {
							timeDayPrice=0;outgo=0;
							}
			}
			if (workType1.equals("plan")){
				if(tDays1<1.0) {
			switch(dguType1) {case 3:timeDayPrice=0;outgo=1674.00;break;
			                  case 8:timeDayPrice=0;outgo=1860.00;break;
			                  case 16:timeDayPrice=0;outgo=1860.00;break;}}
				if(tDays1>=1.0&&tDays1<3) {
					switch(dguType1) {case 3:timeDayPrice=16042.50;outgo=1674.00;break;
					                  case 8:timeDayPrice=17065.50;outgo=1860.00;break;
					                  case 16:timeDayPrice=17995.50;outgo=1860.00;break;}}
				if(tDays1>=3) {
					switch(dguType1) {case 3:timeDayPrice=15624.00;outgo=1674.00;break;
					                  case 8:timeDayPrice=16070.40;outgo=1860.00;break;
					                  case 16:timeDayPrice=16070.40;outgo=1860.00;break;}}
			}

			if (workType1.equals("false")){
				switch(dguType1) {case 3:timeDayPrice=0;outgo=1860.00;break;
		                          case 8:timeDayPrice=0;outgo=2157.60;break;
		                          case 16:timeDayPrice=0;outgo=2157.60;break;}}
			
			outGoPrice=outgo;
			orderPriceDays=timeDayPrice*tDays1;
			BigDecimal bd = new BigDecimal(orderPriceDays).setScale(2, RoundingMode.HALF_UP);
			orderPriceDays = bd.doubleValue();
			}
	
	return orderPriceDays;
}
@PostMapping("/recordOrder")
public String recordOrder(@RequestParam("ordernumber1") String orderNumber,@RequestParam("bsnumber1") String bsNumber,//
		@RequestParam("start1") String dateStart,@RequestParam("end1") String dateEnd,//
		@RequestParam("ordercalc1") String calc,@RequestParam("ordercalcnds1") String calcNds,Model model) {
 	
String [] orderArrayMassiv= {orderNumber,bsNumber,dateStart,dateEnd,calc,calcNds,"|"};
MainController.orderArray.addAll(new ArrayList<String>(Arrays.asList(orderArrayMassiv)));

OrderForm orderForm=new OrderForm(orderNumber,bsNumber,dateStart,dateEnd,calc,calcNds);
MainController.orderFormArray.addAll(Arrays.asList(orderForm));

    return "order";
}
}

/*String home = System.getProperty("user.home");
String outputFileName = home+"/Downloads/" +"dinamics.txt";
	String resource=getClass().getClassLoader().getResource("dinamics.txt").getPath();
	System.out.println(resource);
	//String resourcePath=Paths.get(resource).toString();
String outputFileName =resource;

MainController.outputFileName=outputFileName;
	try(BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName,true));)
    {
        writer.write(orderNumber+" ");writer.write(bsNumber+" ");writer.write(dateStart+" ");//
        writer.write(dateEnd+" ");writer.write(calc+" ");writer.write(calcNds+" ");
        writer.newLine();
        writer.close();
}
 catch (FileNotFoundException e) {
    System.err.println("не удалось создать файл ");
    e.printStackTrace();
} catch (IOException e) {
    System.err.println("Проблема с записью в файл ");
    e.printStackTrace();
}*/