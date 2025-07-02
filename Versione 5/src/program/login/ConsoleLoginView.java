package program.login;

import java.util.ArrayList;
import java.util.Arrays;

import program.users.Configurator;
import program.users.Person;
import program.users.User;
import program.users.volunteer.Volunteer;
import program.visitistance.Visit;
import utilities.InputDati;
import utilities.MenuLayout;

//classe view per il controller LoginManager
public class ConsoleLoginView implements LoginView{
	
	private static final String MSG_INS_FRUITORE_CONFERMA = "Il nome utente inserito non risulta presente nel sistema, desideri registrarti?";
	private static final String MSG_CONFERMA_ACCESSO = "Accesso eseguito correttamente!";
	private static final String MSG_ERR_LOGIN = "Errore: credenziali di accesso errate!";
	private static final String MSG_TITLE_MENU="\n Inserisci una delle seguenti opzioni: ";
	private static final String MSG_EXIT="\n"+"+++++ ARRIVEDERCI +++++ \n";
	private static final String MSG_INS_USERNAME = "Inserisci lo username/nickname: ";
	private static final String MSG_INS_PASSWORD = "Inserisci la password: ";
	private static final ArrayList<String> VOCI_MENU_GENERALE = new ArrayList<>(Arrays.asList(
		    "Configuratore",
		    "Volontario",
		    "Fruitore",
		    "Esci"
		));
	private static final String MSG_WELCOME="+++++ BENVENUTO NELL'APP +++++";
	private static final String MSG_TITOLO_WELCOME_VOLUNTEER = "Benvenuto nell'applicazione dei volontari!";
	private static final String MSG_TITOLO_WELCOME_CONFIGURATOR = "Benvenuto nell'applicazione dei configuratori!";
	private static final String MSG_TITOLO_WELCOME_USER = "Benvenuto nell'applicazione dei fruitori!";
	private static final String MSG_CONFERMA_REGISTRAZIONE = "Vuoi registrarti?";
	private static final String MSG_INS_COGNOME = "Inserisci il cognome dell'utente: ";
	private static final String MSG_INS_NOME = "Inserici il nome dell'utente: ";
	private static final String MSG_ERR_USERNAME_GIA_PRESENTE = "Errore...username già utilizzato";
	private static final String MSG_TITOLO_PRIMO_ACCESSO_ASSOLUTO = "Primo accesso assoluto";
	private static final String MSG_INS_PERSONE_ISCRIVIBILI_SINGOLA_SESSIONE = "Inserisci il numero di persone iscrivibili per singola iscrizione: ";
	private static final String MSG_INS_PERSONE_ISCRIVIBILI = "Numero impostato di persone iscrivibili per singola iscrizione: ";
	private static final String MSG_CONFERMA_NUMERO = "Vuoi inserire il numero di persone iscrivibili per singola iscrizione?";
	
	//lettura ruolo per l'accesso che si vuole effettuare
	public Class<? extends Person> readRole() {
		MenuLayout menu= new MenuLayout(MSG_TITLE_MENU,VOCI_MENU_GENERALE);
		int opz;

		//ciclo di esecuzione
		do {
			opz=InputDati.leggiIntero(menu.visualizzaMenu(), 0, menu.getNumVoci()-1);

			switch (opz) {
				case 1: {
					return Configurator.class;
				}
				case 2: {
					return Volunteer.class;
				}
				case 3: {
					return User.class;
				}
				default:
					System.out.println(MSG_EXIT);
					break;
			}
		}while(opz!=0);
		return null;
	}

	//metodo di stampa del messagio di benvenuto
	public void printMessageWelcomeApp() {
		System.out.println(MSG_WELCOME);
	}

	//metodo che legge lo username
	public String readUser() {
		return InputDati.leggiStringaNonVuota(MSG_INS_USERNAME);
		
	}

	//metodo che legge la password
	public String readPassword() {
		return InputDati.leggiStringaNonVuota(MSG_INS_PASSWORD);
	}
	
	//metodo che legge l'ok per la conferma della registrazione
	public boolean readConfirmRegistration() {
		return InputDati.yesNo(MSG_CONFERMA_REGISTRAZIONE);	
	}
	
	//metodo che legge l'ok per l'operazione di registrazione del fruitore
	public boolean readConfirmChoiceRegistration() {
		return InputDati.yesNo(MSG_INS_FRUITORE_CONFERMA);
	}

	//metodo per stampare il messaggio di benvenuto al nuovo volontario
	public void printMessageWelcomeNewVolunteer() {
		System.out.println(MSG_TITOLO_WELCOME_VOLUNTEER);
	}
	
	//metodo per stampare il messaggio di benvenuto al nuovo volontario
	public void printMessageWelcomeNewUser() {
		System.out.println(MSG_TITOLO_WELCOME_USER);
	}
	
	//metodo per stampare il messaggio di benvenuto al nuovo configuratore
	public void printMessageWelcomeNewConfigurator() {
		System.out.println(MSG_TITOLO_WELCOME_CONFIGURATOR);
	}

	//messaggio di errore => credenziali errate
	public void printMessageErrorLogin() {
		System.out.println(MSG_ERR_LOGIN);
	}

	//metodo che conferma che l'accesso è andato a buon fine
	public void printMessageOkLogin() {
		System.out.println(MSG_CONFERMA_ACCESSO);
	}

	//metodo per leggere il nome dell'utente
	public String readName() {
		return InputDati.leggiStringaNonVuota(MSG_INS_NOME);
	}
	
	//metodo per leggere il cognome dell'utente
	public String readSurname() {
		return InputDati.leggiStringaNonVuota(MSG_INS_COGNOME);
	}

	//metodo per comunicare che lo username è già presente
	public void printMessageErrorUsernameInsert() {
		System.out.println(MSG_ERR_USERNAME_GIA_PRESENTE);
	}

	//metodo che stampa il messaggio di primo accesso assoluto
	public void printMessageForFirstAbsoluteAccess() {
		System.out.println(MSG_TITOLO_PRIMO_ACCESSO_ASSOLUTO);
	}

	//metodo che legge il numero di iscrizioni massime possibili
	public int readNumberOfSub() {
		int numberOfSub;
		do {
			if(Visit.getNumberOfSub() != 0)
				System.out.println(MSG_INS_PERSONE_ISCRIVIBILI + Visit.getNumberOfSub());
			numberOfSub = InputDati.leggiInteroConMinimo(MSG_INS_PERSONE_ISCRIVIBILI_SINGOLA_SESSIONE, 0);
		}while(!InputDati.yesNo(MSG_CONFERMA_NUMERO));
		
		return numberOfSub;
	}
}
