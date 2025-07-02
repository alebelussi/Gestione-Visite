package program.users;

public abstract class Person {
	
	private String name;	
	private String surname; 
	
	public Person() {}; 
	
	public Person (String name, String surname) {
		this.name = name;	
		this.surname = surname; 
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String nome) {
		this.name = nome;
	}
	
	public String getSurname() {
		return this.surname;
	}
	
	public void setSurname(String cognome) {
		this.surname = cognome;
	}
	
	public abstract String viewPerson();
}

