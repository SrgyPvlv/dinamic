package com.example.sbkafka;

public class OrderForm {
	private String orderNumber;
	private String bsNumber;
	private String dateStart;
	private String dateEnd;
	private String calc;
	private String calcNds;
	
	public OrderForm() {}
	
	public OrderForm(String orderNumber,String bsNumber,String dateStart,String dateEnd,String calc,String calcNds) {
		this.orderNumber=orderNumber;
		this.bsNumber=bsNumber;
		this.dateStart=dateStart;
		this.dateEnd=dateEnd;
		this.calc=calc;
		this.calcNds=calcNds;
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
    	calc.replace(".", ",");
    	this.calc=calc;
}
    public String getCalcNds() {
        return calcNds;
    }
 
    public void setCalcNds(String calcNds) {
    	calcNds.replace(".", ",");
    	this.calcNds=calcNds;
}
}