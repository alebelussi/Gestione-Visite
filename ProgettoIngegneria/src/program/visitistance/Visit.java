package program.visitistance;

import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import program.visitistance.state.CancelledVisitState;
import program.visitistance.state.CompleteVisitState;
import program.visitistance.state.CompletedVisitState;
import program.visitistance.state.ConfirmedVisitState;
import program.visitistance.state.ProposedVisitState;
import program.visitistance.state.VisitState;
import program.visitistance.state.VisitStateEnum;
import program.visittype.VisitType;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Visit {

	private VisitType visitType;
	@JsonProperty("state")
	private VisitStateEnum stateEnum;
	@JsonIgnore
	private VisitState state;
	private String date;
	private static int numberOfSub;
	private static int globalCounter = 0;
	private int counterId;
	
	private static final Map<VisitStateEnum, VisitState> stateMap = Map.of(
		VisitStateEnum.PROPOSTA, new ProposedVisitState(),
		VisitStateEnum.COMPLETA, new CompleteVisitState(),
		VisitStateEnum.CONFERMATA, new ConfirmedVisitState(),
		VisitStateEnum.CANCELLATA, new CancelledVisitState(),
		VisitStateEnum.EFFETTUATA, new CompletedVisitState()
	);

	public Visit() {}

	public Visit(VisitType visitType, String date, VisitStateEnum state) {
		this.visitType = visitType;
		this.date = date;
		this.stateEnum = state;
		this.state = stateMap.get(state);
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
	
	public void setState(VisitState state) {
		this.state = state; 
		this.stateEnum = state.getType();
	}
	
	public VisitState getState() {
		return this.state;
	}

	public void setStateEnum(VisitStateEnum stateEnum) {
		this.stateEnum = stateEnum;
		this.state = stateMap.get(stateEnum);
    }

	public VisitStateEnum getStateEnum() {
		return this.stateEnum;
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

	public int returnMaxParticipant() {
		return this.visitType.getMaxParticipant();
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
