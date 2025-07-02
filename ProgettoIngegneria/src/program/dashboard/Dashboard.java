package program.dashboard;

import program.dashboard.view.DashboardView;
import program.users.availability.OpenCloseAvailabilityManager;
import program.visitistance.ExcludedDates;
import program.visitistance.VisitManager;
import program.visitistance.ConsoleVisitView;
import program.visittype.VisitTypeManager;
import utilities.RepositorySystem;
import utilities.TimeSimulator;

public abstract class Dashboard {
	protected static final String FILE_VOLONTARI = "json/volontario.json";
	protected static final String FILE_ISCRIZIONI = "json/iscrizioni.json";
	protected static final String FILE_TIPO_VISITA = "json/tipoVisita.json";
	protected static final String FILE_DATE_ESCLUSE = "json/dateEscluse.json";
	protected static final String FILE_VISITE = "json/visite.json";
	protected static final String FILE_LUOGHI = "json/luoghi.json";
	protected static final String FILE_AVAIL = "json/statoDisponibilita.json";
	
	protected TimeSimulator time;
	protected VisitTypeManager visitTypeManager;
	protected ExcludedDates excludedDates;
	protected OpenCloseAvailabilityManager availabilityManager;
	protected DashboardView viewDashboard;
	private RepositorySystem repositoryVisitType;
	private RepositorySystem repositoryExcludedDates;
	private RepositorySystem repositoryVisit;
	private RepositorySystem repositoryRegister;
	

	public Dashboard(TimeSimulator time, VisitTypeManager visitTypeManager, ExcludedDates excludedDates, OpenCloseAvailabilityManager availabilityManager,DashboardView dashboardView, RepositorySystem repositoryVisitType, RepositorySystem repositoryExcludedDates, RepositorySystem repositoryVisit, RepositorySystem repositoryRegister) {
		this.time = time;
		this.visitTypeManager = visitTypeManager;
		this.excludedDates= excludedDates;
		this.availabilityManager = availabilityManager;
		this.viewDashboard = dashboardView;
		this.repositoryVisitType= repositoryVisitType;
		this.repositoryExcludedDates= repositoryExcludedDates;
		this.repositoryVisit= repositoryVisit;
		this.repositoryRegister= repositoryRegister;
	}

	//metodo final --> uguale per tutti i figli
	public final void runTimeSimulation() {
		int giorniAvanzamento;
		VisitManager visitManager=new VisitManager(new ConsoleVisitView(), repositoryVisitType, repositoryExcludedDates, repositoryVisit); 

		//condizione aggiunta: posso avanzare al giorno successivo solo dopo aver aperto
		//la raccolta delle disponibilità --> l'unico momento in cui la condizione è falsa è il 16 del mese dopo che è stata chiusa
		//quindi prima di avanzare, devo aprire la raccolta
		if(!OpenCloseAvailabilityManager.getAvailabilityOpen()) {
			viewDashboard.printErrorAvailabilityNotOpen();
			return;
		}

		do{	//leggo il numero di giorni in cui vuoi avanzare
			giorniAvanzamento= viewDashboard.readDaysAndvance();
		}while(!viewDashboard.readConfirmAdvance());

		//avanzo ed eventualmente genero le visite
		if(time.advance(giorniAvanzamento)) {
			availabilityManager.closeAvaialbility();
			visitManager.generateVisit(time);
		}

		visitManager.updateVisitState(time.getCurrentDate(), repositoryRegister);	//verifico se gli stati delle visite sono cambiati
	}
	
	//metodo run -> descrive l'algoritmo generale di gestione del menu per gli utenti dell'app 
	public final void run() {
	    
		int choice;
		do{
			//stampa messaggio di benvenuto e data corrente
			printWelcomeMessageAndCurrentDay();
			
			//stampo il menu
			getMainMenu();
			
			//leggo la scelta dell'utente
			choice= viewDashboard.readChoiceMenu();
			
			//esegui l'operazione selezionata dall'utente
			handleMenuChoice(choice);
			
		}while (choice !=0);
		
	    //stampa messaggio di arrivederci
		printGoodbyeMessage();
	    
	}
	
	//metodi che rappresentano i dettagli dell'algoritmo generale, definiti dalle classi figlie 
    protected abstract void getMainMenu();
    protected abstract void handleMenuChoice(int choice);
    protected abstract void printGoodbyeMessage();
    protected abstract void printWelcomeMessageAndCurrentDay();
}
