package com.example.sbkafka.Model;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor 
@Entity
@Table(name="orderform")
public class OrderForm { //страница Заказа со всеми параметрами
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column
	private int ordernumber;//номер заказа
	
	@Column
	private String bsnumber;//номер БС
	
	@Column
	private String datestart;//начало работ
	
	@Column
	private String dateend;//окончание работ
	
	@Column
	private double calc;//стоимость без НДС
	
	@Column
	private double calcnds;//стоимость с НДС
	
	@Column
	private String comm;//комментарий к заказу
	
	@Column
	private Double jeepprice;//стоимость в заказе за использование проходимых мащин
	
	@Column
	private Integer jeepyesno;//определение факта использования проходимых машин
	
	@Column
	private Double jeeponeprice;//стоимость в ТЦП за использование проходимых машин
	
	@Column
	private Double orderoutgoprice;//стоимость за выезд аварийной бригады с ДГУ
	
	@Column
	private Double ordercalchprice;//стоимость работ (ед.изм.- часы)
	
	@Column
	private Double orderdifftime;//продолжительность работ (ед.изм.- часы)
	
	@Column
	private Double timehoursprice;//цена работ за 1 час
	
	@Column
	private String owenertype;//владелец ДГУ
	
	@Column
	private Integer dgutype;//тип(мощность) ДГУ
	
	@Column
	private String worktype;//тип работ(аварийные или плановые)
	
	@Column
	private Double ordercalcdprice;//стоимость работ (ед.изм.- сутки)
	
	@Column
	private Double orderdiffday;//продолжительность работ (ед.изм.- сутки)
	
	@Column
	private Double timedayprice;//цена работ за 1 сутки
	
	@Column
	private Double ordertransport;//стоимость транспортных расходов
	
	@Column
	private String orderdistance;//кол-во км
	
	@Column
	private Double orderkmprice;//цена транспортных расходов за 1 км
	
	@Column
	private Double ordernds;//стоимость работ с НДС
	
	@Column
	private Double rectifierprice;//стоимость в заказе за замену выпрямителя
	
	@Column
	private Integer rectifieryesno;//определение факта замены выпрямителя
	
	@Column
	private Double rectifieroneprice;//стоимость в ТЦП за замену выпрямителя
	
	@Column
	private Double breakerprice;//стоимость в заказе включения автомата
	
	public OrderForm(int ordernumber,String bsnumber,String datestart,String dateend,double calc,double calcnds,String comm,
			double jeepprice,int jeepyesno,double jeeponeprice,double orderoutgoprice,double ordercalchprice,
			double orderdifftime,double timehoursprice,String owenertype,int dgutype,String worktype,double ordercalcdprice,
			double orderdiffday,double timedayprice,double ordertransport,String orderdistance,double orderkmprice,double ordernds,
			double rectifierprice,int rectifieryesno,double rectifieroneprice,
			double breakerprice) {
		
		this.ordernumber=ordernumber;
		this.bsnumber=bsnumber;
		this.datestart=datestart;
		this.dateend=dateend;
		this.calc=calc;
		this.calcnds=calcnds;
		this.comm=comm;
		this.jeepprice=jeepprice;
		this.jeepyesno=jeepyesno;
		this.jeeponeprice=jeeponeprice;
		this.orderoutgoprice=orderoutgoprice;
		this.ordercalchprice=ordercalchprice;
		this.orderdifftime=orderdifftime;
		this.timehoursprice=timehoursprice;
		this.owenertype=owenertype;
		this.dgutype=dgutype;
		this.worktype=worktype;
		this.ordercalcdprice=ordercalcdprice;
		this.orderdiffday=orderdiffday;
		this.timedayprice=timedayprice;
		this.ordertransport=ordertransport;
		this.orderdistance=orderdistance;
		this.orderkmprice=orderkmprice;
		this.ordernds=ordernds;
		this.rectifierprice=rectifierprice;
		this.rectifieryesno=rectifieryesno;
		this.rectifieroneprice=rectifieroneprice;
		this.breakerprice=breakerprice;
	}	
}