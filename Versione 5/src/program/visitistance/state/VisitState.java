package program.visitistance.state;

import java.time.LocalDate;

import program.visitistance.Visit;

public interface VisitState {
	
	VisitState nextState(Visit visit, int numberOfSub, LocalDate time);	  //aggiorna lo stato della visita
	
	VisitStateEnum getType();	//restituisce l'etichetta dello stato della visita
}
