package program.login;

import program.login.chain.ConfiguratorLoginHandler;

import program.login.chain.LoginHandler;
import program.users.*;
import utilities.RepositorySystem;


//classe che viene utilizzata per la fase di inizializzazione ed effettuare il login
public class LoginManager {
	
	private Person logged;
	private LoginView view;
	private RepositorySystem repositoryConfiguratorSystem;
	private RepositorySystem repositoryVolunteerSystem;
	private RepositorySystem repositoryUserSystem;
	private RepositorySystem repositoryVisitType;
	
	public LoginManager(LoginView view,RepositorySystem repositoryConfiguratorSystem, RepositorySystem repositoryVolunteerSystem, RepositorySystem repositoryUserSystem, RepositorySystem repositoryVisitType) {
		this.view= view;
		this.view.printMessageWelcomeApp();
		this.repositoryConfiguratorSystem= repositoryConfiguratorSystem;
		this.repositoryVolunteerSystem= repositoryVolunteerSystem;
		this.repositoryUserSystem= repositoryUserSystem;
		this.repositoryVisitType= repositoryVisitType;
	}
	
	//metodo getLogged
	public Person getLogged(){
		return logged;
	}
	
	//metodo per effettuare il login 
	public Person login() {
		String user, psw;
		
		//lettura del ruolo in cui si vuole effettuare l'accesso
		Class<? extends Person> role= view.readRole();
			
		if(role == null)
			return null;
		
		//Istanzio la chain di handler
		LoginHandler loginHandler = new ConfiguratorLoginHandler(this, repositoryConfiguratorSystem, repositoryVolunteerSystem, repositoryUserSystem, repositoryVisitType);
		
		//effettuo il login
		do{
			
			//leggo le credenziali di accesso
			user= view.readUser();
			psw= view.readPassword();
			
			//effettuo l'operazione di login vera e propria utilizzando la chain
			logged= loginHandler.handleRequest(role, user, psw);
			
			//controllo l'esito del login
			if(logged==null)
				view.printMessageErrorLogin();
			
		}while(logged==null);
		
		view.printMessageOkLogin();
		return logged;
	}

	//metodo per invocare il messaggio di saluto al nuovo volontario
	protected void sayWelcomeNewVolunter() {
		view.printMessageWelcomeNewVolunteer();
	}
	
	//metodo per invocare il messaggio di saluto al nuovo configuratore
	protected void sayWelcomeNewConfigurator() {
		view.printMessageWelcomeNewConfigurator();
	}
	
	//metodo che ritorna la password, ossia il valore per inizializzare il volontario
	protected String getDataForInizializeVolunteer() {
		String password;
	
		//stampo il messaggio di benvenuto
		sayWelcomeNewVolunter();
		do {
			password= view.readPassword();
		}while(!view.readConfirmRegistration());
		
		return password;
	}

	//metodo che legge le credenziali del nuovo configuratore
	protected Configurator readNewConfigData() {
		String username, password, name, surname;
		//stampa messaggio di benvenuto
		view.printMessageWelcomeNewConfigurator();
		//ciclo di lettura
		do {
			//leggo nome e cognome
			name = view.readName();
			surname = view.readSurname();

			//leggo lo username
			do {
				username = view.readUser();
				//controllo che lo username non sia già presente
				if(!isOkUsernameInsert(username))
					view.printMessageErrorUsernameInsert();
			}while(!isOkUsernameInsert(username));

			//leggo la password
			password = view.readPassword();

		}while(!view.readConfirmRegistration());
		
		return new Configurator(name, surname, username, password);
	}

	//metodo che tratta l'indicazione del primo accesso assoluto
	protected void sayFirstHello() {
		view.printMessageForFirstAbsoluteAccess();
	}
	
	//metodo che tratta la lettura delle informazioni per il primo accesso assoluto => lettura numero iscrizioni
	protected int getDataForFirstAbsoluteAccess() {
		return view.readNumberOfSub();
	}

	//metodo che verifica se l'utente che sta effettuando l'accesso come fruitore vuole registrarsi
	protected boolean isChoiceRegistration() {
		return view.readConfirmChoiceRegistration();
	}

	//metodo che legge le credenziali del nuovo fruitore
	protected User readNewUserData(String username, String password) {
		String name, surname;
		//stampa messaggio di benvenuto
		view.printMessageWelcomeNewUser();
		//ciclo di lettura
		do {
			//leggo nome, cognome
			name = view.readName();
			surname = view.readSurname();
		}while(!view.readConfirmRegistration());
		
		return new User(name, surname, username, password);
	}
	
	//metodo che verifica che lo username inserito sia univoco
	protected boolean isOkUsernameInsert(String user) {
		//controllo che lo username inserito con coincida con nessuno di quelli del configuratore
		boolean confFound = repositoryConfiguratorSystem.existElement("username", user);
		if(confFound)	return false;
			
		//controllo che lo username non sia uguale a nessun nickname di nessun volontario
		boolean volFound = repositoryVolunteerSystem.existElement("nickname", user);
		if(volFound)	return false;
						
		//controllo che lo username non sia uguale a nessun nickname di nessun volontario
		boolean userFound = repositoryUserSystem.existElement("username", user);
		if(userFound)	return false;

		//username univoco
		return true;
	}
}
