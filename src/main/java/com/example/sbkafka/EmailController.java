package com.example.sbkafka;

import java.util.ArrayList;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class EmailController {

    @Autowired
    public EmailService emailService;
	
	@GetMapping("/mailSend")
	public String showMail(Model model) {
				
		ArrayList<OrderForm> orderform=MainController.orderFormArray;
		model.addAttribute("orderform", orderform);
	    return "mailpage";
	}
	
	@PostMapping("/mailSend")
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
		
		return "oksend";
	}
	
	@PostMapping("/mailSendHtml")
	public String sendMailHtml(@RequestParam(value="comment") String textemail,@RequestParam(value="from") String frommail,@RequestParam(value="copy") String copyto) throws MessagingException {
							
			/*ArrayList<OrderForm> orderform=MainController.orderFormArray;
			    for(OrderForm order : orderform) {
				String ordertable="<td>order.orderNumber</td>"+"<td>order.bsNumber</td>"+"<td>order.dateStart</td>"+
						"<td>order.dateEnd</td>"+"<td>order.calc</td>"+"<td>order.calcNds</td>"+"\n";*/
			
			String ordertable="<h1>Hello</h1>";//тест
				try {
				this.emailService.sendHtmlEmail(textemail,ordertable,frommail,copyto);
	          
				} catch (Exception e) {e.printStackTrace();}  
        
	    return "oksend";
	}
	
	@PostMapping("/mailSendWithAttech")
	public String sendMailWithAttachment(@RequestParam(value="comment") String textemail,@RequestParam(value="file") MultipartFile file,@RequestParam(value="from") String frommail,@RequestParam(value="copy") String copyto) {
		
		try {
						
            String path1=MainController.outputFileName;
            this.emailService.sendEmailWithAttachment(textemail,path1,frommail,copyto);
            
        } catch (Exception e) {e.printStackTrace();}
		
	    return "oksend";
	}
	
}

/*
*byte[] bytes = file.getBytes();
*Path path = Paths.get(UPLOADED_fOLDER+file.getOriginalFilename());
*Files.write(path, bytes);
*String path1 = path.toString(); оставил для будущих проектов
*/