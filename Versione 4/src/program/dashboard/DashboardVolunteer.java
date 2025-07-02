package program.dashboard;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import program.DataConsistencyService;
import program.SystemService;
import program.users.OpenCloseAvailabilityManager;
import program.users.Person;
import program.users.Volunteer;
import program.users.VolunteerManager;
import program.visitFormatter.VisitFormatter;
import program.visitFormatter.VolunteerScheduledVisitFormatter;
import program.visitistance.ExcludedDates;
import program.visitistance.VisitManager;
import program.visitistance.VisitState;
import program.visittype.VisitTypeManager;
import utilities.InputDati;
import utilities.MenuLayout;
import utilities.TimeSimulator;

/*+++++DASHBOARD DEL PROGRAMMA PER IL VOLONTARIO+++++
CONTIENE IL MENU CON TUTTE LE VARIE FUNZIONALITA' PER IL VOLONTARIO*/
public class DashboardVolunteer extends Dashboard{

	private static final String FILE_DATE_ESCLUSE = "json/dateEscluse.json";

	private static final String MSG_PRINT_DATE="\n \n "+"+++++ CIAO %s %s +++++ \n "+"+++++ OGGI E' IL GIORNO: %s +++++ \n \n";
	private static final String MSG_WELCOME="Inserisci una delle seguenti opzioni: ";
	private static final String MSG_EXIT="\n"+"+++++ ARRIVEDERCI %s %s +++++ \n";
	private static final ArrayList<String> VOCI_MENU_GENERALE = new ArrayList<>(Arrays.asList(
		    "Inserisci le disponibilità",
		    "Visualizza le disponibilità inserite",
		    "Rimuovi la disponbilità inserita",
		    "Visualizza i tipi visita",
		    "Visualizza le visite confermate",
		    "Fai scorrere il tempo",
		    "Esci"
		));

	private Volunteer user;
	private final VolunteerManager volunteerManager;
	private final VisitManager visitManager;
	private Map<VisitState, VisitFormatter> customFormatter;

	public DashboardVolunteer(Person user) {
		super(new TimeSimulator(LocalDate.now()), new VisitTypeManager(), new ExcludedDates(FILE_DATE_ESCLUSE), new OpenCloseAvailabilityManager());
		this.user = (Volunteer) user;
		this.volunteerManager = new VolunteerManager();
		this.visitManager = new VisitManager();
		this.customFormatter = new HashMap<>();
		this.customFormatter.put(VisitState.CONFERMATA, new VolunteerScheduledVisitFormatter());
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
				case 1: { 	//inserimento disponibilità
					volunteerManager.addAvailability(user, time, excludedDates, visitTypeManager.getVisitTypeList());
					DataConsistencyService.updateAvailabilityInVisitType(volunteerManager.getVolunteerList(), visitTypeManager.getVisitTypeList());	//aggiorno i dati nel tipo visita associato
					this.user = volunteerManager.getVolunteerList().getVolunteer(user.getNickname());	//aggiorno il volontario utente
					break;
				}
				case 2: { 	//visualizza disponbilità
					volunteerManager.viewAvailability(user);
					break;
				}
				case 3: {	//rimozione disponibilità
					volunteerManager.removeAvailability(user, time);
					DataConsistencyService.updateAvailabilityInVisitType(volunteerManager.getVolunteerList(), visitTypeManager.getVisitTypeList());
					this.user = volunteerManager.getVolunteerList().getVolunteer(user.getNickname());
					break;
				}
				case 4: {	//visualizza tipi visita associati al volontario
					visitTypeManager.viewVisitTypeForVolunteer(user, visitTypeManager.getVisitTypeList());
					break;
				}
				case 5: {	//visualizza le visite confermate
					visitManager.viewWithCustomFormatter(EnumSet.of(VisitState.CONFERMATA), customFormatter);
					break;
				}
				case 6: {
					runTimeSimulation();
				}
				default:
					System.out.println(String.format(MSG_EXIT, user.getName(),user.getSurname()));
			}

		}while(opz!=0);
	}
}
