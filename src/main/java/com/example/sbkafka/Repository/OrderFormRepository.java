package com.example.sbkafka.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.sbkafka.Model.OrderForm;

@Repository
public interface OrderFormRepository extends JpaRepository<OrderForm,Integer> {
	
	@Query("select f from OrderForm f order by ordernumber") //выборка из таблицы по увеличению номера заказа
	public List<OrderForm> findAllByOrderOrderNumberAsc();

	@Query("select count(f) from OrderForm f")//кол-во заказов
	public int countOfOrders();
	
	@Query("select sum(calc) from OrderForm f")//сумма всех заказов без ндс
	public Double sumOfOrders();
	
	@Query("select min(calc) from OrderForm f")//минимальный заказ
	public Double minOfOrder();
	
	@Query("select max(calc) from OrderForm f")//максимальный заказ
	public Double maxOfOrder();
	
	@Query("select avg(calc) from OrderForm f")//средняя стоимость заказов
	public Double avgOfOrder();
	
	@Query("select count(f) from OrderForm f where calc in(:f1,:f2,:f3,:f4,:f5,:f6)")//кол-во ложных выездов до КАД
	public int countOfFalseOut(@Param("f1") double f1,@Param("f2") double f2,@Param("f3") double f3,@Param("f4") double f4,@Param("f5") double f5,@Param("f6") double f6);
	
	@Query("select sum(calc) from OrderForm f where calc in(:f1,:f2,:f3,:f4,:f5,:f6)")//сумма ложных выездов до КАД
	public Double sumOfFalseOut(@Param("f1") double f1,@Param("f2") double f2,@Param("f3") double f3,@Param("f4") double f4,@Param("f5") double f5,@Param("f6") double f6);
	
	@Query("select count(f) from OrderForm f where comm in('Fix')")//кол-во заказов ДФС
	public int countDfs();
	
	@Query("select sum(calc) from OrderForm f where comm in('Fix')")//сумма заказов ДФС
	public Double sumDfs();
	
	@Query(value="select * from orderform where ordernumber in(:ordernum) order by ordernumber",nativeQuery=true)//поиск по номеру заказа
	public List<OrderForm> findByOrderNumber(@Param("ordernum") int ordernum);

	@Query(value="select * from orderform where lower(bsnumber) like concat('%',:bsnum,'%') order by ordernumber",nativeQuery=true)//поиск по номеру БС
	public List<OrderForm> findByBsName(@Param("bsnum") String bsnum);
	
}
