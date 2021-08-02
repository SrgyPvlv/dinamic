package com.example.sbkafka;

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
	
	@GetMapping("/priceChanges")
	public String showPrices(Model model) {
		
		priceService.findAll().forEach(it->System.out.println(it));
		
		model.addAttribute("DGU3_CONTRACTOR_TIMEHOURES_WORKALARM", MyConstants.DGU3_CONTRACTOR_TIMEHOURES_WORKALARM);
		
	    return "prices";
	}
	
	@PostMapping("/priceChanges")
	public String sendMail(@RequestParam(value="DGU3_CONTRACTOR_TIMEHOURES_WORKALARM") String DGU3_CONTRACTOR_TIMEHOURES_WORKALARM,Model model) {
		
		MyConstants.DGU3_CONTRACTOR_TIMEHOURES_WORKALARM=Double.parseDouble(DGU3_CONTRACTOR_TIMEHOURES_WORKALARM);
		
		return "oksend";
	}
	
}
