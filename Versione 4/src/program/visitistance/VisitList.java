package program.visitistance;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import program.users.Volunteer;
import program.visitFormatter.VisitFormatter;
import utilities.JsonManager;

public class VisitList {

	private static final String MSG_ERR_NESSUNA_VISITA_DISPONIBILE = "Nessuna visita disponibile...";

	private Map<Visit, Integer> visitList;
	private JsonManager jsonManager;

	public VisitList(String jsonPath) {
		this.visitList = new LinkedHashMap<>();
		this.jsonManager = new JsonManager(jsonPath);
		Visit.setNumberOfSub(this.jsonManager.loadFirstElement("numberOfSub"));
		loadVisit();
		inizializedGlobalCounter();
	}

	public Map<Visit, Integer> getVisitList() {
		return this.visitList;
	}

	private void loadVisit() {
		List<Visit> list = jsonManager.loadData(Visit.class);
		for(Visit visit : list) {
			if(visit.getVisitType() != null)
				visitList.put(visit, visit.getCounterId());
		}
	}

	public void addVisit(Visit visit) {
		this.jsonManager.addElement(Visit.class, visit);
		this.visitList.put(visit, visit.getCounterId());
	}

	public void viewVisit(Set<VisitState> states, Map<VisitState, VisitFormatter> visitFormatter) {
		loadVisit();
		List<Visit> filteredVisit = getVisitsByState(states);

		if(filteredVisit.isEmpty()) {
			System.out.println(MSG_ERR_NESSUNA_VISITA_DISPONIBILE);
			return;
		}

		for(Visit visit : filteredVisit) {
			VisitFormatter formatter = visitFormatter.get(visit.getState());
			System.out.println(formatter.format(visit));
		}
	}

	public void removeVisit(String title) {
		for(Visit visit : visitList.keySet()) {
			if(visit.getVisitType().getTitle().equals(title))
				this.jsonManager.removeElement(Visit.class, visit);
		}
	}

	public Visit getVisitByCode(int code) {
		for(Map.Entry<Visit, Integer> elem : visitList.entrySet()) {
			if(elem.getValue() == code)
				return elem.getKey();
		}
		return null;
	}

	public boolean findVisit(Visit visit) {
		for(Visit visitElem : visitList.keySet()) {
			if(visitElem.equals(visit)) {
				return true;
			}

		}
		return false;
	}

	public Visit searchVisit(int codeVisit) {
		return getVisitByCode(codeVisit);
	}

	public boolean isVolunteerScheduled(String nickname, String date) {
		for(Visit visitElem : visitList.keySet()) {
			for(Volunteer volElem : visitElem.getVisitType().getVolunteer()) {
				if(volElem.getNickname().equals(nickname) && visitElem.getDate().equals(date))
					return true;
			}
		}
		return false;
	}

	private void inizializedGlobalCounter() {
		int maxCounter = visitList.values().stream().max(Integer::compare).orElse(0);
		Visit.setGlobalCounter(maxCounter);
	}

	public List<Visit> getVisitsByState(Set<VisitState> states){
		return visitList
				.keySet()
				.stream()
				.filter(v -> states.contains(v.getState()))
				.toList();
	}

	private void changeState(Visit visit) {
		this.jsonManager.modifyElement("counterId", String.valueOf(visit.getCounterId()), visit);
		loadVisit();
	}

	/*--->il metodo si occupa di gestire i cambi di stato delle visite*/
	public Visit nextState(Visit visit, int numberOfSub, LocalDate time) {
		VisitState state = visit.getState();
		int min = visit.getVisitType().getMinParticipant();
		int max = visit.getVisitType().getMaxParticipant();
		String date = visit.getDate();
		boolean closeRegistration = !time.isBefore(LocalDate.parse(date).minusDays(3));

		switch(state) {
			case PROPOSTA:

				if(numberOfSub == max) {
					visit.setVisitState(VisitState.COMPLETA);
					changeState(visit);
				}

				if(closeRegistration) {
					if(numberOfSub < min)
						visit.setVisitState(VisitState.CANCELLATA);
					else
						visit.setVisitState(VisitState.CONFERMATA);
					changeState(visit);
				}

				return visit;
			case COMPLETA:
				if(numberOfSub < max) {
					visit.setVisitState(VisitState.PROPOSTA);
					changeState(visit);
				}
				if(closeRegistration) {
					visit.setVisitState(VisitState.CONFERMATA);
					changeState(visit);
				}
				return visit;
			case CONFERMATA:
				if(LocalDate.parse(date).isBefore(time)) {
					visit.setVisitState(VisitState.EFFETTUATA);
					changeState(visit);
				}
				return visit;
			case CANCELLATA:
				break;
			case EFFETTUATA:
				break;

		}
		return visit;
	}

}
