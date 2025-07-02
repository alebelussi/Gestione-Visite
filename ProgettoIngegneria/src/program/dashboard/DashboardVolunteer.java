package program.dashboard;

import java.time.LocalDate;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import program.DataConsistencyService;
import program.dashboard.view.DashboardView;
import program.dashboard.view.DashboardVolunteerView;
import program.places.PlaceView;
import program.users.Person;
import program.users.availability.OpenCloseAvailabilityView;
import program.users.availability.OpenCloseAvailabilityManager;
import program.users.volunteer.VolunteerView;
import program.users.volunteer.Volunteer;
import program.users.volunteer.VolunteerManager;
import program.visitFormatter.VisitFormatter;
import program.visitFormatter.VolunteerScheduledVisitFormatter;
import program.visitistance.ExcludedDates;
import program.visitistance.VisitView;
import program.visitistance.VisitManager;
import program.visitistance.state.VisitStateEnum;
import program.visittype.VisitTypeView;
import program.visittype.VisitTypeManager;
import utilities.RepositorySystem;
import utilities.TimeSimulator;

/*+++++DASHBOARD DEL PROGRAMMA PER IL VOLONTARIO+++++
CONTIENE IL MENU CON TUTTE LE VARIE FUNZIONALITA' PER IL VOLONTARIO*/
public class DashboardVolunteer extends Dashboard{

	private Volunteer user;
	private final VolunteerManager volunteerManager;
	private final VisitManager visitManager;
	private final DashboardVolunteerView view;
	private Map<VisitStateEnum, VisitFormatter> customFormatter;
	
	private Map<Integer, Runnable> menuActions;

	public DashboardVolunteer(Person user, DashboardView viewAbstract, DashboardVolunteerView view, OpenCloseAvailabilityView opView, VolunteerView volunteerView, VisitView visitView, PlaceView placeView, VisitTypeView visitTypeView,  RepositorySystem repositoryVisitType, RepositorySystem repositoryPlaces, RepositorySystem repositoryExcludedDates, RepositorySystem repositoryAvail, RepositorySystem repositoryVisit, RepositorySystem repositoryVolunteer, RepositorySystem repositoryRegister) {
		super(new TimeSimulator(LocalDate.now()), new VisitTypeManager(visitTypeView, placeView, repositoryVisitType,repositoryPlaces), new ExcludedDates(repositoryExcludedDates), new OpenCloseAvailabilityManager(opView, repositoryAvail), viewAbstract, repositoryVisitType, repositoryExcludedDates, repositoryVisit, repositoryRegister);
		this.user = (Volunteer) user;
		this.volunteerManager = new VolunteerManager(volunteerView, repositoryVolunteer);
		this.visitManager = new VisitManager(visitView, repositoryVisitType, repositoryExcludedDates, repositoryVisit);
		this.customFormatter = new HashMap<>();
		this.customFormatter.put(VisitStateEnum.CONFERMATA, new VolunteerScheduledVisitFormatter(repositoryRegister));
		visitManager.checkDay(time, availabilityManager);
		this.view= view;
		
		this.initMenuActions();
	}

	//metodo per inserire la disponibilità di un volontario
	private void insertAvailability() {
		volunteerManager.addAvailability(user, time, excludedDates, visitTypeManager.getVisitTypeList());
		DataConsistencyService.updateAvailabilityInVisitType(volunteerManager.getVolunteerList(), visitTypeManager.getVisitTypeList());	//aggiorno i dati nel tipo visita associato
		this.user = volunteerManager.getVolunteerList().getVolunteer(user.getNickname());	//aggiorno il volontario utente
	}
	
	//metodo per visualizzare le disponibilità del volontario
	private void viewAvailability() {
		volunteerManager.viewAvailability(user);
	}
	
	//metodo per visualizzare le visite confermate
	private void viewVisitConfirm() {
		visitManager.viewWithCustomFormatter(EnumSet.of(VisitStateEnum.CONFERMATA), customFormatter);
	}

	//metodo per visualizzare i tipi visita associati al volontario
	private void viewVolunteerVisitType() {
		visitTypeManager.viewVisitTypeForVolunteer(user, visitTypeManager.getVisitTypeList());
	}

	//metodo per visualizzare le disponibilità del volontario
	private void removeAvailability() {
		volunteerManager.removeAvailability(user, time);
		DataConsistencyService.updateAvailabilityInVisitType(volunteerManager.getVolunteerList(), visitTypeManager.getVisitTypeList());
		this.user = volunteerManager.getVolunteerList().getVolunteer(user.getNickname());
	}

	
//++++++++++++++++++++++++++++++++++ DETTAGLI DELL'ALGORITMO PER IL FUNZIONAMENTO DELL'APP ++++++++++++++++++++++++++++++++++
	
	//imposto le voci del menu 
	@Override
	protected void getMainMenu() {
		view.printMainMenu();
	}

	//imposto la lista di operazioni da eseguire a seconda dell'opzione scelta
	@Override
	protected void handleMenuChoice(int opz) {
	    Runnable action = menuActions.get(opz);
	    
	    if(action != null)
	    	action.run();
	    else
	       view.printMenuChoiceError();
	}

	//imposto il messaggio di arrivederci per il volontario
	@Override
	protected void printGoodbyeMessage() {
		view.printGoodbye(user);
	}

	//imposto il metodo per stampare la data corrente 
	@Override
	protected void printWelcomeMessageAndCurrentDay() {
	    //stampo i dati dell'utente loggato
		view.printUserLogged(user);
		//stampo la data corrente
		view.printCurrentDate(time.getCurrentDate());
	}
	
	//++++++++++++++++++++++++++++++++++ CREAZIONE DELLE MAPPA DEI MENU ++++++++++++++++++++++++++++++++++
	//inizializzatore menu
	private void initMenuActions() {
		menuActions = Map.of(
			0, () -> {},
			1, () -> insertAvailability(),
			2, () -> viewAvailability(),
			3, () -> removeAvailability(),
			4, () -> viewVolunteerVisitType(),
			5, () -> viewVisitConfirm(),
			6, () -> runTimeSimulation()
		);
	}
	
}
