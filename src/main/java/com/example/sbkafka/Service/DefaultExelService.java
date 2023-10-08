package com.example.sbkafka.Service;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.sbkafka.Helper.ExelHelper;
import com.example.sbkafka.Model.OrderForExel;
import com.example.sbkafka.Model.OrderForm;
import com.example.sbkafka.Repository.BsListRepository;
import com.example.sbkafka.Repository.OrderFormRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultExelService implements ExelService {
	private final OrderFormRepository orderFormRepository;
	private final BsListRepository bsListRepository;

	@Override
	public ByteArrayInputStream ordersLoad() {
		
		List<OrderForm> orders = orderFormRepository.findAllByOrderOrderNumberAsc();
		List<OrderForExel> ordersforexel = new ArrayList<>();
		for (OrderForm order: orders) {
			String bn=order.getBsnumber();
			String bsaddress=null;
			try{bsaddress=bsListRepository.findBsByNumber(bn).getBsAddress();}catch(Exception ex) {bsaddress="БС с данным номером не найдена. Обратитесь к администратору приложения.";};
			ordersforexel.add(new OrderForExel(order.getOrdernumber(),order.getBsnumber(),order.getDatestart(),order.getDateend(),
					order.getCalc(),order.getCalcnds(),order.getComm(),bsaddress));
		}
		
		ByteArrayInputStream in = ExelHelper.orderFormToExcel(ordersforexel);
				
		return in;
	}
}
