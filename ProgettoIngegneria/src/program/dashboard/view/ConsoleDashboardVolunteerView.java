package program.dashboard.view;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import program.users.Person;
import utilities.MenuLayout;

public class ConsoleDashboardVolunteerView implements DashboardVolunteerView{

	private static final String MSG_ERR_SCELTA_ERRATA = "Errore, il valore inserito non corrisponde a nessuna delle scelte selezionabili";
	private static final String MSG_TITLE_MENU="Inserisci una delle seguenti opzioni: ";
	private static final ArrayList<String> VOCI_MENU_GENERALE = new ArrayList<>(Arrays.asList(
		    "Inserisci le disponibilità",
		    "Visualizza le disponibilità inserite",
		    "Rimuovi la disponbilità inserita",
		    "Visualizza i tipi visita",
		    "Visualizza le visite confermate",
		    "Fai scorrere il tempo",
		    "Esci"
	));
	private static final String MSG_PRINT_DATE="+++++ OGGI E' IL GIORNO: %s +++++ \n \n"; 
	private static final String MSG_USER_LOGGED_DATA= "\n \n "+"+++++ CIAO %s %s +++++";
	private static final String MSG_EXIT="\n"+"+++++ ARRIVEDERCI %s %s +++++ \n";
	private static final String MSG_EXIT_NO_LOG="\n"+"+++++ ARRIVEDERCI +++++ \n";
	

	//stampa menu principale
	public void printMainMenu() {
		MenuLayout menu= new MenuLayout(MSG_TITLE_MENU, VOCI_MENU_GENERALE);
		System.out.print(menu.visualizzaMenu());
	}
	
	//stampa errore in caso di scelta non compresa da quelle disponibili
	public void printMenuChoiceError() {
		System.out.println(MSG_ERR_SCELTA_ERRATA);
	}
	
	//stampa messaggio di arrivederci
	public void printGoodbye(Person user) {
		if(user!=null) {
			String printDataString= String.format(MSG_EXIT, user.getName(), user.getSurname());
			System.out.println(printDataString);
		}else {
			System.out.println(MSG_EXIT_NO_LOG);
		}
	}
	
	//stampa data corrente
	public void printCurrentDate(LocalDate currentDate) {
		String printDataString= String.format(MSG_PRINT_DATE,currentDate.toString());
		System.out.println(printDataString);
	}
	
	//stampa dati utente loggato
	public void printUserLogged(Person user) {
		String printDataString= String.format(MSG_USER_LOGGED_DATA, user.getName(), user.getSurname());
		System.out.println(printDataString);
	}
}
