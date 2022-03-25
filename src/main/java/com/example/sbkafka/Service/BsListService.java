package com.example.sbkafka.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.sbkafka.Model.BsList;
import com.example.sbkafka.Repository.BsListRepository;

@Service
public class BsListService {

	@Autowired
	BsListRepository bsListRepository;
	
	public String findBsAddress(String bsnumber) { //поиск адреса БС по номеру БС, для вставки в заявку (надо ввести точный номер)
		String message="БС с данным номером не найдена. Обратитесь к администратору приложения. (Пока-что Вы можете "
				+ "внести адрес самостоятельно непосредственно в word-файл заявки.)";
		BsList bs=bsListRepository.findBsByNumber(bsnumber);
		
		if (bs!=null) {
		String bsaddress=bs.getBsAddress();
		return bsaddress;} else {return message;}	
	}
	
	public List<BsList> findBsByNumber(String bsnumber) { //поиск БС по номеру для редактирования/удаления и т.д.
		List<BsList> bs=bsListRepository.findBsByNumber2(bsnumber);		
		return bs;}
	
	public void createBs(String bsnumber,String bsaddress) { //создание новой БС в БД
		BsList newBs= new BsList();
		newBs.setBsNumber(bsnumber);
		newBs.setBsAddress(bsaddress);
		bsListRepository.saveAndFlush(newBs);
	}
	public BsList getBsById(Long id) { //получение БС по id
		return bsListRepository.getById(id);
	}
	public void saveBs(BsList bs) { //сохранение БС в БД
		bsListRepository.saveAndFlush(bs);
	}
	public void deleteBsById(long id) { //удаление БС по id
		bsListRepository.deleteById(id);
	}
}
