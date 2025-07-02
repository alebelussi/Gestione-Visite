package program.login.chain;

import program.login.LoginManager;
import program.login.UserLoginStrategy;
import program.users.Person;
import program.users.User;
import utilities.RepositorySystem;

public class UserLoginHandler implements LoginHandler {
	
	private final LoginManager manager;
	private final RepositorySystem repositorySystem;
	
	public UserLoginHandler(LoginManager manager, RepositorySystem repositorySystem) {
		this.manager = manager;
		this.repositorySystem= repositorySystem;
	}

	@Override
	public Person handleRequest(Class<? extends Person> role, String user, String password) {
		
		if(User.class.equals(role))
			return new UserLoginStrategy(manager, repositorySystem).login(user, password);
	
		return null; //=> Ultimo elemento della chain
	}

}
