/*
 * @author Belussi Alessandro --- Matricola: 742360
 * @author Franzoni Tommaso --- Matricola: 740892
 * @author Ingiaimo Vincenzo  --- Matricola: 741831
 */
package program;

import program.dashboard.Dashboard;
import program.dashboard.DashboardConfigurator;
import program.dashboard.DashboardUser;
import program.dashboard.DashboardVolunteer;
import program.dashboard.view.ConsoleDashboardConfiguratorView;
import program.dashboard.view.ConsoleDashboardUserView;
import program.dashboard.view.ConsoleDashboardView;
import program.dashboard.view.ConsoleDashboardVolunteerView;
import program.login.LoginManager;
import program.login.ConsoleLoginView;
import program.places.ConsolePlaceView;
import program.registration.ConsoleRegistrationView;
import program.users.Person;
import program.users.availability.ConsoleOpenCloseAvailabilityView;
import program.users.volunteer.ConsoleVolunteerView;
import program.visitistance.ConsoleVisitView;
import program.visittype.ConsoleVisitTypeView;
import utilities.JsonManager;

//+++++CLASSE CHE EFFETTUA IL LOGIN E RIMANDA ALLA DASHBOARD+++++
public class MainClass {

	private static final String FILE_FRUITORE = "json/fruitore.json";
	private static final String FILE_VOLONTARIO = "json/volontario.json";
	private static final String FILE_CONFIGURATORE = "json/configuratore.json";
	private static final String FILE_TIPOVISITA = "json/tipoVisita.json";
	private static final String FILE_LUOGHI = "json/luoghi.json";
	private static final String FILE_DATE_ESCLUSE = "json/dateEscluse.json";
	private static final String FILE_AVAIL = "json/statoDisponibilita.json";
	private static final String FILE_VISITE = "json/visite.json";
	private static final String FILE_VOLONTARI = "json/volontario.json";
	private static final String FILE_ISCRIZIONI = "json/iscrizioni.json";
	
	public static void main(String[] args) {
		
		//controllo per la gestione della data corrente, dovuto al fatto che l'avanzamento è statico
		DataConsistencyService.startControl(new JsonManager(FILE_AVAIL));
		
		LoginManager loginManager= new LoginManager(new ConsoleLoginView(), new JsonManager(FILE_CONFIGURATORE), new JsonManager(FILE_VOLONTARIO), new JsonManager(FILE_FRUITORE), new JsonManager(FILE_TIPOVISITA));
		
		//faccio l'operazione di login
		Person loggato = loginManager.login();
		
		//istanzio l'oggetto per gestire la dashboard
		Dashboard app;

		if(loggato != null) {
			if(loggato.getRole().equals("Configurator"))
				app = new DashboardConfigurator(loggato, new ConsoleDashboardView(), new ConsoleDashboardConfiguratorView(), new ConsoleOpenCloseAvailabilityView(), new ConsoleVolunteerView(), new ConsolePlaceView(), new ConsoleVisitView(), new ConsoleVisitTypeView(), new JsonManager(FILE_TIPOVISITA), new JsonManager(FILE_LUOGHI), new JsonManager(FILE_DATE_ESCLUSE), new JsonManager(FILE_AVAIL), new JsonManager(FILE_VISITE), new JsonManager(FILE_VOLONTARI), new JsonManager(FILE_ISCRIZIONI)); //configuratore
			else if(loggato.getRole().equals("Volunteer"))
				app = new DashboardVolunteer(loggato, new ConsoleDashboardView(), new ConsoleDashboardVolunteerView(), new ConsoleOpenCloseAvailabilityView(), new ConsoleVolunteerView(), new ConsoleVisitView(), new ConsolePlaceView(), new ConsoleVisitTypeView(), new JsonManager(FILE_TIPOVISITA), new JsonManager(FILE_LUOGHI), new JsonManager(FILE_DATE_ESCLUSE), new JsonManager(FILE_AVAIL), new JsonManager(FILE_VISITE), new JsonManager(FILE_VOLONTARI), new JsonManager(FILE_ISCRIZIONI)); //volontario
			else
				app = new DashboardUser(loggato, new ConsoleDashboardView(), new ConsoleDashboardUserView(), new ConsoleOpenCloseAvailabilityView(), new ConsoleRegistrationView(), new ConsoleVisitView(), new ConsolePlaceView(), new ConsoleVisitTypeView(), new JsonManager(FILE_TIPOVISITA), new JsonManager(FILE_LUOGHI), new JsonManager(FILE_DATE_ESCLUSE), new JsonManager(FILE_AVAIL), new JsonManager(FILE_VISITE), new JsonManager(FILE_VOLONTARI), new JsonManager(FILE_ISCRIZIONI)); //utente
			
			//runno l'applicazione
			app.run();
		}
	}

}
