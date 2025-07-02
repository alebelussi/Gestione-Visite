package program.registration;

import java.util.HashSet;

import java.util.List;
import java.util.Set;
import program.users.User;
import program.visitistance.Visit;
import utilities.RepositorySystem;

public class RegistrationList {

	private Set<Registration> registrationList;
	private RepositorySystem repositorySystemUsed;

	public RegistrationList(RepositorySystem repositorySystemUsed) {
		this.registrationList = new HashSet<>();
		this.repositorySystemUsed = repositorySystemUsed;
		loadRegistration();
	}

	public void loadRegistration() {
		List<Registration> list = repositorySystemUsed.loadData(Registration.class);
		for(Registration registration : list) {
			registrationList.add(registration);
		}
	}

	public Set<Registration> getRegistrationList(){
		return this.registrationList;
	}

	public void addRegistration(Registration registration) {
		this.registrationList.add(registration);
		this.repositorySystemUsed.addElement(Registration.class, registration);
	}

	public void removeRegistration(Registration registration) {
		this.registrationList.remove(registration);
		this.repositorySystemUsed.removeElement(Registration.class, registration);
	}

	public Registration searchRegistration(String registrationCode) {
		return this.repositorySystemUsed.searchElement(Registration.class, "code", registrationCode);
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
