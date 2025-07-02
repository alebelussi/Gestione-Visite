package program.dashboard;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;

import program.SystemService;
import program.registration.RegistrationManager;
import program.users.OpenCloseAvailabilityManager;
import program.users.Person;
import program.users.User;
import program.visitistance.ExcludedDates;
import program.visitistance.VisitManager;
import program.visitistance.VisitState;
import program.visittype.VisitTypeManager;
import utilities.InputDati;
import utilities.MenuLayout;
import utilities.TimeSimulator;

public class DashboardUser extends Dashboard {

	private static final String MSG_TITOLO_GESTIONE_ISCRIZIONI = "[ GESTIONE ISCRIZIONI ] \n \n ";
	private static final String MSG_TITOLO_GESTIONE_VISITE = "[ GESTIONE VISITE ] \n \n ";
	private static final String FILE_DATE_ESCLUSE = "json/dateEscluse.json";
	private static final String MSG_PRINT_DATE="\n \n "+"+++++ CIAO %s %s +++++ \n "+"+++++ OGGI E' IL GIORNO: %s +++++ \n \n";
	private static final String MSG_WELCOME="Inserisci una delle seguenti opzioni: ";
	private static final String MSG_EXIT="\n"+"+++++ ARRIVEDERCI %s %s +++++ \n";
	private static final ArrayList<String> VOCI_MENU_GENERALE = new ArrayList<>(Arrays.asList(
		    "Gestisci le Visite",
		    "Gestisci le Iscrizioni",
		    "Fai scorrere il tempo",
		    "Esci"
		));
	private static final ArrayList<String> VOCI_MENU_VISITE = new ArrayList<>(Arrays.asList(
		    "Visualizza Visite",
		    "Cerca Visita",
		    "Indietro"
		));
	private static final ArrayList<String> VOCI_MENU_ISCRIZIONI = new ArrayList<>(Arrays.asList(
		    "Iscrizione Visita",
		    "Disdici Iscrizione",
		    "Cerca Iscrizione",
		    "Visualizza Iscrizioni",
		    "Indietro"
		));

	private User user;
	private RegistrationManager registrationManager;
	private VisitManager visitManager;

	public DashboardUser(Person user) {
		super(new TimeSimulator(LocalDate.now()), new VisitTypeManager(), new ExcludedDates(FILE_DATE_ESCLUSE), new OpenCloseAvailabilityManager());
		this.user = (User) user;
		this.visitManager = new VisitManager();
		this.registrationManager = new RegistrationManager(visitManager);
		SystemService.checkDay(time, visitTypeManager.getVisitTypeList(), availabilityManager, excludedDates.getArrayListExcludedDate());
	}

	@Override
	public void run() {
		MenuLayout menu= new MenuLayout(MSG_WELCOME,VOCI_MENU_GENERALE);
		int opz;

		//ciclo di esecuzione
		do {
			System.out.println(String.format(MSG_PRINT_DATE,user.getName(),user.getSurname(),time.getCurrentDate().toString()));
			opz=InputDati.leggiIntero(menu.visualizzaMenu(), 0, menu.getNumVoci()-1);

			switch (opz) {
				case 1: { 	//gestione visite
					runVisit();
					break;
				}
				case 2: { 	//gestione iscrizoni
					runRegistration();
					break;
				}
				case 3: { //avanza nel tempo
					runTimeSimulation();
					break;
				}
				default: //uscita
					System.out.println(String.format(MSG_EXIT, user.getName(),user.getSurname()));
			}

		}while(opz!=0);
	}


	//metodo che gestisce il menu per la gestione delle visite
	private void runVisit() {
		MenuLayout menu= new MenuLayout(MSG_TITOLO_GESTIONE_VISITE+MSG_WELCOME,VOCI_MENU_VISITE);
		int opz;

		opz=InputDati.leggiIntero(menu.visualizzaMenu(), 0, menu.getNumVoci()-1);
		switch (opz) {
			case 1: {	//visualizza visite
				visitManager.view(EnumSet.of(VisitState.PROPOSTA, VisitState.CANCELLATA, VisitState.CONFERMATA));
				break;
			}
			case 2: {	//cerca visita
				visitManager.searchVisit();
				break;
			}
		}
	}

	//metodo che gestisce il menu per la gestione delle iscrizioni
	private void runRegistration() {
		MenuLayout menu= new MenuLayout(MSG_TITOLO_GESTIONE_ISCRIZIONI+MSG_WELCOME,VOCI_MENU_ISCRIZIONI);
		int opz;

		opz=InputDati.leggiIntero(menu.visualizzaMenu(), 0, menu.getNumVoci()-1);
		switch (opz) {
			case 1: { 	//iscrizione visita
				registrationManager.add(user, time.getCurrentDate());
				break;
			}
			case 2: { 	//disdici iscrizone
				registrationManager.remove(user, time.getCurrentDate());
				break;
			}
			case 3: {	//cerca iscrizione
				registrationManager.searchRegistration();
				break;
			}
			case 4: {	//visualizza iscrizione
				registrationManager.view(user, EnumSet.of(VisitState.PROPOSTA, VisitState.CANCELLATA, VisitState.CONFERMATA));
				break;
			}
		}
	}
}

