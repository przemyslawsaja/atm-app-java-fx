package atm.server;

import java.io.Serializable;
import java.util.ArrayList;

import atm.Operations;
import javafx.collections.ObservableList;

public class ServerResponse implements Serializable {
	private Operations operation;
	private boolean operationSuccess;
	private float updatedBalance;
	private float requestedAmount;
	private String errorMessage;
	private ArrayList<OperationHistory> operationHistoryList = new ArrayList<OperationHistory>();

	public ServerResponse() {
	}

	public Operations getOperation() {
		return operation;
	}

	public void setOperation(Operations operation) {
		this.operation = operation;
	}

	public boolean isOperationSuccess() {
		return operationSuccess;
	}

	public void setOperationSuccess(boolean operationSuccess) {
		this.operationSuccess = operationSuccess;
	}	
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public double getUpdatedBalance() {
		return updatedBalance;
	}

	public void setUpdatedBalance(float updatedBalance) {
		this.updatedBalance = updatedBalance;
	}
	public void setRequestedAmount(float amount)
	{
		this.requestedAmount=amount;
	}

	@Override
	public String toString() {
		switch (this.getOperation()) {
		case AUTHENTICATE:
			if (this.isOperationSuccess())
				return "Pomyœlnie Zalogowano!";
			return this.getErrorMessage();	
		}	
		return "";
	}

	public ArrayList<OperationHistory> getOperationHistoryList() {
		return operationHistoryList;
	}

	public void setOperationHistoryList(ArrayList<OperationHistory> operationHistoryList) {
		this.operationHistoryList = operationHistoryList;
	}

	
}
