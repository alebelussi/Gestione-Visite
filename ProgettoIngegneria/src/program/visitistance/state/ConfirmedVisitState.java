package program.visitistance.state;

import java.time.LocalDate;

import program.visitistance.Visit;

public class ConfirmedVisitState implements VisitState {

	@Override
	public VisitState nextState(Visit visit, int numberOfSub, LocalDate time) {
		String date = visit.getDate();
		if(LocalDate.parse(date).isBefore(time)) 	//se la data è precedente a quella corrente la visita è stata effettuata
			return new CompletedVisitState();
		
		return this;
	}

	@Override
	public VisitStateEnum getType() {
		return VisitStateEnum.CONFERMATA;
	}

}
