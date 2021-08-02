package com.example.sbkafka;

import javax.persistence.*;

@Entity
@Table(name="price")
public class Price {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column
	private String pricesName;
	
	@Column
	private Double pricesValue;
	
	public Price() {}
	
	public Price(String pricesName,Double pricesValue) {
		this.pricesName=pricesName;
		this.pricesValue=pricesValue;
	}
	
	public int  getId() {
	return id;
	}
	
	public String getPricesName() {
		return pricesName;
	}
	public void setPricesName(String pricesName) {
		this.pricesName=pricesName;
	}
	
	public double getPricesValue() {
		return pricesValue;
	}
	public void setPricesValue(double pricesValue) {
		this.pricesValue=pricesValue;
	}
	
	@Override
	public String toString() {
		return "Price { id="+id+", pricesName="+pricesName+", pricesValue="+pricesValue+"};";
	}
}
