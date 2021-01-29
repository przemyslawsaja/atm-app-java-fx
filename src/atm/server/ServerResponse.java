package atm.server;

import java.io.Serializable;

import atm.Operations;

public class ServerResponse implements Serializable {
	private Operations operation;
	private boolean operationSuccess;
	private double updatedBalance;
	private double requestedAmount;
	private String errorMessage;

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
}
