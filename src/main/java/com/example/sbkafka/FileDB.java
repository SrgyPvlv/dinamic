package com.example.sbkafka;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "files")
public class FileDB {
	
	  @Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private int id;

	  @Column
	  private String name;

	  @Column
	  private String type;

	  @Column
	  @Type(type="org.hibernate.type.BinaryType")
	  private byte[] data;
	  
	  
	  @OneToOne(fetch = FetchType.LAZY)
	  @JoinColumn(name = "orderform_id",referencedColumnName="id")
	  private OrderForm orderform;
	  
	  @Column(name="orderform_id",nullable=false,insertable=false,updatable=false)
	  private int orderform_id;
	  
	  public FileDB() {
	  }

	  public FileDB(String name, String type, byte[] data,int orderform_id) {
	    this.name = name;
	    this.type = type;
	    this.data = data;
	    this.orderform_id=orderform_id;
	  }

	  public int getId() {
	    return id;
	  }

	  public String getName() {
	    return name;
	  }

	  public void setName(String name) {
	    this.name = name;
	  }

	  public String getType() {
	    return type;
	  }

	  public void setType(String type) {
	    this.type = type;
	  }

	  public byte[] getData() {
	    return data;
	  }

	  public void setData(byte[] data) {
	    this.data = data;
	  }
	  
	  public int getOrderFormId() {
		  return orderform_id;
	  }
	  
	 public void setOrderFormId(int orderform_id) {
		 this.orderform_id=orderform_id;
	 }
	 public OrderForm getOrderForm() {
		 return this.orderform;
	 }
	 public void setOrderForm(OrderForm orderform) {
		 
		 this.orderform=orderform;
	 }

}
