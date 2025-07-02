package program.login;

import program.places.PlaceManager;
import program.places.ConsolePlaceView;
import program.users.Configurator;
import program.users.Person;
import program.visitistance.Visit;
import utilities.JsonManager;
import utilities.RepositorySystem;

public class ConfiguratorLoginStrategy implements LoginStrategy{

	private static final String FILE_VISITE_JSON = "json/visite.json";
	private static final String FILE_PLACE_JSON = "json/luoghi.json";
	
	private LoginManager loginManager;
	private RepositorySystem repositoryConfiguratorSystem;
	
	public ConfiguratorLoginStrategy(LoginManager loginManager, RepositorySystem repositoryConfiguratorSystem) {
		this.loginManager= loginManager;
		this.repositoryConfiguratorSystem= repositoryConfiguratorSystem;
	}
	
	@Override
    public Person login(String user, String psw) {
		
		boolean userFound;
			
		//ricerco il configuratore
		userFound = repositoryConfiguratorSystem.existElement("username", user);
			
		//se utente trovato 
		if(userFound){
			//se utente e password sono quelli di default => primo accesso
			if(user.equals("admin") && psw.equals("admin")) {
				//faccio il primo accesso per il volontario
				return firstAccess(repositoryConfiguratorSystem, new JsonManager(FILE_VISITE_JSON), new JsonManager(FILE_PLACE_JSON));
			}else{ 
				Configurator config= repositoryConfiguratorSystem.searchElement(Configurator.class, "username", user);
				if(psw.equals(config.getPassword())) //utente già inserito e password corretta
					return config;
			}
		}
		//errore in fase di accesso
		return null;
	}
	
	//metodo per il primo accesso assoluto 
	private void firstAbsoluteAccess(RepositorySystem repositoryVisitSystem, RepositorySystem repositoryPlaceSystem) {
		loginManager.sayFirstHello();

		//inserisco l'ambito territoriale
		PlaceManager placeManager = new PlaceManager(new ConsolePlaceView(), repositoryPlaceSystem);
		placeManager.setRegion();

		//inserisco il numero massimo per iscrizione
		setNumberOfSub(repositoryVisitSystem);

		repositoryConfiguratorSystem.modifyObject("firstAbsoluteAccess", "true", "false");
	}
		
	//metodo che setta il numero massimo di iscrizioni possibili
	private void setNumberOfSub(RepositorySystem repositoryVisitSystem) {
		int numberOfSub= loginManager.getDataForFirstAbsoluteAccess();
			
		repositoryVisitSystem.modifyObject("numberOfSub", String.valueOf(Visit.getNumberOfSub()), numberOfSub);
		Visit.setNumberOfSub(numberOfSub);
	}
	
	//metodo che tratta il primo accesso del configuratore
	private Configurator firstAccess(RepositorySystem repositorySystem,RepositorySystem repositoryVisitSystem, RepositorySystem repositoryPlaceSystem) {
		Configurator utenteRegistrato= loginManager.readNewConfigData();
		repositorySystem.addElement(Configurator.class, utenteRegistrato);

		//se primo accesso assoluto
		if(repositorySystem.existElement("firstAbsoluteAccess", "true"))
			firstAbsoluteAccess(repositoryVisitSystem, repositoryPlaceSystem);

		return utenteRegistrato;
	}

}
