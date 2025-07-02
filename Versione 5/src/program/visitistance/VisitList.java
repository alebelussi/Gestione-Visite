package program.visitistance;

import java.time.LocalDate;


import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import program.visitistance.state.VisitStateEnum;
import program.users.volunteer.Volunteer;
import program.visitistance.state.VisitState;
import utilities.RepositorySystem;

public class VisitList {

	private Map<Visit, Integer> visitList;
	private RepositorySystem repositoryVisitSystem;

	public VisitList(RepositorySystem repositoryVisitSystem) {
		this.visitList = new LinkedHashMap<>();
		this.repositoryVisitSystem = repositoryVisitSystem;
		Visit.setNumberOfSub(this.repositoryVisitSystem.loadFirstElement("numberOfSub"));
		loadVisit();
		inizializedGlobalCounter();
	}

	public Map<Visit, Integer> getVisitList() {
		return this.visitList;
	}

	public void loadVisit() {
		List<Visit> list = repositoryVisitSystem.loadData(Visit.class);
		for(Visit visit : list) {
			if(visit.getVisitType() != null)
				visitList.put(visit, visit.getCounterId());
		}
	}

	public void addVisit(Visit visit) {
		this.repositoryVisitSystem.addElement(Visit.class, visit);
		this.visitList.put(visit, visit.getCounterId());
	}

	public void removeVisit(String title) {
		for(Visit visit : visitList.keySet()) {
			if(visit.getVisitType().getTitle().equals(title))
				this.repositoryVisitSystem.removeElement(Visit.class, visit);
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

	public List<Visit> getVisitsByState(Set<VisitStateEnum> states){
		return visitList
				.keySet()
				.stream()
				.filter(v -> states.contains(v.getStateEnum()))
				.toList();
	}

	private void changeState(Visit visit) {
		this.repositoryVisitSystem.modifyElement("counterId", String.valueOf(visit.getCounterId()), visit);
		loadVisit();
	}

	/*--->il metodo si occupa di gestire i cambi di stato delle visite*/
	public Visit nextState(Visit visit, int numberOfSub, LocalDate time) {
		
		VisitState newVisitState = visit.getState().nextState(visit, numberOfSub, time);
		
		visit.setState(newVisitState);
		changeState(visit);
		return visit;
	}

}
