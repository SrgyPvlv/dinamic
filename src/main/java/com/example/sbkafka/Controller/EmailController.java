package com.example.sbkafka.Controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.sbkafka.Model.OrderForm;
import com.example.sbkafka.Service.EmailService;

@Controller
public class EmailController {

    @Autowired
    public EmailService emailService;
	
	@GetMapping("/mailSend") //переход на страницу отправки почты с отображением заказов
	public String showMail(Model model) {
				
		ArrayList<OrderForm> orderform=MainController.orderFormArray;
		model.addAttribute("orderform", orderform);
	    return "mailPage";
	}
	
	@PostMapping("/mailSend") // отправка почты с заказами
	public String sendMail(@RequestParam(value="comment") String textemail,@RequestParam(value="from") String frommail,@RequestParam(value="copy") String copyto) {
		
		ArrayList<OrderForm> orderform=MainController.orderFormArray;
		StringBuilder x=new StringBuilder();
		for(OrderForm order : orderform) {	
		x.append(order);		
		}
		String textorder=x.toString();
		
		try {     
            this.emailService.sendSimpleEmail(textemail,textorder,frommail,copyto);
        } catch (Exception e) {e.printStackTrace();}
		
		return "okSend";
	}
	
}