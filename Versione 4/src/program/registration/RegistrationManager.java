package program.registration;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import program.DataConsistencyService;
import program.users.User;
import program.visitistance.Visit;
import program.visitistance.VisitManager;
import program.visitistance.VisitState;
import utilities.InputDati;

public class RegistrationManager {

	private static final String MSG_DISDIZIONE_CONFERMATA = "Iscrizione annullata con successo!";
	private static final String MSG_ERR_REGISTRAZIONE_NON_EFFETTUATA = "Errore...non hai effettuato questa registrazione!";
	private static final String MSG_CONFERMA_DISDIZIONE = "Vuoi disdirre l'iscrizione?";
	private static final String MSG_CONFERMA_RICERCA = "Vuoi cercare questa iscrizione?";
	private static final String MSG_ERR_REGISTRAZIONE_NON_TROVATA = "Errore...registrazione non trovata!";
	private static final String MSG_INS_COD = "Inserisci il codice di prenotazione: ";
	private static final String MSG_CODICE_PRENOTAZIONE = "Codice prenotazione: %s";
	private static final String MSG_ISCRIZIONE_CONFERMATA = "Iscrizione avvenuta con successo!";
	private static final String MSG_CONFERMA_ISCRIZIONE = "Vuoi confermare l'iscrizione?";
	private static final String MSG_ERR_ISCRIZIONE_GIA_EFFETTUATA = "Errore...hai già effettuato un'iscrizione per questa visita";
	private static final String MSG_ERR_N_MAX_PARTECIPANTI = "Errore...hai inserito un numero di persone che supera il limite massimo di partecipanti!";
	private static final String MSG_ERR_N_MAX = "Errore...hai inserito un numero superiore rispetto a quello consentito!";
	private static final String MSG_N_PERSONE_ISCRIZIONE = "Inserisci il numero di persone che vuoi iscrivere: ";
	private static final String FILE_ISCRIZIONI = "json/iscrizioni.json";

	private RegistrationList registrationList;
	private VisitManager visitManager;

	public RegistrationManager(VisitManager visitManager) {
		this.registrationList = new RegistrationList(FILE_ISCRIZIONI);
		this.visitManager = visitManager;
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

			number = InputDati.leggiInteroConMinimo(MSG_N_PERSONE_ISCRIZIONE, 0);
			totalRegistration = registrationList.getTotalRegistrationForVisit(visit);

			if(number > Visit.getNumberOfSub()) {
				System.out.println(MSG_ERR_N_MAX);
				return;
			}

			else if(number + totalRegistration > visit.returnMaxParticipant()) {
				System.out.println(MSG_ERR_N_MAX_PARTECIPANTI);
				return;
			}

			else if(registrationList.findRegistrationByUserVisit(user, visit)) {
				System.out.println(MSG_ERR_ISCRIZIONE_GIA_EFFETTUATA);
				return;
			}

		}while(!InputDati.yesNo(MSG_CONFERMA_ISCRIZIONE));

		String registrationCode = generateCode();
		registrationList.addRegistration(new Registration(number, registrationCode, visit, user));
		visit = visitManager.getVisitList().nextState(visit, totalRegistration + number, time); //modifico lo stato della visita
		DataConsistencyService.updateRegistrationState(visit);

		System.out.println(MSG_ISCRIZIONE_CONFERMATA);
		System.out.println(String.format(MSG_CODICE_PRENOTAZIONE, registrationCode));

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
				registrationCode = InputDati.leggiStringaNonVuota(MSG_INS_COD);
				registration = registrationList.searchRegistration(registrationCode);

				if(registration == null)
					System.out.println(MSG_ERR_REGISTRAZIONE_NON_TROVATA);

			}while(registration == null);

		}while(!InputDati.yesNo(MSG_CONFERMA_DISDIZIONE));

		if(!user.equals(registration.getUser())) {
			System.out.println(MSG_ERR_REGISTRAZIONE_NON_EFFETTUATA);
			return;
		}

		registrationList.removeRegistration(registration);
		visit = visitManager.getVisitList().nextState(registration.getVisit(), registrationList.getTotalRegistrationForVisit(registration.getVisit()), time);
		DataConsistencyService.updateRegistrationState(visit);

		System.out.println(MSG_DISDIZIONE_CONFERMATA);
	}

	public void view(User user, Set<VisitState> states) {
		this.registrationList.viewRegistration(user, visitManager.getVisitFormatter(), states);
	}

	public Registration searchRegistration() {
		String registrationCode;

		do {
			registrationCode = InputDati.leggiStringaNonVuota(MSG_INS_COD);
		}while(!InputDati.yesNo(MSG_CONFERMA_RICERCA));

		Registration registration = registrationList.searchRegistration(registrationCode);

		if(registration != null)
			System.out.println(registration.toString());
		else
			System.out.println(MSG_ERR_REGISTRAZIONE_NON_TROVATA);

		return registration;
	}

	/*-->il metodo genera il codice di prenotazione delle visite*/
	private String generateCode() {
		return "SUB-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
	}
}
