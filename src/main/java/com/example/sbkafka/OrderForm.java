package com.example.sbkafka;

public class OrderForm {
	private String orderNumber;
	private String bsNumber;
	private String dateStart;
	private String dateEnd;
	private String calc;
	private String calcNds;
	private String comm;
	
	public OrderForm() {}
	
	public OrderForm(String orderNumber,String bsNumber,String dateStart,String dateEnd,String calc,String calcNds,String comm) {
		this.orderNumber=orderNumber;
		this.bsNumber=bsNumber;
		this.dateStart=dateStart;
		this.dateEnd=dateEnd;
		this.calc=calc.replace(".", ",");
		this.calcNds=calcNds.replace(".", ",");
		this.comm=comm;
	}

	public String getOrderNumber() {
        return orderNumber;
    }
 
    public void setOrderNumber(String orderNumber) {
    	this.orderNumber=orderNumber;
}
    
    public String getBsNumber() {
        return bsNumber;
    }
 
    public void setBsNumber(String bsNumber) {
    	this.bsNumber=bsNumber;
}  
    public String getDateStart() {
        return dateStart;
    }
 
    public void setDateStart(String dateStart) {
    	this.dateStart=dateStart;
}  
    public String getDateEnd() {
        return dateEnd;
    }
 
    public void setDateEndd(String dateEnd) {
    	this.dateEnd=dateEnd;   
}
    public String getCalc() {
        return calc;
    }
 
    public void setCalc(String calc) {
    	this.calc=calc;
}
    public String getCalcNds() {
        return calcNds;
    }
 
    public void setCalcNds(String calcNds) {
    	this.calcNds=calcNds;
}
    
    public String getComm() {
        return comm;
    }
 
    public void setComm(String comm) {
    	this.comm=comm;
}
    
    @Override
    public String toString() {
        return(orderNumber + " " + bsNumber + " " + dateStart + " " + dateEnd + " " + calc+ " " + calcNds+ " "+comm+"\n");
    }
}