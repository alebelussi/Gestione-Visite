package program.visitistance.state;

import java.time.LocalDate;

import program.visitistance.Visit;

public class CancelledVisitState implements VisitState {

	@Override
	public VisitState nextState(Visit visit, int numberOfSub, LocalDate time) {
		return this;
	}

	@Override
	public VisitStateEnum getType() {
		return VisitStateEnum.CANCELLATA;
	}

}
