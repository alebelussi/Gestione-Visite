package program.registration;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import program.users.User;
import program.visitFormatter.VisitFormatter;
import program.visitistance.Visit;
import program.visitistance.VisitState;
import utilities.JsonManager;

public class RegistrationList {

	private static final String MSG_ERR_NESSUNA_VISITA_DISPONIBILE = "Nessuna visita disponibile...";
	private Set<Registration> registrationList;
	private JsonManager jsonManager;

	public RegistrationList(String jsonPath) {
		this.registrationList = new HashSet<>();
		this.jsonManager = new JsonManager(jsonPath);
		loadRegistration();
	}

	private void loadRegistration() {
		List<Registration> list = jsonManager.loadData(Registration.class);
		for(Registration registration : list) {
			registrationList.add(registration);
		}
	}

	public Set<Registration> getRegistrationList(){
		return this.registrationList;
	}

	public void addRegistration(Registration registration) {
		this.registrationList.add(registration);
		this.jsonManager.addElement(Registration.class, registration);
	}

	public void removeRegistration(Registration registration) {
		this.registrationList.remove(registration);
		this.jsonManager.removeElement(Registration.class, registration);
	}

	/*-->il metodo visualizza le visite associate alle iscrizioni del fruitore*/
	public void viewRegistration(User user, Map<VisitState, VisitFormatter> visitFormatter, Set<VisitState> states) {
		loadRegistration();

		List<Registration> registration = registrationList
														.stream()
														.filter(r -> r.getUser().equals(user))
														.filter(s -> states.contains(s.getVisit().getState()))
														.toList();

		if(registration.isEmpty())
			System.out.println(MSG_ERR_NESSUNA_VISITA_DISPONIBILE);
		else {
			for(Registration regElem : registration) {
				VisitFormatter formatter = visitFormatter.get(regElem.getVisit().getState());
				System.out.println(formatter.format(regElem.getVisit()));
			}
		}
	}

	public Registration searchRegistration(String registrationCode) {
		return this.jsonManager.searchElement(Registration.class, "code", registrationCode);
	}

	/*-->il metodo ricerca l'iscrizione tramite il fruitore e la visita*/
	public boolean findRegistrationByUserVisit(User user, Visit visit) {
		for(Registration registration : registrationList) {
			if(registration.getUser().equals(user) && registration.getVisit().equals(visit))
				return true;
		}
		return false;
	}

	/*-->il metodo calcola il numero di iscritti per la visita passata come paramtetro*/
	public int getTotalRegistrationForVisit(Visit visit) {
		int somma = 0;

		for(Registration registration : registrationList) {
			if(registration.getVisit().equals(visit))
				somma += registration.getTotalRegistrations();
		}

		return somma;
	}
}
