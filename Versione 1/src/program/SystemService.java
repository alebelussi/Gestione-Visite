package program;

import program.places.*;
import program.users.*;
import program.visitistance.*;
import utilities.*;

public class SystemService {
	
	private static final String MSG_CONFERMA_NUMERO = "Vuoi inserire il numero di persone iscrivibili per singola iscrizione?";
	private static final String MSG_INS_PERSONE_ISCRIVIBILI_SINGOLA_SESSIONE = "Inserisci il numero di persone iscrivibili per singola iscrizione: ";
	private static final String MSG_INS_PERSONE_ISCRIVIBILI = "Numero impostato di persone iscrivibili per singola iscrizione: ";
	private static final String MSG_TITOLO_BENTORNATO = "Bentornato nell'applicazione!";
	private static final String MSG_ERR_PASSWORD_ERRATA = "Errore...password errata";
	private static final String MSG_ERR_USERNAME_NON_PRESENTE = "Errore...username errato";
	private static final String MSG_TITOLO_WELCOME = "Benvenuto nell'applicazione!";
	private static final String MSG_INS_PASSWORD = "Inserisci la password: ";
	private static final String MSG_TITOLO_PRIMO_ACCESSO_ASSOLUTO = "Primo accesso assoluto";
	private static final String MSG_REGISTRAZIONE_CONFERMATA = "Regitrazione completata con successo";
	private static final String MSG_CONFERMA_REGISTRAZIONE = "Vuoi registrarti?";
	private static final String MSG_ERR_USERNAME_GIA_PRESENTE = "Errore...username già utilizzato";
	private static final String MSG_INS_USERNAME = "Inserisci lo username: ";
	private static final String MSG_INS_COGNOME = "Inserisci il cognome dell'utente: ";
	private static final String MSG_INS_NOME = "Inserici il nome dell'utente: ";
	private static final String MSG_ERR_USER_NICK = "Errore: username inserito coincide con il nickname di un volontario";
	private static final String FILE_VISITE = "json/visite.json";
	private static final String FILE_VOLONTARIO = "json/volontario.json";
	private static final String FILE_CONFIGURATOR = "json/configuratore.json";

	//metodo che tratta il primo accesso di un configuratore
	private static Configurator firstAccess(JsonManager jsonManager) {
		String name, surname, user, password;
		VolunteerList volunteerList= new VolunteerList(FILE_VOLONTARIO);
		
		do {
			name = InputDati.leggiStringaNonVuota(MSG_INS_NOME);
			surname = InputDati.leggiStringaNonVuota(MSG_INS_COGNOME);
			
			do {
				user=InputDati.leggiStringa(MSG_INS_USERNAME);

				//controllo che lo username non sia già presente 
				if(jsonManager.searchElement("username", user)) 
					System.out.println(MSG_ERR_USERNAME_GIA_PRESENTE);
								
					//controllo che lo username non sia uguale a nessun nickname di nessun volontario
					
					if(volunteerList.findVolunteer(user))
						System.out.println(MSG_ERR_USER_NICK);
			}while(jsonManager.searchElement("username", user) || volunteerList.findVolunteer(user));
			
			password = InputDati.leggiStringaNonVuota(MSG_INS_PASSWORD);
			
		}while(!InputDati.yesNo(MSG_CONFERMA_REGISTRAZIONE));
		
		Configurator utenteRegistrato= new Configurator(name, surname, user, password);
		jsonManager.addElement(Configurator.class, utenteRegistrato);
		
		if(jsonManager.searchElement("firstAbsoluteAccess", "true"))
			firstAbsoluteAccess(jsonManager);
		
		System.out.println(MSG_REGISTRAZIONE_CONFERMATA);
		return utenteRegistrato;
	}
	
	//metodo che tratta il primo accesso assoluto al sistema
	private static void firstAbsoluteAccess(JsonManager jsonManager) {
		System.out.println(MSG_TITOLO_PRIMO_ACCESSO_ASSOLUTO);
		
		PlaceManager.setRegion();
		
		setNumberOfSub();
		
		jsonManager.modifyObject("firstAbsoluteAccess", "true", "false");
		
	}
	
	//++++++++++++++++++ Metodo che effettua il LOGIN ++++++++++++++++++++++++++
		public static Configurator login(String path) {
			String user, password; 
			JsonManager jsonManager = new JsonManager(path);
			do {
				user = InputDati.leggiStringaNonVuota(MSG_INS_USERNAME);
				password = InputDati.leggiStringaNonVuota(MSG_INS_PASSWORD);
				
				if(jsonManager.searchElement("user", user) && jsonManager.searchElement("pw", password)) { //accesso con le credenziali standard = primo accesso
					System.out.println(MSG_TITOLO_WELCOME);
					return firstAccess(jsonManager); 
				}
					
				else if(!jsonManager.searchElement("username", user))
					System.out.println(MSG_ERR_USERNAME_NON_PRESENTE);
				else if(!jsonManager.searchElement("password", password))
					System.out.println(MSG_ERR_PASSWORD_ERRATA);
				else { //ACCESSO CORRETTO E UTENTE GIà INSERITO
					System.out.println(MSG_TITOLO_BENTORNATO);
				}
			}while(!jsonManager.searchElement("username", user) || !jsonManager.searchElement("password", password));
			
			return jsonManager.searchElement(Configurator.class,"username", user);
		}
		
		//metodo che setta in numero di iscrivibili per un'iscrizione
		public static void setNumberOfSub() {
			int numberOfSub; 
			JsonManager jsNumberOfSub = new JsonManager(FILE_VISITE);
			Visit.setNumberOfSub(jsNumberOfSub.loadFirstElement("numberOfSub"));
			
			do {	
				if(Visit.getNumberOfSub() != 0)
					System.out.println(MSG_INS_PERSONE_ISCRIVIBILI + Visit.getNumberOfSub());
				numberOfSub = InputDati.leggiInteroConMinimo(MSG_INS_PERSONE_ISCRIVIBILI_SINGOLA_SESSIONE, 0);
			}while(!InputDati.yesNo(MSG_CONFERMA_NUMERO));
			

			jsNumberOfSub.modifyObject("numberOfSub", String.valueOf(Visit.getNumberOfSub()), numberOfSub);
			Visit.setNumberOfSub(numberOfSub);
		}
		
		//metodo che verifica se esiste un configuratore con lo username passato come parametro
		public static boolean existConfigurator(String username) {
			JsonManager jsConf= new JsonManager(FILE_CONFIGURATOR);
					
			//ricerco se esiste l'elemento e ritorno l'esito
			return (jsConf.searchElement("username", username));		
		}	

}
