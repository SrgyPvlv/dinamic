package com.example.sbkafka;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BdController {

	@Autowired
	private OrderFormService orderformservice;
	
	@GetMapping("/bdShow")
	public String bdShow(Model model){
		List<OrderForm> orderlist=orderformservice.findAll();
		model.addAttribute("orderlist", orderlist);
		return"orderbd";
	}
}
