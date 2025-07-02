package program.dashboard;

import program.SystemService;
import program.users.OpenCloseAvailabilityManager;
import program.visitistance.ExcludedDates;
import program.visitistance.VisitList;
import program.visittype.VisitTypeManager;
import utilities.InputDati;
import utilities.TimeSimulator;

public abstract class Dashboard {

	private static final String MSG_ERR_DISPONIBILITA_NO_APERTA = "Attenzione: prima devi aprire la raccolta delle disponibilità";
	private static final String MSG_CONFERMA_AVANZAMENTO = "Vuoi confermare l'avanzamento? ";
	private static final String MSG_TITOLO_MENU_TIMESIMULATION="[ GESTIONE SIMULAZIONE TEMPO ] \n \n "+"Inserisci il numero di giorni di cui avanzare: ";
	private static final String FILE_VISITE = "json/visite.json";

	protected TimeSimulator time;
	protected VisitTypeManager visitTypeManager;
	protected ExcludedDates excludedDates;
	protected OpenCloseAvailabilityManager availabilityManager;

	public Dashboard(TimeSimulator time, VisitTypeManager visitTypeManager, ExcludedDates excludedDates, OpenCloseAvailabilityManager availabilityManager) {
		this.time = time;
		this.visitTypeManager = visitTypeManager;
		this.excludedDates= excludedDates;
		this.availabilityManager = availabilityManager;
		SystemService.updateVisitState(time.getCurrentDate()); //verifico se gli stati delle visite sono cambiati
	}

	public abstract void run();

	public void runTimeSimulation() {
		int giorniAvanzamento;
		VisitList visitList = new VisitList(FILE_VISITE);

		//condizione aggiunta: posso avanzare al giorno successivo solo dopo aver aperto
		//la raccolta delle disponibilità --> l'unico momento in cui la condizione è falsa è il 16 del mese dopo che è stata chiusa
		//quindi prima di avanzare, devo aprire la raccolta
		if(!OpenCloseAvailabilityManager.getAvailabilityOpen()) {
			System.out.println(MSG_ERR_DISPONIBILITA_NO_APERTA);
			return;
		}

		do{	//leggo il numero di giorni in cui vuoi avanzare
			giorniAvanzamento=InputDati.leggiInteroPositivo(MSG_TITOLO_MENU_TIMESIMULATION);
		}while(!InputDati.yesNo(MSG_CONFERMA_AVANZAMENTO));

		//avanzo ed eventualmente genero le visite
		if(time.advance(giorniAvanzamento)) {
			availabilityManager.closeAvaialbility();
			SystemService.generateVisit(time,visitTypeManager.getVisitTypeList(), visitList, excludedDates.getArrayListExcludedDate());
		}

		SystemService.updateVisitState(time.getCurrentDate());	//verifico se gli stati delle visite sono cambiati

	}
}
