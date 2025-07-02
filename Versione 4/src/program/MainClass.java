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
import program.users.Person;

//+++++CLASSE CHE EFFETTUA IL LOGIN E RIMANDA ALLA DASHBOARD+++++
public class MainClass {

	private static final String FILE_CONFIGURATORI = "json/configuratore.json";
	private static final String FILE_VOLONTARI = "json/volontario.json";
	private static final String FILE_FRUITORI = "json/fruitore.json";
	private static final String MSG_WELCOME="+++++ BENVENUTO NELL'APP +++++";

	public static void main(String[] args) {

		System.out.println(MSG_WELCOME);
		//faccio l'operazione di login

		Person loggato = SystemService.run(FILE_CONFIGURATORI, FILE_VOLONTARI, FILE_FRUITORI);

		//istanzio l'oggetto per gestire la dashboard e runno
		Dashboard app;

		if(loggato != null) {
			if(loggato.returnRole().equals("Configurator"))
				app = new DashboardConfigurator(loggato); //configuratore
			else if(loggato.returnRole().equals("Volunteer"))
				app = new DashboardVolunteer(loggato); //volontario
			else
				app = new DashboardUser(loggato); //utente
			app.run();
		}
	}

}
