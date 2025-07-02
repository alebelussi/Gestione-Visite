package program.visitFormatter;

import program.visitistance.Visit;

public class DeletedVisitFormatter implements VisitFormatter {

	@Override
	public String format(Visit visit) {

		return String.format("\n Titolo: %s \n Data di svolgimento: %s",
				visit.getVisitType().getTitle(),
				visit.getDate()
				);
	}

}