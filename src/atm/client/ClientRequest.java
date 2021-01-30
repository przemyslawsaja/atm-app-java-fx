package atm.client;

import java.io.Serializable;

import atm.Operations;


public class ClientRequest implements Serializable {
	private long cardId;
	private String pin;
	private double amount;
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
	private ClientRequest(Operations operation) {
	
		this.operation = operation;
	}
	public static ClientRequest exitSession(int id) {
		return new ClientRequest(id,Operations.EXIT);
	}
	public static ClientRequest checkBalance(long id) {
		return new ClientRequest(id,Operations.BALANCE_INQUIRY);
	}

	public long getcardId() {
		return cardId;
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
