package com.example.sbkafka;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderFormRepository extends JpaRepository<OrderForm,Integer> {
	
	@Query("select f from OrderForm f order by ordernumber") //выборка из таблицы по увеличению номера заказа
	public List<OrderForm> findAllByOrderOrderNumberAsc();

	@Query("select count(f) from OrderForm f")//кол-во заказов
	public int countOfOrders();
	
	@Query("select sum(calc) from OrderForm f")//сумма всех заказов без ндс
	public double sumOfOrders();
	
	@Query("select min(calc) from OrderForm f")//минимальный заказ
	public double minOfOrder();
	
	@Query("select max(calc) from OrderForm f")//максимальный заказ
	public double maxOfOrder();
	
	@Query("select avg(calc) from OrderForm f")//средняя стоимость заказов
	public double avgOfOrder();
	
	@Query("select count(f) from OrderForm f where calc in(:f1,:f2,:f3,:f4,:f5,:f6)")//кол-во ложных выездов до КАД
	public int countOfFalseOut(@Param("f1") double f1,@Param("f2") double f2,@Param("f3") double f3,@Param("f4") double f4,@Param("f5") double f5,@Param("f6") double f6);
	
	@Query("select sum(calc) from OrderForm f where calc in(:f1,:f2,:f3,:f4,:f5,:f6)")//сумма ложных выездов до КАД
	public int sumOfFalseOut(@Param("f1") double f1,@Param("f2") double f2,@Param("f3") double f3,@Param("f4") double f4,@Param("f5") double f5,@Param("f6") double f6);
	
	@Query("select count(f) from OrderForm f where comm in('Fix')")//кол-во заказов ДФС
	public int countDfs();
	
	@Query("select sum(calc) from OrderForm f where comm in('Fix')")//сумма заказов ДФС
	public int sumDfs();
	
	@Query(value="copy (select ordernumber,bsnumber,datestart,dateend,calc,calcnds,comm from orderform) to STDOUT WITH (FORMAT CSV, HEADER)",nativeQuery=true)
	public void saveToCsv();
	
	//COPY products_273 TO STDOUT WITH (FORMAT CSV, HEADER);
//\\copy (select ordernumber,bsnumber,datestart,dateend,calc,calcnds,comm from orderform) to 'C:/My/orders.csv' DELIMITER ',' CSV HEADER",nativeQuery=true)
}
