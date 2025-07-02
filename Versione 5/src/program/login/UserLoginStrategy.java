package program.login;

import program.users.Person;
import program.users.User;
import utilities.RepositorySystem;

public class UserLoginStrategy implements LoginStrategy{
	
	private LoginManager loginManager;
	private RepositorySystem repositoryUserSystem;
	
	public UserLoginStrategy(LoginManager loginManager, RepositorySystem repositoryUserSystem) {
		this.loginManager= loginManager;
		this.repositoryUserSystem= repositoryUserSystem;
	}
	
	public UserLoginStrategy() {} //no-op
	
	@Override
	public Person login(String usernameRead, String psw) {
		boolean userFound;
				
		//ricerco il configuratore
		userFound = repositoryUserSystem.existElement("username", usernameRead);
				
		//se utente trovato 
		if(userFound){
			User newUser= repositoryUserSystem.searchElement(User.class, "username", usernameRead);
			if(psw.equals(newUser.getPassword())) //utente già inserito e password corretta
				return newUser;
		}else if(loginManager.isOkUsernameInsert(usernameRead) && loginManager.isChoiceRegistration()) //utente non presente nel sistema => gli chiedo se vuole registrarsi
			return firstAccess(usernameRead, psw);
			
		//errore in fase di accesso
		return null;
	}
		
	//metodo che tratta il primo accesso dell'utente, ossia la sua registrazione
	private User firstAccess(String userRead, String psw) {
		//leggo le informazioni del nuovo fruitore
		User newUser= loginManager.readNewUserData(userRead, psw);
		//inserisco il nuovo fruitore nel file
		repositoryUserSystem.addElement(User.class, newUser);
			
		return newUser;
	}
	
}