package program.visitFormatter;

import program.registration.Registration;
import program.registration.RegistrationList;
import program.visitistance.Visit;
import utilities.RepositorySystem;

public class VolunteerScheduledVisitFormatter extends ScheduledVisitFormatter {

	private RegistrationList registrationList;

	public VolunteerScheduledVisitFormatter(RepositorySystem repositoryRegistrationSystem) {
		this.registrationList = new RegistrationList(repositoryRegistrationSystem);
	}

	@Override
	public String format(Visit visit) {

		String baseInfo = "", extraInfo = "";

		for(Registration registration : registrationList.getRegistrationList()) {
			if(registration.getVisit().equals(visit)) {
				baseInfo = super.format(visit);
				extraInfo += String.format("\n Codice di prenotazione: %s \n Numero di persone: %d",
						registration.getCode(),
						registration.getTotalRegistrations()
						);
			}
		}

		return baseInfo + extraInfo;

	}
}



