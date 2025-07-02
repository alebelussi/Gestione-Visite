package program.visitistance;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import program.users.Volunteer;
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
	
	public void viewVisit() {  
		if(visitList.size() == 0) 
			System.out.println(MSG_ERR_NESSUNA_VISITA_DISPONIBILE);
		else {
			for(Visit visit : visitList.keySet()) {
				System.out.println(visit.toString());
			}
		}	
	}
	
	public void removeVisit(String title) {
		for(Visit visit : visitList.keySet()) {
			if(visit.getVisitType().getTitle().equals(title))
				this.jsonManager.removeElement(Visit.class, visit);
		}
	}
	
	public boolean findVisit(Visit visit) {  
		for(Visit visitElem : visitList.keySet()) {
			if(visitElem.equals(visit)) {
				return true;	
			}
				
		}
		return false;
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
		
}
