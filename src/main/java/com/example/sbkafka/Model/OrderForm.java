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
public class OrderForm {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column
	private int ordernumber;
	
	@Column
	private String bsnumber;
	
	@Column
	private String datestart;
	
	@Column
	private String dateend;
	
	@Column
	private double calc;
	
	@Column
	private double calcnds;
	
	@Column
	private String comm;
	
	@Column
	private Double jeepprice;
	
	@Column
	private Integer jeepyesno;
	
	@Column
	private Double jeeponeprice;
	
	@Column
	private Double orderoutgoprice;
	
	@Column
	private Double ordercalchprice;
	
	@Column
	private Double orderdifftime;
	
	@Column
	private Double timehoursprice;
	
	@Column
	private String owenertype;
	
	@Column
	private Integer dgutype;
	
	@Column
	private String worktype;
	
	@Column
	private Double ordercalcdprice;
	
	@Column
	private Double orderdiffday;
	
	@Column
	private Double timedayprice;
	
	@Column
	private Double ordertransport;
	
	@Column
	private String orderdistance;
	
	@Column
	private Double orderkmprice;
	
	@Column
	private Double ordernds;
	
	public OrderForm(int ordernumber,String bsnumber,String datestart,String dateend,double calc,double calcnds,String comm,
			double jeepprice,int jeepyesno,double jeeponeprice,double orderoutgoprice,double ordercalchprice,
			double orderdifftime,double timehoursprice,String owenertype,int dgutype,String worktype,double ordercalcdprice,
			double orderdiffday,double timedayprice,double ordertransport,String orderdistance,double orderkmprice,double ordernds) {
		
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
	}	
}