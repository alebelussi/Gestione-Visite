package program.visitistance;

import java.util.HashMap;
import java.util.Map;

import program.visitFormatter.DeletedVisitFormatter;
import program.visitFormatter.ScheduledVisitFormatter;
import program.visitFormatter.VisitFormatter;
import program.visitistance.state.VisitStateEnum;
import utilities.InputDati;

public class ConsoleVisitView implements VisitView{
	
	private static final String MSG_ERR_VISITA_NON_TROVATA = "Errore...visita non trovata!";
	private static final String MSG_CONFERMA_RICERCA = "Vuoi cercare la visita?";
	private static final String MSG_INS_CODICE = "Inserisci il codice della visita da cercare: ";
	private static final String MSG_ERR_NESSUNA_VISITA_DISPONIBILE = "Nessuna visita disponibile...";
	private static final String MSG_INS_PERSONE_ISCRIVIBILI_SINGOLA_SESSIONE = "Inserisci il numero di persone iscrivibili per singola iscrizione: ";
	private static final String MSG_INS_PERSONE_ISCRIVIBILI = "Numero impostato di persone iscrivibili per singola iscrizione: ";
	private static final String MSG_CONFERMA_NUMERO = "Vuoi inserire il numero di persone iscrivibili per singola iscrizione?";
	
	private Map<VisitStateEnum, VisitFormatter> visitFormatter;
	
	public ConsoleVisitView() {
		this.visitFormatter = new HashMap<>();

		visitFormatter.put(VisitStateEnum.CONFERMATA, new ScheduledVisitFormatter());
		visitFormatter.put(VisitStateEnum.PROPOSTA, new ScheduledVisitFormatter());
		visitFormatter.put(VisitStateEnum.CANCELLATA, new DeletedVisitFormatter());
		visitFormatter.put(VisitStateEnum.COMPLETA, new ScheduledVisitFormatter());
		visitFormatter.put(VisitStateEnum.EFFETTUATA, new DeletedVisitFormatter());
	}
	
	public Map<VisitStateEnum, VisitFormatter> getVisitFormatter() {
		return this.visitFormatter;
	}
	
	public int askCodeVisit() {
		return InputDati.leggiInteroConMinimo(MSG_INS_CODICE, 0);
	}
	
	public void showNoVisitAvailable() {
		System.out.println(MSG_ERR_NESSUNA_VISITA_DISPONIBILE);
	}
	
	public void showVisitNotFound() {
		System.out.println(MSG_ERR_VISITA_NON_TROVATA);
	}
	
	public void showVisit(Visit visit) {
		VisitFormatter formatter = visitFormatter.get(visit.getStateEnum());
		System.out.println(formatter.format(visit));
	}
	
	public void showVisit(Visit visit, Map<VisitStateEnum, VisitFormatter> visitFormatter) {
		VisitFormatter formatter = visitFormatter.get(visit.getStateEnum());
		System.out.println(formatter.format(visit));
	}
	
	public boolean confirmSearch() {
		return InputDati.yesNo(MSG_CONFERMA_RICERCA);
	}
	
	//metodo che legge il numero di iscrizioni massime possibili
	public int readNumberOfSub() {
		int numberOfSub;
		do {
			if(Visit.getNumberOfSub() != 0)
				System.out.println(MSG_INS_PERSONE_ISCRIVIBILI + Visit.getNumberOfSub());
			numberOfSub = InputDati.leggiInteroConMinimo(MSG_INS_PERSONE_ISCRIVIBILI_SINGOLA_SESSIONE, 1);
		}while(!InputDati.yesNo(MSG_CONFERMA_NUMERO));
		
		return numberOfSub;
	}
}
