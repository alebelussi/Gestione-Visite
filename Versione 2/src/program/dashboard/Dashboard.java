package program.dashboard;

import utilities.InputDati;
import utilities.TimeSimulator;

public abstract class Dashboard {
	
	private static final String MSG_CONFERMA_AVANZAMENTO = "Vuoi confermare l'avanzamento? ";
	private static final String MSG_TITOLO_MENU_TIMESIMULATION="[ GESTIONE SIMULAZIONE TEMPO ] \n \n "+"Inserisci il numero di giorni di cui avanzare: ";
	
	protected TimeSimulator time;
	
	public Dashboard(TimeSimulator time) {
		this.time = time;
	}
	
	public abstract void run();

	//metodo di gestione dello scorrimento del tempo
	protected void runTimeSimulation() {
		int giorniAvanzamento;
		//leggo il numero di giorni in cui vuoi avanzare 
		do{
			giorniAvanzamento=InputDati.leggiInteroPositivo(MSG_TITOLO_MENU_TIMESIMULATION);
		}while(!InputDati.yesNo(MSG_CONFERMA_AVANZAMENTO));
	
		//avanzo il tempo
		time.advance(giorniAvanzamento);
	}
}
