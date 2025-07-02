package program.visitistance;


import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import program.visittype.VisitType;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Visit {

	private VisitType visitType;	
	private VisitState state; 
	private String date; 
	private static int numberOfSub;
	private static int globalCounter = 0;
	private int counterId; 
	
	public Visit() {};	
	
	public Visit(VisitType visitType, String date, VisitState state) {	
		this.visitType = visitType;
		this.date = date;
		this.state = state;
		this.counterId = ++globalCounter;
	}
	
	public void setCounterId(int counterId) {	
		this.counterId = counterId;
	}
	
	public int getCounterId() {	
		return this.counterId;
	}
	
	public void setVisitType(VisitType visitType) { 
		this.visitType = visitType;
	}
	
	public VisitType getVisitType() { 
		return this.visitType;
	}
	
	public void setVisitState(VisitState state) { 
		this.state = state;
	}
	
	public VisitState getState() { 
		return this.state;
	}
	
	public void setDate(String date) { 
		this.date = date;
	}
	
	public String getDate() { 
		return this.date;
	}
	
	
	public static void setNumberOfSub(int numberOfSub) {  
		Visit.numberOfSub = numberOfSub;
	}
	
	public static int getNumberOfSub() {
		return Visit.numberOfSub;
	}
	
	public static void setGlobalCounter(int globalCounter) {
		Visit.globalCounter = globalCounter;
	}
	
	public static void decreaseGlobalCounter() {
		Visit.globalCounter -= 1; 
	}
	
	@Override
	public String toString() {  
		return "Visita ID: " + getCounterId() + "\t Data: " + getDate() + "\t Nome: " + getVisitType().getTitle() + "\t Volontario: " + getVisitType().getVolunteer();
	}
	
	@Override
	public boolean equals(Object o) { 
		if(this == o)
			return true;
		if(o == null || o.getClass() != getClass())
			return false;
			
		Visit visit = (Visit) o;

		return Objects.equals(visitType.getTitle(), visit.getVisitType().getTitle()) && Objects.equals(date, visit.getDate());
	}
	
	@Override
	public int hashCode() {	
		return Objects.hash(visitType.getTitle(), date);
	}

}
