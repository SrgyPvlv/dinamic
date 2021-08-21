package com.example.sbkafka;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderFormRepository extends JpaRepository<OrderForm,Integer> {
	
	@Query("select f from OrderForm f order by ordernumber") //выборка из таблицы по увеличению номера заказа
	public List<OrderForm> findAllByOrderOrderNumberAsc();

	@Query("select count(f) from OrderForm f")
	public int countOfOrders();

}
