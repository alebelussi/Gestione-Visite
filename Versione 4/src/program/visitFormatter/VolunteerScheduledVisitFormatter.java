package program.visitFormatter;

import program.registration.Registration;
import program.registration.RegistrationList;
import program.visitistance.Visit;

public class VolunteerScheduledVisitFormatter extends ScheduledVisitFormatter {

	private static final String FILE_ISCRIZIONI = "json/iscrizioni.json";
	private RegistrationList registrationList;

	public VolunteerScheduledVisitFormatter() {
		this.registrationList = new RegistrationList(FILE_ISCRIZIONI);
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



