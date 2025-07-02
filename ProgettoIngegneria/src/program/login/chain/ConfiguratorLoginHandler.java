package program.login.chain;

import program.login.ConfiguratorLoginStrategy;
import program.login.LoginManager;
import program.users.Configurator;
import program.users.Person;
import utilities.RepositorySystem;

public class ConfiguratorLoginHandler implements LoginHandler {
	
	private final LoginHandler next;
	private final LoginManager manager;
	private RepositorySystem repositoryConfiguratorSystem;
	
	public ConfiguratorLoginHandler(LoginManager manager, RepositorySystem repositoryConfiguratorSystem, RepositorySystem repositoryVolunteerSystem, RepositorySystem repositoryUserSystem, RepositorySystem repositoryVisitType) {
		this.manager = manager; 
		this.next = new VolunteerLoginHandler(manager, repositoryVolunteerSystem, repositoryUserSystem, repositoryVisitType);
		this.repositoryConfiguratorSystem= repositoryConfiguratorSystem;
	}

	@Override
	public Person handleRequest(Class<? extends Person> role, String user, String password) {
		
		if(Configurator.class == role) //configuratore
			return new ConfiguratorLoginStrategy(manager,repositoryConfiguratorSystem).login(user, password);
		
		return next.handleRequest(role, user, password);
	}

}
