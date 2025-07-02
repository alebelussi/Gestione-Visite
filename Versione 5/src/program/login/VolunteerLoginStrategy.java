package program.login;

import program.DataConsistencyService;
import program.users.Person;
import program.users.volunteer.Volunteer;
import utilities.RepositorySystem;

public class VolunteerLoginStrategy implements LoginStrategy{
	
	private LoginManager loginManager;
	private RepositorySystem repositoryVolunteerSystem;
	private RepositorySystem repositoryVisitType;
	
	public VolunteerLoginStrategy(LoginManager loginManager, RepositorySystem repositoryVolunteerSystem, RepositorySystem repositoryVisitType) {
		this.loginManager= loginManager;
		this.repositoryVolunteerSystem= repositoryVolunteerSystem;
		this.repositoryVisitType= repositoryVisitType;
	}
	
	@Override
	public Person login(String user, String psw) {
		boolean userFound;
		
		//ricerco il volontario
		userFound = repositoryVolunteerSystem.existElement("nickname", user);
			
		//se utente trovato
		if(userFound){
			Volunteer volunteer = repositoryVolunteerSystem.searchElement(Volunteer.class, "nickname", user);
			//se la password è quella di default => primo accesso
			if(volunteer.getPassword().equals("volunteer")) {
				//faccio il primo accesso per il volontario
				firstAccess(repositoryVolunteerSystem, repositoryVisitType, volunteer);
				return volunteer;	
					
			}else if(psw.equals(volunteer.getPassword())) //utente già inserito e password corretta
				return volunteer;
		}
			
		//errore in fase di accesso
		return null;
	}
	
	//metodo che tratta il primo accesso del volontario
	private void firstAccess(RepositorySystem repositoryStm,RepositorySystem repositoryStmVisitType, Volunteer volunteer) {
		//leggo la nuova password del configuratore
		String password= loginManager.getDataForInizializeVolunteer();
			
		//setto la nuova password
		volunteer.setPassowrd(password);
		repositoryStm.modifyElement("nickname", volunteer.getNickname(), volunteer);

		//verifico la consistenza in Json
		DataConsistencyService.updatePasswordVolunteer(volunteer,repositoryStmVisitType); //--> modifica della password anche nei tipi visita
	}
	
	
}
