package com.example.sbkafka;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class PricesController {
	
	@Autowired
	private PriceService priceService;
	
	@GetMapping("/admin/priceShow")
	public String showPrices(Model model) {
		
		Price[] price=new Price[46];
		for (int i=2;i<46;i++) {
	     
		price[i]=priceService.getById(i);
		model.addAttribute("price"+i,price[i]);}
		
	    return "prices";
	}
	
	
	@PostMapping("/admin/priceChanges")
	public String savePrices(@RequestParam Map<String,String> allParam,Model model) {
		
		List<String> list = new ArrayList<String>(allParam.values());
		list.add(0, null);list.add(1, null);
		String[] paramMassiv=list.toArray(new String[0]);
		Price[] price=new Price[46];
		for (int i=2;i<46;i++) { 
		price[i]=priceService.getById(i);
		price[i].setPricesValue(Double.parseDouble(paramMassiv[i]));
		priceService.savePrice(price[i]);
		}
		
		return "oksave";
	}
	
}
