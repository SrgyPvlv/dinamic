package com.example.sbkafka;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BdController {
	double sumorders;
	@Autowired
	private OrderFormService orderformservice;
	
	@GetMapping("/bdShow") //получение инфо из бд заказов
	public String bdShow(Model model){
		List<OrderForm> orderlist=orderformservice.findAllByOrderOrderNumberAsc();
		model.addAttribute("orderlist", orderlist);
		
		for(OrderForm everyorder:orderlist) {
			String a=everyorder.getCalc();
			a = a.replaceAll(",", ".").replaceAll(" ", "");
			double b=Double.parseDouble(a);
			sumorders=sumorders+b;
			
		}
		BigDecimal bd = new BigDecimal(sumorders).setScale(2, RoundingMode.HALF_UP);
		sumorders = bd.doubleValue();
		System.out.println("Сумма заказов="+sumorders);
		return "orderbd";
	}
	
	@GetMapping("admin/bdEdit") //получение инфо из бд заказов с возможностью перехода к редактированию
	public String bdEdit(Model model){
		List<OrderForm> orderlist=orderformservice.findAllByOrderOrderNumberAsc();
		model.addAttribute("orderlist", orderlist);
		
		
		return "orderbdedit";
	}
	
	@PostMapping("/bdEdit") // переход на форму редактирования заказа
	public String edit(@RequestParam("id") int id, Model model) {
	
	OrderForm order=orderformservice.getById(id);
	model.addAttribute("order", order);
	   
	   return "editform";
	}
	
	@PostMapping("/orderEdit") // редактирование заказа
	public String orderEdit(@RequestParam("id2") int id, @RequestParam("ordernumber2") String ordernumber,@RequestParam("bsnumber2") String bsnumber,//
	@RequestParam("start2") String datestart,@RequestParam("end2") String dateend,//
	@RequestParam("ordercalc2") String calc,@RequestParam("ordercalcnds2") String calcnds,//
	@RequestParam("comm2") String comm) {
	
	orderformservice.editOrderForm(id, ordernumber, bsnumber, datestart, dateend, calc, calcnds, comm);
	   
	   return "okedit";
	}
	
	@PostMapping("/bdDelete") // удаление заказа
	public String delete(@RequestParam("id") int id) {
	
	orderformservice.deleteById(id);
	   
	   return "redirect:/admin/bdEdit";
	}
	
}
