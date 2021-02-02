package atm.client;

import java.io.Serializable;

import atm.Operations;


public class ClientRequest implements Serializable {
	private long cardId;
	private String pin;
	private float amount;
	private Operations operation;

	// metoda wysy³ana do serwera celem autoryzacji u¿ytkownika has³o w formie zaszyfrowanej
	public static ClientRequest authenticate(int id, String pin) {
		return new ClientRequest(id, pin,Operations.AUTHENTICATE);
	}	


	private ClientRequest(long id, String pin, Operations operation) {
		this.cardId = id;
		this.pin = pin;

		this.operation = operation;
	}
	private ClientRequest(long id, Operations operation) {
		this.cardId = id;
		this.pin = pin;

		this.operation = operation;
	}
	private ClientRequest(long id,float amount, Operations operation) {
		this.cardId = id;
			this.amount = amount;

		this.operation = operation;
	}
	private ClientRequest(Operations operation) {
	
		this.operation = operation;
	}
	public static ClientRequest exitSession(int id) {
		return new ClientRequest(id,Operations.EXIT);
	}
	public static ClientRequest checkBalance(long id) {
		return new ClientRequest(id,Operations.BALANCE_INQUIRY);
	}
	public static ClientRequest deposit(int id, float amt) {
		return new ClientRequest(id,amt, Operations.DEPOSIT);
	}
	public static ClientRequest withDraw(int id, float amt) {
		return new ClientRequest(id,amt, Operations.WITHDRAW);
	}
	
	public static ClientRequest operationHistory(int id) {
		return new ClientRequest(id, Operations.OPERATION_HISTORY);
	}
	
	public static ClientRequest withDrawCustom(int id, float amt) {
		return new ClientRequest(id,amt, Operations.WITHDRAW_CUSTOM);
	}

	public long getcardId() {
		return cardId;
	}
	public float getAmount()
	{
		return amount;
	}

	public String getPin() {
		return pin;
	}

	public Operations getOperation() {
		return operation;
	}

	@Override
	public String toString() {
		if (operation == Operations.AUTHENTICATE || operation == Operations.BALANCE_INQUIRY)
			return String.format("Id Karty: %s%nOperacja: %s", this.cardId,this.operation);

		return String.format("Id Karty: %s%n Kwota %.2f%nOperacja: %s", this.cardId,
				this.amount, this.operation);
	}
}
