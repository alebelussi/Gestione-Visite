package program.visitistance;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import program.users.availability.OpenCloseAvailabilityManager;
import program.users.volunteer.Volunteer;
import program.visitFormatter.VisitFormatter;
import program.visitistance.state.VisitStateEnum;
import utilities.RepositorySystem;
import utilities.TimeSimulator;


public class VisitManager {
	
	private VisitList visitList;
	private VisitView visitView;
	private VisitGenerator generator;
	private RepositorySystem repositoryVisitSystem;

	public VisitManager(VisitView visitView, RepositorySystem repositoryVisitTypeSystem, RepositorySystem repositoryExcludedDates, RepositorySystem repositoryVisitSystem) {
		this.visitList = new VisitList(repositoryVisitSystem);
		this.generator = new VisitGenerator(visitList, repositoryVisitTypeSystem, repositoryExcludedDates);
		this.visitView = visitView;
		this.repositoryVisitSystem= repositoryVisitSystem;
	}

	public VisitList getVisitList() {
		return this.visitList;
	}

	public void view(Set<VisitStateEnum> states) {
		visitList.loadVisit();
		List<Visit> filteredVisit = visitList.getVisitsByState(states);

		if(filteredVisit.isEmpty()) {
			visitView.showNoVisitAvailable();
			return;
		}

		for(Visit visit : filteredVisit) {
			visitView.showVisit(visit);
		}
	}

	public void viewWithCustomFormatter(Set<VisitStateEnum> states, Map<VisitStateEnum, VisitFormatter> formatter) {
		visitList.loadVisit();
		List<Visit> filteredVisit = visitList.getVisitsByState(states);

		if(filteredVisit.isEmpty()) {
			visitView.showNoVisitAvailable();
			return;
		}

		for(Visit visit : filteredVisit) {
			visitView.showVisit(visit, formatter);
		}
	}

	public Visit searchVisit() {

		int codeVisit;

		do {
			codeVisit = visitView.askCodeVisit();
		}while(!visitView.confirmSearch());

		Visit visit = visitList.searchVisit(codeVisit);

		if(visit != null)
			visitView.showVisit(visit);
		else
			visitView.showVisitNotFound();

		return visit;
	}
	
	//---------------------- GESTIONE DEL PROCESSO DI GENERAZIONE DELLE VISITE -----------------------------------
	
	//metodo per la generazione delle visite
	public void generateVisit(TimeSimulator time) {
		//genero le visite
		generator.generateVisit(time);
	}
		
	//metodo per verificare se la data di un tipo visita è associata ad una visita a cui un volontario partecipa volontario
	public boolean isDateOfVisitType(TimeSimulator time, Volunteer volunteer, LocalDate date, int plusMonths) {
		return generator.isDateOfVisitType(time, volunteer, date, plusMonths);
	}
	
	//metodo per aggiornare lo stato delle visite quando si aggiorna il giorno -> usato in dashboard
	public void updateVisitState(LocalDate time, RepositorySystem repositoryRegistrationSystem) {
		generator.updateVisitState(time, repositoryRegistrationSystem);
	}
	
	//metodo per aggiornare lo stato delle visite quando si aggiorna il giorno o il numero di partecipanti 
	public Visit updateVisitState(Visit visit, int participants, LocalDate date) {
	    return generator.updateVisitState(visit, participants, date);
	}
	
	//metodo che controlla se il giorno richiede la generazione automatica delle visite e in tal caso avvia la generazione 
	public void checkDay(TimeSimulator time, OpenCloseAvailabilityManager availabilityManager) {
		if(time.getNumberOfDay() == 16) {
			availabilityManager.closeAvaialbility();
			generator.generateVisit(time);
		}
	}
	
	//metodo statico per la modifica del numero massimo di iscrizioni effettuabili
	public void setNumberOfSub() {
		//leggo il valore da modificare
		int numberOfSub= visitView.readNumberOfSub();
		//modifico tale valore
		repositoryVisitSystem.modifyObject("numberOfSub", String.valueOf(Visit.getNumberOfSub()), numberOfSub);
		Visit.setNumberOfSub(numberOfSub);
	}

}
