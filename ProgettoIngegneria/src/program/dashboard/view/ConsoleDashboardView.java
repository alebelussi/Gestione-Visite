package program.dashboard.view;

import utilities.InputDati;

public class ConsoleDashboardView implements DashboardView{
	private static final String MSG_ERR_DISPONIBILITA_NO_APERTA = "Attenzione: prima devi aprire la raccolta delle disponibilità";
	private static final String MSG_CONFERMA_AVANZAMENTO = "Vuoi confermare l'avanzamento? ";
	private static final String MSG_TITOLO_MENU_TIMESIMULATION="[ GESTIONE SIMULAZIONE TEMPO ] \n \n "+"Inserisci il numero di giorni di cui avanzare: ";
	
	//messaggio di errore disponibilità non aperta
    public void printErrorAvailabilityNotOpen() {
        System.out.println(MSG_ERR_DISPONIBILITA_NO_APERTA);
    }

    //metodo che legge se avanzare o meno
    public boolean readConfirmAdvance() {
        return InputDati.yesNo(MSG_CONFERMA_AVANZAMENTO);
    }

    //metodo che legge il numero di giorni in cui avanzare
    public int readDaysAndvance() {
        return InputDati.leggiInteroPositivo(MSG_TITOLO_MENU_TIMESIMULATION);
    }
    
    //leggo la scelta del menu
  	public int readChoiceMenu() {
  		return InputDati.leggiInteroPositivoSenzaMessaggio();
  	}
}