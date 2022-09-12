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
	private double jeepPrice;
	
	@Column
	private int jeepYesNo;
	
	@Column
	private double jeepOnePrice;
	
	@Column
	private double orderOutGoPrice;
	
	@Column
	private double orderCalcHPrice;
	
	@Column
	private double orderDiffTime;
	
	@Column
	private double timeHoursPrice;
	
	@Column
	private String owenerType;
	
	@Column
	private int dguType;
	
	@Column
	private String workType;
	
	@Column
	private double orderCalcDPrice;
	
	@Column
	private double orderDiffDay;
	
	@Column
	private double timeDayPrice;
	
	@Column
	private double orderTransport;
	
	@Column
	private String orderDistance;
	
	@Column
	private double orderKmPrice;
	
	@Column
	private double orderNds;
	
	public OrderForm(int ordernumber,String bsnumber,String datestart,String dateend,double calc,double calcnds,String comm,
			double jeepPrice,int jeepYesNo,double jeepOnePrice,double orderOutGoPrice,double orderCalcHPrice,
			double orderDiffTime,double timeHoursPrice,String owenerType,int dguType,String workType,double orderCalcDPrice,
			double orderDiffDay,double timeDayPrice,double orderTransport,String orderDistance,double orderKmPrice,double orderNds) {}
	
}