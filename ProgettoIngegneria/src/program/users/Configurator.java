package program.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import utilities.RepositorySystem;

public class Configurator extends Person{
	private String username;
	private String password;

	public Configurator() {}

	public Configurator(String name, String surname, String username, String password) {
		super(name, surname);
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

	@JsonIgnore
	@Override
	public String getRole() {
		return "Configurator";
	}
	
	//metodo che verifica se esiste un configuratore con lo username passato come parametro
	public static boolean existConfigurator(String username, RepositorySystem repositorySystem) {
		//ricerco se esiste l'elemento e ritorno l'esito
		return (repositorySystem.existElement("username", username));
	}
}
