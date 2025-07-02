package program.dashboard;

import java.time.LocalDate;


import java.util.EnumSet;
import java.util.Map;

import program.DataConsistencyService;
import program.dashboard.view.DashboardConfiguratorView;
import program.dashboard.view.DashboardView;
import program.places.PlaceView;
import program.places.Place;
import program.places.PlaceManager;
import program.users.Person;
import program.users.availability.OpenCloseAvailabilityView;
import program.users.availability.OpenCloseAvailabilityManager;
import program.users.volunteer.VolunteerView;
import program.users.volunteer.Volunteer;
import program.users.volunteer.VolunteerManager;
import program.visitistance.ExcludedDates;
import program.visitistance.VisitView;
import program.visitistance.VisitManager;
import program.visitistance.state.VisitStateEnum;
import program.visittype.VisitTypeView;
import program.visittype.VisitType;
import program.visittype.VisitTypeManager;
import utilities.RepositorySystem;
import utilities.TimeSimulator;

/*+++++DASHBOARD DEL PROGRAMMA PER IL CONFIGURATORE+++++
	CONTIENE IL MENU CON TUTTE LE VARIE FUNZIONALITA' PER IL CONFIGURATORE*/
public class DashboardConfigurator extends Dashboard{
	
	private final Person user;
	private final PlaceManager placeManager;
	private final VolunteerManager volunteerManager;
	private final VisitManager visitManager;
	private final DashboardConfiguratorView view;
	
	private Map<Integer, Runnable> placeActions; 
	private Map<Integer, Runnable> visitTypeActions; 
	private Map<Integer, Runnable> visitActions; 
	private Map<Integer, Runnable> volunteerActions;
	private Map<Integer, Runnable> menuActions; 
	
	public DashboardConfigurator(Person user, DashboardView viewAbstract, DashboardConfiguratorView view, OpenCloseAvailabilityView opView, VolunteerView volunteerView, PlaceView placeView, VisitView visitView, VisitTypeView visitTypeView, RepositorySystem repositoryVisitType, RepositorySystem repositoryPlaces, RepositorySystem repositoryExcludedDates, RepositorySystem repositoryAvail, RepositorySystem repositoryVisit, RepositorySystem repositoryVolunteer, RepositorySystem repositoryRegistration) {
		super(new TimeSimulator(LocalDate.now()), new VisitTypeManager(visitTypeView, placeView, repositoryVisitType, repositoryPlaces), new ExcludedDates(repositoryExcludedDates), new OpenCloseAvailabilityManager(opView, repositoryAvail), viewAbstract, repositoryVisitType, repositoryExcludedDates, repositoryVisit, repositoryRegistration);
		this.user=user;
		this.placeManager= new PlaceManager(placeView, repositoryPlaces);
		this.volunteerManager = new VolunteerManager(volunteerView, repositoryVolunteer);
		this.visitManager = new VisitManager(visitView, repositoryVisitType, repositoryExcludedDates, repositoryVisit);
		visitManager.checkDay(time, availabilityManager);
		this.view= view;
		
		this.initActions();
	}
	
	//metodo di gestione dei luoghi
	private void runPlace() {
		//stampo il menu per i luoghi e leggo l'opzione scelta
		view.printMenuPlace();
		int opz= viewDashboard.readChoiceMenu();
		
		Runnable action = placeActions.get(opz);
		
		if(action != null)
			action.run();
		else
			view.printMenuChoiceError();
	}

	//metodo di gestione dei tipi di visita
	private void runVisitType() {
		//stampo il menu per i tipi visita e leggo l'opzione scelta
		view.printMenuVisitType();
		int opz= viewDashboard.readChoiceMenu();
		
		Runnable action = visitTypeActions.get(opz);
		if(action != null)
			action.run();
		else
			view.printMenuChoiceError();	
	}

	//metodo per la selezione di date da escludere
	private void runVisit() {
		//stampo il menu per le visite e leggo l'opzione scelta
		view.printMenuVisitIstance();
		int opz= viewDashboard.readChoiceMenu();

		Runnable action = visitActions.get(opz);
		if(action != null)
			action.run();
		else
			view.printMenuChoiceError();
	}
	
	//metodo di gestione dei volontari
	private void runVolunteer() {
		//stampo il menu per i tipi visita e leggo l'opzione scelta
		view.printMenuVolunteer();
		int opz= viewDashboard.readChoiceMenu();
		
		Runnable action = volunteerActions.get(opz);
		if(action != null)
			action.run();
		else
			view.printMenuChoiceError();
	}

	//metodo che verifica se l'operazione può essere effettuata
	private void executeIfAvailable(Runnable action) {
		if(OpenCloseAvailabilityManager.getAvailabilityOpen()) {
			viewDashboard.printErrorAvailabilityNotOpen();
			return;
		}
		action.run();
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
	
	/*Runnable action = menuActions.get(opz);
		if(action != null)
			action.run();
	*/

	//imposto il messaggio di arrivederci per il configuratore
	@Override
	protected void printGoodbyeMessage() {
		view.printUserLogged(user);
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
	//inizializzatore principale
	private void initActions() {
		this.initPlaceActions();
		this.initVisitTypeActions();
		this.initVisitActions();
		this.initVolunteerActions();
		this.initMenuActions();
	}
	//inizializzatore menu luoghi
	private void initPlaceActions() {
		placeActions = Map.of(
				0, () -> {},
				1, () -> executeIfAvailable(() -> {
					volunteerManager.add(); //aggiungo un volontario
					visitTypeManager.add(placeManager.add(),volunteerManager.getArrayListVolunteerLastInsert()); //almeno un tipo visita associato al luogo => aggiungo un tipo visita dopo aver aggiunto un luogo
					}),
				2, () -> executeIfAvailable(() -> placeManager.modify()),
				3, () -> placeManager.view(),
				4, () -> executeIfAvailable(() -> {
					Place placeRemoved= placeManager.remove();
					if(placeRemoved!=null)
						DataConsistencyService.cleanUpRemovedPlace(visitTypeManager.getVisitTypeList(), volunteerManager.getVolunteerList(), placeRemoved);
				}),
				5, () -> placeManager.searchPlace()
		);
	}
	//inizializzatore menu visit type
	private void initVisitTypeActions() {
		visitTypeActions = Map.of(
			0, () -> {},
			1, () -> executeIfAvailable(() -> {
					volunteerManager.add();
					visitTypeManager.add(volunteerManager.getArrayListVolunteerLastInsert()); //aggiungo anche un volontario
				}),
			2, () -> executeIfAvailable(() -> {
					volunteerManager.addInModify(); //vuoi aggiungere un nuovo volontario
					visitTypeManager.modify(volunteerManager.getArrayListVolunteerLastInsert());
				}),
			3, () -> visitTypeManager.view(),
			4, () -> executeIfAvailable(() -> {
					VisitType visitTypeRemoved= visitTypeManager.remove();
					if(visitTypeRemoved!=null)
						DataConsistencyService.cleanUpRemovedVisitType(visitTypeManager.getVisitTypeList(), placeManager.getPlaceList(), volunteerManager.getVolunteerList(), visitTypeRemoved);
				}),
			5, () -> executeIfAvailable(() -> {
					volunteerManager.add();
					visitTypeManager.addVolunteer(volunteerManager.getArrayListVolunteerLastInsert());
				}),
			6, () -> visitTypeManager.searchVisitType()
		);
	}
	
	//inizializzatore menu visite
	private void initVisitActions() {
		visitActions = Map.of(
			0, () -> {},
			1, () -> visitManager.view(EnumSet.of(VisitStateEnum.PROPOSTA, VisitStateEnum.CONFERMATA, VisitStateEnum.EFFETTUATA, VisitStateEnum.COMPLETA, VisitStateEnum.CANCELLATA)),
			2, () -> excludedDates.excludeDate(time),
			3, () -> excludedDates.viewExcludedDate(time),
			4, () -> excludedDates.removeExcludedDate(time),
			5, () -> executeIfAvailable(() -> visitManager.setNumberOfSub())
		);
	}
	//inizializzatore menu volontari
	private void initVolunteerActions() {
		volunteerActions = Map.of(
			0, () -> {},
			1, () -> volunteerManager.view(),
			2, () -> executeIfAvailable(() -> {
					Volunteer volunteerRemoved= volunteerManager.remove();
					if(volunteerRemoved!=null)
						DataConsistencyService.cleanUpRemovedVolunteer(visitTypeManager.getVisitTypeList(), placeManager.getPlaceList(), volunteerRemoved);
				}),
			3, () -> volunteerManager.searchVolunteer(),
			4, () -> availabilityManager.openAvailability(time)
		);
	}
	//inizializzatore menu 
	private void initMenuActions() {
		menuActions = Map.of(
			0, () -> {},
			1, () -> runPlace(),
			2, () -> runVisitType(),
			3, () -> runVisit(),
			4, () -> runVolunteer(),
			5, () -> runTimeSimulation()
		);
	}

}
