package atm.server;

public class AtmCard {

		private long cardId;
		private int pin;
		private int cvv_code;
		private String type;
		private BankAccount account;
		public AtmCard(long cardId, int pin, int cvv_code, String type, BankAccount account) {
			super();
			this.cardId = cardId;
			this.pin = pin;
			this.cvv_code = cvv_code;
			this.type = type;
			this.account = account;
		}
		public long getCardId() {
			return cardId;
		}
		public void setCardId(long cardId) {
			this.cardId = cardId;
		}
		public int getPin() {
			return pin;
		}
		public void setPin(int pin) {
			this.pin = pin;
		}
		public int getCvv_code() {
			return cvv_code;
		}
		public void setCvv_code(int cvv_code) {
			this.cvv_code = cvv_code;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public BankAccount getAccount() {
			return account;
		}
		public void setAccount(BankAccount account) {
			this.account = account;
		}
		
		
}
