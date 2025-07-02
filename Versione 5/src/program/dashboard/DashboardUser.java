package program.dashboard;

import java.time.LocalDate;

import java.util.EnumSet;
import java.util.Map;
import program.dashboard.view.DashboardUserView;
import program.dashboard.view.DashboardView;
import program.places.PlaceView;
import program.registration.RegistrationView;
import program.registration.RegistrationManager;
import program.users.Person;
import program.users.User;
import program.users.availability.OpenCloseAvailabilityView;
import program.users.availability.OpenCloseAvailabilityManager;
import program.visitistance.ExcludedDates;
import program.visitistance.VisitView;
import program.visitistance.VisitManager;
import program.visitistance.state.VisitStateEnum;
import program.visittype.VisitTypeView;
import program.visittype.VisitTypeManager;
import utilities.RepositorySystem;
import utilities.TimeSimulator;

public class DashboardUser extends Dashboard {

	private User user;
	private RegistrationManager registrationManager;
	private VisitManager visitManager;
	private DashboardUserView view; 
	
	private Map<Integer, Runnable> visitActions; 
	private Map<Integer, Runnable> registrationActions;
	private Map<Integer, Runnable> menuActions; 

	public DashboardUser(Person user, DashboardView viewAbstract, DashboardUserView view, OpenCloseAvailabilityView opView, RegistrationView registrationView, VisitView visitView, PlaceView placeView, VisitTypeView visitTypeView, RepositorySystem repositoryVisitType, RepositorySystem repositoryPlaces, RepositorySystem repositoryExcludedDates, RepositorySystem repositoryAvail, RepositorySystem repositoryVisit, RepositorySystem repositoryVolunteer, RepositorySystem repositoryRegister) {
		super(new TimeSimulator(LocalDate.now()), new VisitTypeManager(visitTypeView, placeView, repositoryVisitType, repositoryPlaces), new ExcludedDates( repositoryExcludedDates), new OpenCloseAvailabilityManager(opView, repositoryAvail), viewAbstract, repositoryVisitType, repositoryExcludedDates, repositoryVisit, repositoryRegister);
		this.user = (User) user;
		this.visitManager = new VisitManager(visitView, repositoryVisitType, repositoryExcludedDates, repositoryVisit);
		this.registrationManager = new RegistrationManager(visitManager, registrationView, visitView, repositoryRegister);
		visitManager.checkDay(time, availabilityManager);
		this.view= view;
		
		this.initActions();
	}

	//metodo che gestisce il menu per la gestione delle visite
	private void runVisit() {
		//stampo il menu per la gestione delle visite
		view.printMenuVisit();
		//leggo l'opzione che l'utente vuole eseguire
		int opz=viewDashboard.readChoiceMenu();
		
		Runnable action = visitActions.get(opz);
		if(action != null)
	    	action.run();
	    else
	       view.printMenuChoiceError();
	}

	//metodo che gestisce il menu per la gestione delle iscrizioni
	private void runRegistration() {
		//stampo il menu per la gestione delle registrazioni
		view.printMenuRegistration();
		//leggo l'operazione che l'utente vuole eseguire
		int opz= viewDashboard.readChoiceMenu();
		
		Runnable action = registrationActions.get(opz);
		if(action != null)
	    	action.run();
	    else
	       view.printMenuChoiceError();
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
	
	//imposto il messaggio di arrivederci per il fluitore
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
	//inizializzatore principale
	private void initActions() {
		this.initVisitActions();
		this.initRegistrationActions();
		this.initMenuActions();
	}
	//inizializzatore menu visite
	private void initVisitActions() {
		visitActions = Map.of(
			0, () -> {},
			1, () -> visitManager.view(EnumSet.of(VisitStateEnum.PROPOSTA, VisitStateEnum.CANCELLATA, VisitStateEnum.CONFERMATA)),
			2, () -> visitManager.searchVisit()
		);
	}
	//inizializzatore menu iscrizioni
	private void initRegistrationActions() {
		registrationActions = Map.of(
			0, () -> {},
			1, () -> registrationManager.add(user, time.getCurrentDate()),
			2, () -> registrationManager.remove(user, time.getCurrentDate()),
			3, () -> registrationManager.searchRegistration(),
			4, () -> registrationManager.view(user, EnumSet.of(VisitStateEnum.PROPOSTA, VisitStateEnum.CANCELLATA, VisitStateEnum.CONFERMATA))
		);
	}
	//inizializzatore menu
	private void initMenuActions() {
		menuActions = Map.of(
			0, () -> {},
			1, () -> runVisit(),
			2, () -> runRegistration(),
			3, () -> runTimeSimulation()
		);
	}
}

