package program.dashboard;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;

import program.DataConsistencyService;
import program.SystemService;
import program.places.PlaceManager;
import program.users.OpenCloseAvailabilityManager;
import program.users.Person;
import program.users.VolunteerManager;
import program.visitistance.ExcludedDates;
import program.visitistance.VisitManager;
import program.visitistance.VisitState;
import program.visittype.VisitTypeManager;
import utilities.InputDati;
import utilities.MenuLayout;
import utilities.TimeSimulator;

/*+++++DASHBOARD DEL PROGRAMMA PER IL CONFIGURATORE+++++
	CONTIENE IL MENU CON TUTTE LE VARIE FUNZIONALITA' PER IL CONFIGURATORE*/
public class DashboardConfigurator extends Dashboard{

	private static final String MSG_ERR_DISPONIBILITA_APERTA_NO_OPERATION = "Attenzione: non puoi svolgere questa operazione adesso! \nLa raccolta delle disponibilità dei volontari è aperta";
	private static final String FILE_DATE_ESCLUSE = "json/dateEscluse.json";

	private final Person user;
	private final PlaceManager placeManager;
	private final VolunteerManager volunteerManager;
	private final VisitManager visitManager;

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
	private static final String MSG_TITOLO_MENU_VOLUNTEER="[ GESTIONE VOLONTARI ] \n \n "+"Inserisci una delle seguenti opzioni: ";
	private static final ArrayList<String> VOCI_MENU_VOLUNTEER = new ArrayList<>(Arrays.asList(
			"Visualizza Volontari",
		    "Rimuovi Volontario",
		    "Cerca Volontario",
		    "Apri raccolta disponibilità volontari",
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

//	private static final String MSG_TITOLO_MENU_TIMESIMULATION="[ GESTIONE SIMULAZIONE TEMPO ] \n \n "+"Inserisci il numero di giorni di cui avanzare: ";

	public DashboardConfigurator(Person user) {
		super(new TimeSimulator(LocalDate.now()), new VisitTypeManager(), new ExcludedDates(FILE_DATE_ESCLUSE), new OpenCloseAvailabilityManager());
		this.user=user;
		this.placeManager= new PlaceManager();
		this.volunteerManager = new VolunteerManager();
		this.visitManager = new VisitManager();
		SystemService.checkDay(this.time, visitTypeManager.getVisitTypeList(), availabilityManager, excludedDates.getArrayListExcludedDate());
	}


	//+++++ MENU DOVE SONO DISPONIBILI LE VARIE OPERAZIONI +++++++++
	@Override
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
				executeIfAvailable(() -> {
					volunteerManager.add(); //aggiungo un volontario
					visitTypeManager.add(placeManager.add(),volunteerManager.getArrayListVolunteerLastInsert()); //almeno un tipo visita associato al luogo => aggiungo un tipo visita dopo aver aggiunto un luogo
				});
				break;
			}
			case 2: { //MODIFICA
				executeIfAvailable(() -> placeManager.modify());
				break;
			}
			case 3: { //VISUALIZZA
				placeManager.view();
				break;
			}
			case 4: { //RIMOZIONE
				executeIfAvailable(() -> DataConsistencyService.cleanUpRemovedPlace(visitTypeManager.getVisitTypeList(), volunteerManager.getVolunteerList(), placeManager.remove()));
				break;
			}
			case 5: { //CERCA UN LUOGO
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
				executeIfAvailable(() -> {
					volunteerManager.add();
					visitTypeManager.add(volunteerManager.getArrayListVolunteerLastInsert()); //aggiungo anche un volontario
				});
				break;
			}
			case 2:{ //MODIFICA
				executeIfAvailable(() -> {
					volunteerManager.addInModify(); //vuoi aggiungere un nuovo volontario
					visitTypeManager.modify(volunteerManager.getArrayListVolunteerLastInsert());
				});
				break;
			}
			case 3:{ //VISUALIZZA
				visitTypeManager.view();
				break;
			}
			case 4: { //RIMOZIONE
				executeIfAvailable(() -> {
					DataConsistencyService.cleanUpRemovedVisitType(visitTypeManager.getVisitTypeList(), placeManager.getPlaceList(), volunteerManager.getVolunteerList(), visitTypeManager.remove());
				});
				break;
			}
			case 5:{ //ASSOCIAZIONE NUOVI VOLONTARI
				executeIfAvailable(() -> {
					volunteerManager.add();
					visitTypeManager.addVolunteer(volunteerManager.getArrayListVolunteerLastInsert());
				});
				break;
			}
			case 6:{ //CERCA
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
				visitManager.view(EnumSet.of(VisitState.PROPOSTA, VisitState.CONFERMATA, VisitState.EFFETTUATA, VisitState.COMPLETA, VisitState.CANCELLATA));
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
				executeIfAvailable(() -> SystemService.setNumberOfSub());
				break;
			}
		}
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
			case 2:{ //RIMOZIONE
				executeIfAvailable(() -> {
					DataConsistencyService.cleanUpRemovedVolunteer(visitTypeManager.getVisitTypeList(), placeManager.getPlaceList(), volunteerManager.remove());
				});
				break;
			}
			case 3:{ //CERCA
				volunteerManager.searchVolunteer();
				break;
			}
			case 4:{ //APERTURA DISPONIBILITA
				availabilityManager.openAvailability(time);
				break;
			}
		}
	}

	//metodo che verifica se l'operazione può essere effettuata
	private void executeIfAvailable(Runnable action) {
		if(OpenCloseAvailabilityManager.getAvailabilityOpen()) {
			System.out.println(MSG_ERR_DISPONIBILITA_APERTA_NO_OPERATION);
			return;
		}
		action.run();
	}
}
