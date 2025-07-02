package program.visitFormatter;

import program.visitistance.Visit;

public class ScheduledVisitFormatter implements VisitFormatter {

	@Override
	public String format(Visit visit) {

		return String.format("\n Titolo: %s \n Descrizione: %s \n Punto di incontro: %s \n Data di svolgimento: %s \n"
				+ " Ora di inizio: %s \n Biglietti: %s",
				visit.getVisitType().getTitle(),
				visit.getVisitType().getDescription(),
				visit.getVisitType().getMeetingPoint(),
				visit.getDate(),
				visit.getVisitType().getStartHour(),
				(visit.getVisitType().isBuyTicket() ? "si" : "no")
				);
	}
}
