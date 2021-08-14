package com.example.sbkafka;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BdController {

	@Autowired
	private OrderFormService orderformservice;
	
	@GetMapping("/bdShow")
	public String bdShow(Model model){
		List<OrderForm> orderlist=orderformservice.findAll();
		model.addAttribute("orderlist", orderlist);
		return "orderbd";
	}
	
	@GetMapping("admin/bdEdit")
	public String bdEdit(Model model){
		List<OrderForm> orderlist=orderformservice.findAll();
		model.addAttribute("orderlist", orderlist);
		
		
		return "orderbdedit";
	}
	
	@PostMapping("/bdEdit")
	public String edit(@RequestParam("id") int id, Model model) {
	
	OrderForm order=orderformservice.getById(id);
	model.addAttribute("order", order);
	   
	   return "editform"; /*сделать 15.08.21 форму для редактирования заказа*/
	}
	
	@PostMapping("/bdDelete")
	public String delete(@RequestParam("id") int id) {
	
	orderformservice.deleteById(id);
	   
	   return "redirect:/admin/bdEdit";
	}
	
}
