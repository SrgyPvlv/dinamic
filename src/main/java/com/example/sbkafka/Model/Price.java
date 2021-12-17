package com.example.sbkafka.Model;

import javax.persistence.*;

@Entity
@Table(name="price")
public class Price {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column
	private String pricesname;
	
	@Column
	private Double pricesvalue;
	
	public Price() {}
	
	public Price(String pricesname,Double pricesvalue) {
		this.pricesname=pricesname;
		this.pricesvalue=pricesvalue;
	}
	
	public int  getId() {
	return id;
	}
	
	public String getPricesName() {
		return pricesname;
	}
	public void setPricesName(String pricesname) {
		this.pricesname=pricesname;
	}
	
	public double getPricesValue() {
		return pricesvalue;
	}
	public void setPricesValue(double pricesvalue) {
		this.pricesvalue=pricesvalue;
	}
	
	@Override
	public String toString() {
		return "Price { id="+id+", pricesName="+pricesname+", pricesValue="+pricesvalue+"};";
	}
}
