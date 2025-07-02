package program.visitistance;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import program.DataConsistencyService;
import program.registration.RegistrationList;
import program.users.volunteer.Volunteer;
import program.visitistance.state.VisitStateEnum;
import program.visittype.VisitType;
import program.visittype.VisitTypeList;
import utilities.RepositorySystem;
import utilities.TimeSimulator;

//classe che si occupa di generare le visite
public class VisitGenerator {
	
	 private VisitTypeList visitTypeList;
	 private VisitList visitList;
	 private ExcludedDates excludedDates;
	 
	 public VisitGenerator(VisitList visitList, RepositorySystem repositoryVisitType, RepositorySystem repositoryExcludedDates) {
		 this.visitTypeList= new VisitTypeList(repositoryVisitType);
		 this.visitList= visitList;
		 this.excludedDates= new ExcludedDates(repositoryExcludedDates);
	 }
	 
	//metodo per la generazione delle visite
	public void generateVisit(TimeSimulator time) {
		for(VisitType visitType : visitTypeList.getVisitTypeList().values()) {

			LocalDate endDate = LocalDate.parse(visitType.getEndDate());
			LocalDate startDate = LocalDate.parse(visitType.getStartDate());
				
			for(DayOfWeek day : visitType.getDay()) {

				List<LocalDate> availableDates = getAvailableDate(time, day, startDate, endDate, 1);	//--> date in cui possono essere generate le visite

				for(LocalDate date : availableDates) {
					List<Volunteer> availableVolunteer = getAvailableVolunteer(visitType, date);	//-->volontari disponibili per questa data

					for(Volunteer selectedVolunteer : availableVolunteer) {
						ArrayList<Volunteer> volunteer = new ArrayList<>();
						volunteer.add(selectedVolunteer);
						VisitType visType = new VisitType(visitType.getTitle(), visitType.getDescription(), visitType.getMeetingPoint(),
																	visitType.getStartDate(), visitType.getEndDate(), visitType.getDay(), visitType.getStartHour(),
																	visitType.getDuration(), visitType.isBuyTicket(), visitType.getMinParticipant(), visitType.getMaxParticipant(),
																	visitType.getPlace(), volunteer);
						Visit visit = new Visit(visType, date.toString(), VisitStateEnum.PROPOSTA);

						if(!visitList.isVolunteerScheduled(selectedVolunteer.getNickname(), date.toString()) && !visitList.findVisit(visit))
							visitList.addVisit(visit);
						else
							Visit.decreaseGlobalCounter();
					}
				}
			}
		}
	}
	
	
	// metodo per individuare le disposinibilità del volontario
	private List<Volunteer> getAvailableVolunteer(VisitType visitType, LocalDate date){
		List<Volunteer> availableVolunteer = new ArrayList<>();
		for(Volunteer volElem : visitType.getVolunteer()) {
			if(volElem.getDateAvailability().contains(date))
				availableVolunteer.add(volElem);
		}
		return availableVolunteer;
	}
	
	
	//metodo per generare le date disponibili per una visita; tutti i vincoli sulla data sono applicati al suo interno
	private List<LocalDate> getAvailableDate(TimeSimulator time, DayOfWeek day, LocalDate startDate, LocalDate endDate, int plusMonths) {
		Set<LocalDate> dateSet = new HashSet<>(); //--> Memorizza le date ed evita automaticamente i duplicati
			
		LocalDate date = time.getCurrentDate().plusMonths(plusMonths).withDayOfMonth(1);  //--> Primo giorno del mese i+1

		LocalDate stopDate = date.withDayOfMonth(date.lengthOfMonth());  //--> Ultimo giorno del mese i+1

		date = date.with(TemporalAdjusters.nextOrSame(day)); //--> Avanza fino alla prima data del giorno (param day) specificato

		while(!date.isAfter(stopDate)) {	//--> Itera fino a quando date non è dopo stopDate e quindi fino al mese successivo
			if(!date.isBefore(startDate) && !date.isAfter(endDate)) {	//--> Verifica se è compresa nel periodo del tipo visita
				boolean excluededDate = false;	//--> Tiene traccia se la data è esclusa
				for(String excludedElem : excludedDates.getArrayListExcludedDate()) {	//--> Verifica se la data è esclusa
					if(date.equals(LocalDate.parse(excludedElem))) {
						excluededDate = true;
						break;	//--> Se la data è esclusa termino
					}
				}
				if(!excluededDate) //--> La data non è esclusa, viene inserita
					dateSet.add(date);
			}

			date = date.plusWeeks(1);
		}
		List<LocalDate> dateList = new ArrayList<>(dateSet);
		Collections.sort(dateList);
		return dateList;
	}
	
	//metodo che verifica se una certa data è una delle date in cui un volontario partecipa, senza le date escluse
	public boolean isDateOfVisitType(TimeSimulator time, Volunteer volunteer, LocalDate date, int plusMonths) {

		for (VisitType visitType : visitTypeList.getVisitTypeList().values()) {
			if (visitType.hasVolunteer(volunteer)) {
				for (DayOfWeek day : visitType.getDay()) {
					List<LocalDate> dates = getAvailableDate(
						time,
						day,
						LocalDate.parse(visitType.getStartDate()),
						LocalDate.parse(visitType.getEndDate()),
						plusMonths
					);

					if (dates.contains(date)) {
						return true; //la data è valida per questo volontario
					}
				}
			}
		}
		return false;
	}
	
	//metodo per aggiornare lo stato delle visite quando si aggiorna il giorno
	public void updateVisitState(LocalDate time, RepositorySystem repositoryRegistrationSystem) {
		RegistrationList registrationList = new RegistrationList(repositoryRegistrationSystem);

		for(Visit visit : visitList.getVisitList().keySet()) {
			visit = visitList.nextState(visit, registrationList.getTotalRegistrationForVisit(visit), time);
			DataConsistencyService.updateRegistrationState(visit,repositoryRegistrationSystem);
		}
	}
	
	//metodo per aggiornare lo stato delle visite quando si aggiorna il giorno o il numero di partecipanti 
	public Visit updateVisitState(Visit visit, int participants, LocalDate date) {
		return visitList.nextState(visit, participants, date);
	}
}
