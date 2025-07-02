package program.users;

public abstract class Person {

	private String name;
	private String surname;

	public Person() {}

	public Person (String name, String surname) {
		this.name = name;
		this.surname = surname;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return this.surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	public abstract String getRole();

}

