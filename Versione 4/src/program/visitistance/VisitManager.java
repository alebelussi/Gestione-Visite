package program.visitistance;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import program.visitFormatter.DeletedVisitFormatter;
import program.visitFormatter.ScheduledVisitFormatter;
import program.visitFormatter.VisitFormatter;
import utilities.InputDati;

public class VisitManager {

	private static final String MSG_ERR_VISITA_NON_TROVATA = "Errore...visita non trovata!";
	private static final String MSG_CONFERMA_RICERCA = "Vuoi cercare la visita?";
	private static final String MSG_INS_CODICE = "Inserisci il codice della visita da cercare: ";
	private static final String FILE_VISITE = "json/visite.json";
	private VisitList visitList;
	private Map<VisitState, VisitFormatter> visitFormatter;

	public VisitManager() {
		this.visitList = new VisitList(FILE_VISITE);
		this.visitFormatter = new HashMap<>();

		visitFormatter.put(VisitState.CONFERMATA, new ScheduledVisitFormatter());
		visitFormatter.put(VisitState.PROPOSTA, new ScheduledVisitFormatter());
		visitFormatter.put(VisitState.CANCELLATA, new DeletedVisitFormatter());
		visitFormatter.put(VisitState.COMPLETA, new ScheduledVisitFormatter());
		visitFormatter.put(VisitState.EFFETTUATA, new DeletedVisitFormatter());
	}

	public Map<VisitState, VisitFormatter> getVisitFormatter() {
		return this.visitFormatter;
	}

	public VisitList getVisitList() {
		return this.visitList;
	}

	public void view(Set<VisitState> states) {
		this.visitList.viewVisit(states, visitFormatter);
	}

	public void viewWithCustomFormatter(Set<VisitState> states, Map<VisitState, VisitFormatter> formatter) {
		this.visitList.viewVisit(states, formatter);
	}

	public Visit searchVisit() {

		int codeVisit;

		do {
			codeVisit = InputDati.leggiInteroConMinimo(MSG_INS_CODICE, 0);
		}while(!InputDati.yesNo(MSG_CONFERMA_RICERCA));

		Visit visit = visitList.searchVisit(codeVisit);

		if(visit != null)
			System.out.println(visitFormatter.get(visit.getState()).format(visit));
		else
			System.out.println(MSG_ERR_VISITA_NON_TROVATA);

		return visit;
	}
}
