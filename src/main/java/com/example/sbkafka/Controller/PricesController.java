package com.example.sbkafka.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.sbkafka.Model.Price;
import com.example.sbkafka.Service.PriceService;

// контроллер для редактирования ТЦП договора

@Controller
public class PricesController {
	
	@Autowired
	private PriceService priceService;
	
	@GetMapping("/priceShow") // показать таблицу ТЦП на странице Форма расчетов
	public String showPricesOrder(Model model) {
		
		Price[] price=new Price[47];
		for (int i=2;i<47;i++) {
	     
		price[i]=priceService.getById(i);
		model.addAttribute("price"+i,price[i]);}
		
	    return "pricesOrder";
	}
	
	@GetMapping("/admin/priceShow") // показать таблицу ТЦП на странице админа
	public String showPrices(Model model) {
		
		Price[] price=new Price[47];
		for (int i=2;i<47;i++) {
	     
		price[i]=priceService.getById(i);
		model.addAttribute("price"+i,price[i]);}
		
	    return "prices";
	}
	
	@PostMapping("/admin/priceChanges") //редактирование таблицы ТЦП
	public String savePrices(@RequestParam Map<String,String> allParam,Model model) {
		
		List<String> list = new ArrayList<String>(allParam.values());
		list.add(0, null);list.add(1, null);
		String[] paramMassiv=list.toArray(new String[0]);
		Price[] price=new Price[47];
		for (int i=2;i<47;i++) { 
		price[i]=priceService.getById(i);
		price[i].setPricesValue(Double.parseDouble(paramMassiv[i]));
		priceService.savePrice(price[i]);
		}
		
		return "redirect:/admin/priceShow";
	}
}
