package program.visitistance.state;

import java.time.LocalDate;

import program.visitistance.Visit;

public class ProposedVisitState implements VisitState {

	@Override
	public VisitState nextState(Visit visit, int numberOfSub, LocalDate time) {
		
		int min = visit.getVisitType().getMinParticipant();
		int max = visit.getVisitType().getMaxParticipant();
		String date = visit.getDate();
		boolean closeRegistration = !time.isBefore(LocalDate.parse(date).minusDays(3));

		if(numberOfSub == max) 
			return new CompleteVisitState();	//numero di iscritti == massimo partecipanti --> visita completa

		if(closeRegistration) {	//iscrizioni per la visita chiuse
			if(numberOfSub < min)	//se il numero di iscritti è minore dei partecipanti minimi --> visita cancellata
				return new CancelledVisitState();
			else
				return new ConfirmedVisitState();	//altrimenti la visita è confermata
		}
		
		return this;
		
	}

	@Override
	public VisitStateEnum getType() {
		return VisitStateEnum.PROPOSTA;
	}

}
