package program.users;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import utilities.RepositorySystem;

public class User extends Person{

	private String username;
	private String password;

	public User() {}

	public User(String name, String surname, String username, String password) {
		super(name, surname);
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        	if (obj == null || getClass() != obj.getClass()) return false;
        	User user = (User) obj;
        return Objects.equals(username, user.username); // Confronto basato sul username
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @Override
    @JsonIgnore
	public String getRole() {
		return "User";
	}

    //metodo che verifica se esiste un fruitore con lo username passato come parametro
    public static boolean existUser(String username, RepositorySystem repositorySystem) {
		return (repositorySystem.existElement("username", username));
	}
    
}
