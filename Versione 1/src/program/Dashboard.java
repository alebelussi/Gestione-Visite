package program;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import program.places.PlaceManager;
import program.users.Configurator;
import program.users.VolunteerManager;
import program.visitistance.ExcludedDates;
import program.visitistance.VisitList;
import program.visittype.VisitTypeManager;
import utilities.InputDati;
import utilities.MenuLayout;
import utilities.TimeSimulator;

/*+++++DASHBOARD DEL PROGRAMMA+++++
	CONTIENE IL MENU CON TUTTE LE VARIE FUNZIONALITA' IN BASE AL TIPO DI UTENTE*/
public class Dashboard {
	
	private static final String MSG_CONFERMA_AVANZAMENTO = "Vuoi confermare l'avanzamento? ";
	private static final String FILE_VISITE = "json/visite.json";
	private static final String FILE_DATE_ESCLUSE = "json/dateEscluse.json";
	private final Configurator user;
	private final TimeSimulator time;
	private final PlaceManager placeManager;
	private final VisitTypeManager visitTypeManager;
	private final VolunteerManager volunteerManager; 
	private final ExcludedDates excludedDates;

	private static final String MSG_PRINT_DATE="\n \n "+"+++++ CIAO %s %s +++++ \n "+"+++++ OGGI E' IL GIORNO: %s +++++ \n \n";
	private static final String MSG_WELCOME="Inserisci una delle seguenti opzioni: ";
	private static final String MSG_EXIT="\n"+"+++++ ARRIVEDERCI %s %s +++++ \n";
	private static final ArrayList<String> VOCI_MENU_GENERALE = new ArrayList<>(Arrays.asList(
		    "Gestisci i luoghi", 
		    "Gestisci i tipi di visita", 
		    "Gestisci le visite",
		    "Gestisci i volontari",
		    "Fai scorrere il tempo",
		    "Esci"
		));
	private static final String MSG_TITOLO_MENU_PLACES="[ GESTIONE LUOGHI ] \n \n "+"Inserisci una delle seguenti opzioni: ";
	private static final ArrayList<String> VOCI_MENU_PLACES = new ArrayList<>(Arrays.asList(
		    "Inserisci Luogo",
		    "Modifica Luogo",
		    "Visualizza Luoghi",
		    "Cerca Luogo",
		    "Indietro"
		));
	private static final String MSG_TITOLO_MENU_VISITTYPE="[ GESTIONE TIPI VISITA ] \n \n "+"Inserisci una delle seguenti opzioni: ";
	private static final ArrayList<String> VOCI_MENU_VISITTYPE = new ArrayList<>(Arrays.asList(
		    "Inserisci Tipo Visita",
		    "Modifica Tipo Visita",
		    "Visualizza Tipi Visite",
		    "Cerca Tipo Visita",
		    "Indietro"
		));
	private static final String MSG_TITOLO_MENU_VOLUNTEER="[ GESTIONE VOLONTARI ] \n \n "+"Inserisci una delle seguenti opzioni: ";
	private static final ArrayList<String> VOCI_MENU_VOLUNTEER = new ArrayList<>(Arrays.asList(
		    "Visualizza Volontari",
		    "Cerca Volontario",
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
	
	private static final String MSG_TITOLO_MENU_TIMESIMULATION="[ GESTIONE SIMULAZIONE TEMPO ] \n \n "+"Inserisci il numero di giorni di cui avanzare: ";
	
	public Dashboard(Configurator user) {
		this.user=user;
		this.time=new TimeSimulator(LocalDate.now());
		this.visitTypeManager= new VisitTypeManager();
		this.placeManager= new PlaceManager(); 
		this.volunteerManager = new VolunteerManager();
		this.excludedDates= new ExcludedDates(FILE_DATE_ESCLUSE);
	}
	
	
	//+++++ MENU DOVE SONO DISPONIBILI LE VARIE OPERAZIONI +++++++++
	public void run() {
		MenuLayout menu= new MenuLayout(MSG_WELCOME,VOCI_MENU_GENERALE);
		int opz;
		
		//ciclo di esecuzione
		do {
			System.out.println(String.format(MSG_PRINT_DATE,user.getName(),user.getSurname(),time.getCurrentDate().toString()));
			opz=InputDati.leggiIntero(menu.visualizzaMenu(), 0, menu.getNumVoci()-1);
			
			switch (opz) {
				case 1: { //gestione luoghi
					runPlace();
					break;
				}
				case 2: { //gestione tipo visita
					runVisitType();
					break;
				}
				case 3: { //gestione visite
					runVisit();
					break;
				}
				case 4: { //gestione volontari
					runVolunteer();
					break;
				}
				case 5: { //gestione simulazione scorrimento del tempo
					runTimeSimulation();
					break;
				}
				default:
					System.out.println(String.format(MSG_EXIT, user.getName(),user.getSurname()));
			}
			
		}while(opz!=0);
	}
	
	//metodo di gestione dei luoghi
	private void runPlace() {
		MenuLayout menuPlace= new MenuLayout(MSG_TITOLO_MENU_PLACES, VOCI_MENU_PLACES);
		int opz=InputDati.leggiIntero(menuPlace.visualizzaMenu(), 0, menuPlace.getNumVoci()-1);
		
		switch (opz) {
			case 1: { //INSERIMENTO
				volunteerManager.add(); //aggiungo un volontario
				visitTypeManager.add(placeManager.add(),volunteerManager.getArrayListVolunteerLastInsert()); //almeno un tipo visita associato al luogo => aggiungo un tipo visita dopo aver aggiunto un luogo
				break;
			}
			case 2:{ //MODIFICA
				placeManager.modify();
				break;
			}
			case 3:{ //VISUALIZZA
				placeManager.view();
				break;
			}
			
			case 4: { //CERCA UN LUOGO
				placeManager.searchPlace();
				break;
			}
		}
	}
	
	//metodo di gestione dei tipi di visita
	private void runVisitType() {
		MenuLayout menuVisitType= new MenuLayout(MSG_TITOLO_MENU_VISITTYPE, VOCI_MENU_VISITTYPE);
		int opz=InputDati.leggiIntero(menuVisitType.visualizzaMenu(), 0, menuVisitType.getNumVoci()-1);
		
		switch (opz) {
			case 1: { //INSERIMENTO
				volunteerManager.add();
				visitTypeManager.add(volunteerManager.getArrayListVolunteerLastInsert()); //aggiungo anche un volontario
				break;
			}
			case 2:{ //MODIFICA
				volunteerManager.addInModify(); //vuoi aggiungere un nuovo volontario
				visitTypeManager.modify(volunteerManager.getArrayListVolunteerLastInsert());
				break;
			}
			case 3:{ //VISUALIZZA
				visitTypeManager.view();
				break;
			}
			case 4:{ //CERCA
				visitTypeManager.searchVisitType();
				break;
			}
		}
	}
	
	//metodo per la selezione di date da escludere 
	private void runVisit() {
		MenuLayout menuVisit= new MenuLayout(MSG_TITOLO_MENU_VISIT, VOCI_MENU_VISIT);
		int opz=InputDati.leggiIntero(menuVisit.visualizzaMenu(), 0, menuVisit.getNumVoci()-1);
		
		switch (opz) {
		
			case 1:{ //VISUALIZZA
				VisitList visitList= new VisitList(FILE_VISITE);
				visitList.viewVisit();
				break;
			}
			
			case 2:{ //escludi data
				excludedDates.excludeDate(time);
				break;
			}
				
			case 3:{ //visualizza date escluse
				excludedDates.viewExcludedDate(time);
				break;
			}
			
			case 4:{ //rimozione data esclusa
				excludedDates.removeExcludedDate(time);
				break;
			}
			
			case 5:{ //modifica numero iscritti
				SystemService.setNumberOfSub();
				break;
			}
		}
	}
	
	//metodo di gestione dello scorrimento del tempo
	private void runTimeSimulation() {
		int giorniAvanzamento;
		//leggo il numero di giorni in cui vuoi avanzare 
		do{
			giorniAvanzamento=InputDati.leggiInteroPositivo(MSG_TITOLO_MENU_TIMESIMULATION);
		}while(!InputDati.yesNo(MSG_CONFERMA_AVANZAMENTO));
	
		//avanzo il tempo
		time.advance(giorniAvanzamento);
	}
	
	//metodo di gestione dei volontari
	private void runVolunteer() {
		MenuLayout menuVolunteer= new MenuLayout(MSG_TITOLO_MENU_VOLUNTEER, VOCI_MENU_VOLUNTEER);
		int opz=InputDati.leggiIntero(menuVolunteer.visualizzaMenu(), 0, menuVolunteer.getNumVoci()-1);
		
		switch (opz) {
			case 1:{ //VISUALIZZA
				volunteerManager.view();
				break;
			}
			case 2:{ //CERCA
				volunteerManager.searchVolunteer();
				break;
			}
		}
	}
}
