package atm.server;

import java.io.Serializable;

public class OperationHistory implements Serializable {
	
	private String date;
	private float amount;
	private String details;
	
	
	public OperationHistory(String date, float amount, String details) {
		this.setDate(date);
		this.setAmount(amount);
		this.setDetails(details);
	}


	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public float getAmount() {
		return amount;
	}


	public void setAmount(float amount) {
		this.amount = amount;
	}


	public String getDetails() {
		return details;
	}


	public void setDetails(String details) {
		this.details = details;
	}
	

	

}
