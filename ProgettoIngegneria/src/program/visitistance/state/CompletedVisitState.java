package program.visitistance.state;

import java.time.LocalDate;

import program.visitistance.Visit;

public class CompletedVisitState implements VisitState {

	@Override
	public VisitState nextState(Visit visit, int numberOfSub, LocalDate time) {
		//STATO FINALE => NO OPERATION 
		return this;
	}

	@Override
	public VisitStateEnum getType() {
		return VisitStateEnum.EFFETTUATA;
	}

}
