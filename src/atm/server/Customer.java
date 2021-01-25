package atm.server;

public class Customer {
	private long id;
	private String firstname;
	private String surname;
	private String street;
	private String postcode;
	private String city;
	private int pesel;
	private String phone;
	private String email;
	public Customer(long id, String firstname, String surname, String street, String postcode, String city,
			int pesel, String phone, String email) {
		super();
		this.id = id;
		this.firstname = firstname;
		this.surname = surname;
		this.street = street;
		this.postcode = postcode;
		this.city = city;
		this.pesel = pesel;
		this.phone = phone;
		this.email = email;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public int getPesel() {
		return pesel;
	}
	public void setPesel(int pesel) {
		this.pesel = pesel;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
	

}
