package program.login.chain;

import program.login.LoginManager;
import program.login.VolunteerLoginStrategy;
import program.users.Person;
import program.users.volunteer.Volunteer;
import utilities.RepositorySystem;

public class VolunteerLoginHandler implements LoginHandler {
	
	private final LoginHandler next; 
	private final LoginManager manager;
	private final RepositorySystem repositoryVolunteerSystem;
	private final RepositorySystem repositoryVisitType;
	
	public VolunteerLoginHandler(LoginManager manager,RepositorySystem repositoryVolunteerSystem, RepositorySystem repositoryUserSystem, RepositorySystem repositoryVisitType) {
		this.manager = manager;
		this.next = new UserLoginHandler(manager, repositoryUserSystem);
		this.repositoryVolunteerSystem= repositoryVolunteerSystem;
		this.repositoryVisitType= repositoryVisitType;
	}

	@Override
	public Person handleRequest(Class<? extends Person> role, String user, String password) {
		
		if(Volunteer.class == role) //volontario
			return new VolunteerLoginStrategy(manager, repositoryVolunteerSystem, repositoryVisitType).login(user, password);
		
		return next.handleRequest(role, user, password);
	}

}
