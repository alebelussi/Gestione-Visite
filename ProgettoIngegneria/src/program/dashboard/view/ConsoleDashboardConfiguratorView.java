package program.dashboard.view;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import program.users.Person;
import utilities.MenuLayout;

public class ConsoleDashboardConfiguratorView implements DashboardConfiguratorView{

	private static final String MSG_ERR_SCELTA_ERRATA = "Errore, il valore inserito non corrisponde a nessuna delle scelte selezionabili";
	private static final String MSG_TITOLO_MENU_PLACES="[ GESTIONE LUOGHI ] \n \n "+"Inserisci una delle seguenti opzioni: ";
	private static final ArrayList<String> VOCI_MENU_PLACES= new ArrayList<>(Arrays.asList(
		    "Inserisci Luogo",
		    "Modifica Luogo",
		    "Visualizza Luoghi",
		    "Rimuovi Luogo",
		    "Cerca Luogo",
		    "Indietro"
	));
	private static final String MSG_TITOLO_MENU_VISITTYPE="[ GESTIONE TIPI VISITA ] \n \n "+"Inserisci una delle seguenti opzioni: ";
	private static final ArrayList<String> VOCI_MENU_VISITTYPE = new ArrayList<>(Arrays.asList(
		    "Inserisci Tipo Visita",
		    "Modifica Tipo Visita",
		    "Visualizza Tipi Visite",
		    "Rimuovi Tipo Visita",
		    "Associa Volontari a Tipo Visita",
		    "Cerca Tipo Visita",
		    "Indietro"
	));
	private static final String MSG_TITOLO_MENU_VISIT="[ GESTIONE VISITE ] \n \n "+"Inserisci una delle seguenti opzioni: ";
	private static final ArrayList<String> VOCI_MENU_VISIT = new ArrayList<>(Arrays.asList(
		    "Visualizza Visite in programma",
		    "Escludi una data in cui effettuare una visita",
		    "Visualizza date escluse",
		    "Rimuovi data esclusa",
		    "Modifica il numero di iscritti per singola iscrizione",
		    "Indietro"
	));
	private static final String MSG_TITOLO_MENU_VOLUNTEER="[ GESTIONE VOLONTARI ] \n \n "+"Inserisci una delle seguenti opzioni: ";
	private static final ArrayList<String> VOCI_MENU_VOLUNTEER = new ArrayList<>(Arrays.asList(
			"Visualizza Volontari",
		    "Rimuovi Volontario",
		    "Cerca Volontario",
		    "Apri raccolta disponibilità volontari",
		    "Indietro"
	));
	private static final String MSG_TITLE_MENU="Inserisci una delle seguenti opzioni: ";
	private static final ArrayList<String> VOCI_MENU_GENERALE = new ArrayList<>(Arrays.asList(
		    "Gestisci i luoghi",
		    "Gestisci i tipi di visita",
		    "Gestisci le visite",
		    "Gestisci i volontari",
		    "Fai scorrere il tempo",
		    "Esci"
	));
	private static final String MSG_PRINT_DATE="+++++ OGGI E' IL GIORNO: %s +++++ \n \n"; 
	private static final String MSG_USER_LOGGED_DATA= "\n \n "+"+++++ CIAO %s %s +++++";
	private static final String MSG_EXIT="\n"+"+++++ ARRIVEDERCI %s %s +++++ \n";
	private static final String MSG_EXIT_NO_LOG="\n"+"+++++ ARRIVEDERCI +++++ \n";
	
	
	//stampa errore in caso di scelta non compresa da quelle disponibili
	public void printMenuChoiceError() {
		System.out.println(MSG_ERR_SCELTA_ERRATA);
	}
	
	//stampa menu luoghi
	public void printMenuPlace() {
		MenuLayout menuPlace= new MenuLayout(MSG_TITOLO_MENU_PLACES, VOCI_MENU_PLACES);
		System.out.print(menuPlace.visualizzaMenu());
	}
	
	//stampa menu tipi visita
	public void printMenuVisitType() {
		MenuLayout menuVisitType= new MenuLayout(MSG_TITOLO_MENU_VISITTYPE, VOCI_MENU_VISITTYPE);
		System.out.print(menuVisitType.visualizzaMenu());
	}
	
	//stampa menu visite
	public void printMenuVisitIstance() {
		MenuLayout menuVisit= new MenuLayout(MSG_TITOLO_MENU_VISIT, VOCI_MENU_VISIT);
		System.out.print(menuVisit.visualizzaMenu());
	}
	
	//stampa menu volontari
	public void printMenuVolunteer() {
		MenuLayout menuVolunteer= new MenuLayout(MSG_TITOLO_MENU_VOLUNTEER, VOCI_MENU_VOLUNTEER);
		System.out.print(menuVolunteer.visualizzaMenu());
	}
	
	//stampa menu principale
	public void printMainMenu() {
		MenuLayout menu= new MenuLayout(MSG_TITLE_MENU, VOCI_MENU_GENERALE);
		System.out.print(menu.visualizzaMenu());
	}
	
	//stampa dati utente loggato
	public void printUserLogged(Person user) {
		String printDataString= String.format(MSG_USER_LOGGED_DATA, user.getName(), user.getSurname());
		System.out.println(printDataString);
	}
	
	//stampa data corrente
	public void printCurrentDate(LocalDate currentDate) {
		String printDataString= String.format(MSG_PRINT_DATE,currentDate.toString());
		System.out.println(printDataString);
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
}
