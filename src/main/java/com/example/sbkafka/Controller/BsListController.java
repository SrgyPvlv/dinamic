package com.example.sbkafka.Controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.sbkafka.Model.BsList;
import com.example.sbkafka.Service.BsListService;

// создание, редактирование, удаление БС в БД
@Controller
public class BsListController {
	
	@Autowired
	BsListService bsListService;
	
	@GetMapping("/admin/bsCreate") //переход на форму создания новой БС в БД
	public String newBsForm() {
		return "newBsForm";
	}
	
	@PostMapping("/admin/bsCreate") //создание новой БС в БД
	public String bsCreate(@RequestParam("bsnumber") String bsnumber,@RequestParam("bsaddress") String bsaddress, Model model) {
		bsListService.createBs(bsnumber, bsaddress);
		List<BsList> bs=bsListService.findBsByNumber(bsnumber);
		model.addAttribute("bss", bs);
		return "bsList";}
	
	@GetMapping("/admin/bsEdit") // переход на форму редактирование БС в БД
	public String editBs(@RequestParam("id") long id, Model model) {	   
	   BsList bs=bsListService.getBsById(id);
	   String bsnumber=bs.getBsNumber();
	   String bsaddress=bs.getBsAddress();
		
	   model.addAttribute("id", id);
	   model.addAttribute("bsnumber", bsnumber);
	   model.addAttribute("bsaddress", bsaddress);
		return "editBsForm";
	}
	
	@PostMapping("/admin/bsEdit") // редактирование БС в БД
	public String editBs(@RequestParam("id") long id,@RequestParam("bsnumber") String bsnumber,@RequestParam("bsaddress") String bsaddress, Model model) {	   
	   BsList bsOld=bsListService.getBsById(id);
	   bsOld.setBsNumber(bsnumber);
	   bsOld.setBsAddress(bsaddress);
	   bsListService.saveBs(bsOld);
	   BsList bsNew=bsListService.getBsById(id);
	   
	   model.addAttribute("bss", bsNew);
		return "bsList";
	}
	
	@GetMapping("/superadmin/bsDelete") // удаление БС
	public String deleteBsById(@RequestParam("id") long id, Model model) {
		BsList bs=bsListService.getBsById(id);
		String bsnumber=bs.getBsNumber();
		bsListService.deleteBsById(id);
		List<BsList> bsFind=bsListService.findBsByNumber(bsnumber);
		
		model.addAttribute("bss", bsFind);
		return "bsList";
	}
	
	@GetMapping("/admin/findBsByBsName") // поиск БС по № БС, можно ввести часть номера
	public String findBsByBsNumber(@RequestParam(name="bsNumberSearch", required=false, defaultValue="99-99999") String bsNumberSearch, 
			Model model) throws IOException{
		
		List<BsList> bs=bsListService.findBsByNumber(bsNumberSearch);
		
		model.addAttribute("bss", bs);
		return "bsList";
	}
}
