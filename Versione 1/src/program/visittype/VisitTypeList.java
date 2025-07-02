package program.visittype;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utilities.JsonManager;

public class VisitTypeList {
	
	private Map<String, VisitType> visitTypeList;
	private JsonManager jsonManager;
	
	public VisitTypeList(String jsonPath) {
		this.visitTypeList = new HashMap<>();
		this.jsonManager = new JsonManager(jsonPath);
		loadVisitType();
	}
	
	public Map<String, VisitType> getVisitTypeList() {
		return this.visitTypeList;
	}
	
	private void loadVisitType() {
		List<VisitType> list = jsonManager.loadData(VisitType.class);
		for(VisitType visitType : list) {
			visitTypeList.put(visitType.getTitle(), visitType);
		}
	}
	
	public void addVisitType(VisitType visitType) {
		this.visitTypeList.put(visitType.getTitle(), visitType);
		this.jsonManager.addElement(VisitType.class, visitType);
	}
	
	@Override
	public String toString() {
		StringBuffer str= new StringBuffer();
		for (String key : visitTypeList.keySet()) 
		    str.append(visitTypeList.get(key).toString()+"\n \n");
		return str.toString();
		
	}
	
	public void modifyVisitType(VisitType visitType) {
		this.jsonManager.modifyElement("title", visitType.getTitle(), visitType);
	}
	
	public boolean findVisitType(String title) {
		return this.visitTypeList.containsKey(title);
	}
	
	public VisitType getVisitType(String title) {
		return this.visitTypeList.get(title);
	}
	
	public boolean hasSameTime(String startTime, String namePlace, LocalDate startDate, LocalDate endDate, ArrayList<DayOfWeek> day) {
		
		for(VisitType elem : visitTypeList.values()) {
			if(elem.getPlace().getName().equals(namePlace)) {
				LocalDate startDateElem = LocalDate.parse(elem.getStartDate());
				LocalDate endDateElem = LocalDate.parse(elem.getEndDate());
				
				if(!(startDate.isAfter(endDateElem) || endDate.isBefore(startDateElem))) {
					for(DayOfWeek dayElem : elem.getDay()) {
						if(day.contains(dayElem) && elem.getStartHour().equals(startTime)) 
							return true;
					}
				}
			}	
		}
		return false; 
	}
	
	public VisitType searchVisitType(String key) {
		return this.jsonManager.searchElement(VisitType.class,"title", key);
	}
}
