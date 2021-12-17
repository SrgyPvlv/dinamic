package com.example.sbkafka.Model;

import javax.persistence.*;

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
	
	@OneToOne(mappedBy = "orderform", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval=true)
    private FileDB fileDB;

	public OrderForm() {}
	
	public OrderForm(int ordernumber,String bsnumber,String datestart,String dateend,double calc,double calcnds,String comm) {
		this.ordernumber=ordernumber;
		this.bsnumber=bsnumber;
		this.datestart=datestart;
		this.dateend=dateend;
		this.calc=calc;
		this.calcnds=calcnds;
		this.comm=comm;
	}
	
	public int  getId() {
		return id;
	}

	public int getOrderNumber() {
        return ordernumber;
    }
 
    public void setOrderNumber(int ordernumber) {
    	this.ordernumber=ordernumber;
}
    
    public String getBsNumber() {
        return bsnumber;
    }
 
    public void setBsNumber(String bsnumber) {
    	this.bsnumber=bsnumber;
}  
    public String getDateStart() {
        return datestart;
    }
 
    public void setDateStart(String datestart) {
    	this.datestart=datestart;
}  
    public String getDateEnd() {
        return dateend;
    }
 
    public void setDateEnd(String dateend) {
    	this.dateend=dateend;   
}
    public double getCalc() {
        return calc;
    }
 
    public void setCalc(double calc) {
    	this.calc=calc;
}
    public double getCalcNds() {
        return calcnds;
    }
 
    public void setCalcNds(double calcnds) {
    	this.calcnds=calcnds;
}
    
    public String getComm() {
        return comm;
    }
 
    public void setComm(String comm) {
    	this.comm=comm;
}
    public FileDB getFileDB() {
        return fileDB;
    }

    public void setFileDB(FileDB fileDB) {
    	if(fileDB==null) {
    		if(this.fileDB!=null) {this.fileDB.setOrderForm(null);}}
    	
    	else {fileDB.setOrderForm(this);}
        this.fileDB = fileDB;}
    
    @Override
    public String toString() {
        return(ordernumber + " " + bsnumber + " " + datestart + " " + dateend + " " + calc+ " " + calcnds+ " "+comm+"\n");
    }
}