package program.registration;

import java.time.LocalDate;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import program.DataConsistencyService;
import program.users.User;
import program.visitistance.VisitView;
import program.visitistance.Visit;
import program.visitistance.VisitManager;
import program.visitistance.state.VisitStateEnum;
import utilities.RepositorySystem;

public class RegistrationManager {
	
	private RegistrationView registrationView;
	private RegistrationList registrationList;
	private VisitManager visitManager;
	private VisitView visitView;
	private RepositorySystem repositoryRegistration;
	
	public RegistrationManager(VisitManager visitManager, RegistrationView registrationView, VisitView visitView, RepositorySystem repositoryRegistration) {
		this.registrationList = new RegistrationList(repositoryRegistration);
		this.visitManager = visitManager;
		this.registrationView = registrationView;
		this.visitView = visitView;
		this.repositoryRegistration= repositoryRegistration;
	}
	
	public RegistrationManager(RegistrationList registrationList, VisitManager visitManager, RegistrationView registrationView, VisitView visitView) {
		this.registrationList = registrationList;
		this.visitManager = visitManager;
		this.registrationView = registrationView;
		this.visitView = visitView;
	}
	/*@
		requires registrationList != null;
		requires visitManager != null;
		requires user != null;
		requires time != null;
		
		ensures registrationList.size() == \old(registrationList.size()) + 1;
		ensures registrationList.findRegistrationByUserVisit(user, visit);
	@*/
	public void add(User user, LocalDate time) {
		int number, totalRegistration;
		Visit visit;
		
		do {
			
			do {
				visit = visitManager.searchVisit();
			}while(visit == null);
			
			number = registrationView.askNumberOfPeople();
			totalRegistration = registrationList.getTotalRegistrationForVisit(visit);
			
			if(number > Visit.getNumberOfSub()) {
				registrationView.showAbsoluteMaxExceededError();
				return;
			}
			else if(number + totalRegistration > visit.returnMaxParticipant()) {
				registrationView.showMaxParticipantsExceededError();
				return;
			}
			else if(registrationList.findRegistrationByUserVisit(user, visit)) {
				registrationView.showAlreadyRegisteredError();
				return;
			}
		}while (!registrationView.confirmRegistration());
		
		String registrationCode = generateCode();
		registrationList.addRegistration(new Registration(number, registrationCode, visit, user));
		visit = visitManager.updateVisitState(visit, totalRegistration + number, time);//modifico lo stato della visita
		DataConsistencyService.updateRegistrationState(visit, repositoryRegistration);
		registrationView.showRegistrationSuccess(registrationCode);
	}
	/*@
		requires registrationList != null;
		requires visitManager != null;
		requires user != null;
		requires time != null;
	
		ensures registrationList.size() == \old(registrationList.size()) - 1;
		ensures !registrationList.findRegistrationByUserVisit(user, visit);
	@*/
	public void remove(User user, LocalDate time) {
		Registration registration;
		String registrationCode;
		Visit visit;
		do {
			do {
				registrationCode = registrationView.askRegistrationCode();
				registration = registrationList.searchRegistration(registrationCode);
				if(registration == null)
					registrationView.showRegistrationNotFoundError();
			}while(registration == null);
		}while(!registrationView.confirmUnregistration());
		if(!user.equals(registration.getUser())) {
			registrationView.showRegistrationOwnerError();
			return;
		}
		registrationList.removeRegistration(registration);
		
		int totalRegistration = registrationList.getTotalRegistrationForVisit(registration.getVisit());
		visit = visitManager.updateVisitState(registration.getVisit(), totalRegistration, time);
		
		DataConsistencyService.updateRegistrationState(visit, repositoryRegistration);
		registrationView.showCancellationSuccess();
	}
	
	public void view(User user, Set<VisitStateEnum> states) {
		registrationList.loadRegistration();

		List<Registration> registration = registrationList
														.getRegistrationList()
														.stream()
														.filter(r -> r.getUser().equals(user))
														.filter(s -> states.contains(s.getVisit().getStateEnum()))
														.toList();

		if(registration.isEmpty())
			visitView.showNoVisitAvailable();
		else {
			for(Registration regElem : registration) {
				registrationView.showRegistration(regElem);
			}
		}
	}
	
	public Registration searchRegistration() {
		String registrationCode;
		do {
			registrationCode = registrationView.askRegistrationCode();
		}while(!registrationView.confirmSearch());
		Registration registration = registrationList.searchRegistration(registrationCode);
		if(registration != null)
			registrationView.showRegistration(registration);
		else
			registrationView.showRegistrationNotFoundError();
		return registration;
	}
	
	public RegistrationList getRegistrationList() {
		return registrationList;
	}
	
	/*-->il metodo genera il codice di prenotazione delle visite*/
	protected String generateCode() {
		return "SUB-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
	}
	
	
}