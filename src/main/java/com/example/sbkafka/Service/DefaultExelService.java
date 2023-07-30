package com.example.sbkafka.Service;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.sbkafka.Helper.ExelHelper;
import com.example.sbkafka.Model.OrderForm;
import com.example.sbkafka.Repository.OrderFormRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultExelService implements ExelService {
	private final OrderFormRepository orderFormRepository;

	@Override
	public ByteArrayInputStream ordersLoad() {
		
		List<OrderForm> orders = orderFormRepository.findAllByOrderOrderNumberAsc();
		ByteArrayInputStream in = ExelHelper.orderFormToExcel(orders);
		
		return in;
	}

}
