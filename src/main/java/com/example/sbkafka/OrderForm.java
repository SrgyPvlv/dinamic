package com.example.sbkafka;

public class OrderForm {
	private int orderNumber;
	private String bsNumber;
	private String timeStart;
	private String timeEnd;
	private String orderDistance;
	
	public OrderForm() {}
	
	public OrderForm(int orderNumber,String bsNumber,String timeStart,String timeEnd,String orderDistance) {
		this.orderNumber=orderNumber;
		this.bsNumber=bsNumber;
		this.timeStart=timeStart;
		this.timeEnd=timeEnd;
		this.orderDistance=orderDistance;
	}

	public int getOrderNumber() {
        return orderNumber;
    }
 
    public void setOrderNumber(int orderNumber) {
    	this.orderNumber=orderNumber;
}
    
    public String getBsNumber() {
        return bsNumber;
    }
 
    public void setBsNumber(String bsNumber) {
    	this.bsNumber=bsNumber;
}  
    public String getTimeStart() {
        return timeStart;
    }
 
    public void setTimeStart(String timeStart) {
    	this.timeStart=timeStart;
}  
    public String getTimeEnd() {
        return timeEnd;
    }
 
    public void setTimeEnd(String timeEnd) {
    	this.timeEnd=timeEnd;   
}
    public String getOrderDistance() {
        return orderDistance;
    }
 
    public void setOrderDistance(String orderDistance) {
    	this.orderDistance=orderDistance;
}
}