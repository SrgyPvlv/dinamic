package com.example.sbkafka.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="bslist")
public class BsList {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@Column
	private String bsnumber;
	
	@Column
	private String bsaddress;
	
	public BsList() {}
	
	public BsList(String bsnumber,String bsaddress) {
		this.bsnumber=bsnumber;
		this.bsaddress=bsaddress;	
	}
	
	public long getId() {
		return id;
	}
	public String getBsNumber() {
		return bsnumber;
	}
	public void setBsNumber(String bsnumber) {
		this.bsnumber=bsnumber;
	}
	public String getBsAddress() {
		return bsaddress;
	}
	public void setBsAddress(String bsaddress) {
		this.bsaddress=bsaddress;
	}
}
