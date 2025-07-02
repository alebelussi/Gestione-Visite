/*
 * @author Belussi Alessandro --- Matricola: 742360
 * @author Franzoni Tommaso --- Matricola: 740892
 * @author Ingiaimo Vincenzo  --- Matricola: 741831
 */
package program;

import program.users.Configurator;

//+++++CLASSE CHE EFFETTUA IL LOGIN E RIMANDA ALLA DASHBOARD+++++
public class MainClass {
	
	private static final String FILE_CONFIGURATORI = "json/configuratore.json";
	private static final String MSG_WELCOME="+++++ BENVENUTO NELL'APP +++++";
	
	public static void main(String[] args) {

		System.out.println(MSG_WELCOME);
		Configurator loggato = SystemService.login(FILE_CONFIGURATORI);
		
		//istanzio l'oggetto per gestire la dashboard e runno
		Dashboard app = new Dashboard(loggato);
		app.run();		
	}
	
}
