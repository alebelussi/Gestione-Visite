package program.users;

public class Configurator extends Person{
	
	private String username;
	private String password; 
	
	public Configurator() {}; 
	
	public Configurator(String nome, String cognome, String username, String password) {
		super(nome, cognome);
		this.username = username;
		this.password = password;	
	}
	
	public String getUsername() {
		return this.username;	
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {	
		return this.password;
	}
	
	public void setPassword(String password) {	
		this.password = password; 
	}

	@Override
	public String viewPerson() {
		StringBuffer descrizioneConfiguratore = new StringBuffer();
		descrizioneConfiguratore.append("Nome "+ getName() + "\n Cognome "+ getSurname() + "\n Username " +getUsername());
		return descrizioneConfiguratore.toString();
	}
	
}
