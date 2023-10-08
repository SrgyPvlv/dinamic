package com.example.sbkafka.Model;

public class Order {  //Заказ
	
	private int id;
	private int ordernumber;//номер заказа
	private String bsnumber;//номер БС
	private String datestart;//дата начала работ
	private String dateend;//дата окончания работ
	private double calc;//стоимость без НДС
	private double calcnds;//стоимость с НДС
	private String comm;//комментарий

	public Order() {}
	
	public Order(int id,int ordernumber,String bsnumber,String datestart,String dateend,double calc,double calcnds,String comm) {
		this.id=id;
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
	public void setId(int id) {
    	this.id=id;
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
}