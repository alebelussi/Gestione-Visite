package program.visitistance.state;

import java.time.LocalDate;

import program.visitistance.Visit;

public class CompleteVisitState implements VisitState {

	@Override
	public VisitState nextState(Visit visit, int numberOfSub, LocalDate time) {

		int max = visit.getVisitType().getMaxParticipant();
		String date = visit.getDate();
		boolean closeRegistration = !time.isBefore(LocalDate.parse(date).minusDays(3));

		if(numberOfSub < max) 	//se il numero di iscritti è minore dei partecipanti massimi la visita passa da completa a proposta
			return new ProposedVisitState();
		if(closeRegistration) 	//se le iscrizioni sono chiuse, da visita completa passa a confermata
			return new ConfirmedVisitState();
		
		return this;
	}

	@Override
	public VisitStateEnum getType() {
		return VisitStateEnum.COMPLETA;
	}

}
