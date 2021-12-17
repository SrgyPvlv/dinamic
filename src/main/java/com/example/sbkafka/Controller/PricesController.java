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
import com.example.sbkafka.Model.Users;
import com.example.sbkafka.Service.PriceService;
import com.example.sbkafka.Service.UsersService;

// здесь контроллер для редактирования ТЦП договора и Пользователей приложения

@Controller
public class PricesController {
	
	@Autowired
	private PriceService priceService;
	
	@Autowired
	private UsersService usersService;
	
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
	
	@GetMapping("/admin/usersShow") //список всех пользователей
	public String showUsers(Model model) {
		List<Users> users=usersService.findAllUsers();
		model.addAttribute("users", users);
		return "users";
	}
	
	@GetMapping("/admin/usersCreate") // переход на форму создания нового пользователя
	public String newUserForm() {	   
	   return "newUserForm";
	}
	
	@PostMapping("/admin/usersCreate") // создание нового пользователя
	public String createNewUser(@RequestParam("login") String login, @RequestParam("password") String password,@RequestParam("role") String role) {	   
	   Users newUser=new Users(login,password,role);
	   usersService.saveUsers(newUser);
		
		return "redirect:/admin/usersShow";
	}
	
	@GetMapping("/admin/usersEdit") // переход на форму редактирования пользователя
	public String editUserForm(@RequestParam("id") int id, Model model) {	   
		Users user=usersService.findUsersById(id);
		model.addAttribute("user", user);
		
		return "editUserForm";
	}
	
	@PostMapping("/admin/usersEdit") // редактирование пользователя
	public String editNewUser(@RequestParam("id") int id,@RequestParam("login") String login, @RequestParam("password") String password,@RequestParam("role") String role) {	   
	   Users user=usersService.findUsersById(id);
	   user.setLogin(login);
	   user.setPassword(password);
	   user.setRole(role);
	   usersService.saveUsers(user);
		
		return "redirect:/admin/usersShow";
	}
	
	@GetMapping("/admin/usersDelete") // удаление пользователя
	public String deleteUser(@RequestParam("id") int id) {
		usersService.deleteUserById(id);
		
		return "redirect:/admin/usersShow";
	}
}
