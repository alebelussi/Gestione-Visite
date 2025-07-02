package program.registration;
import utilities.InputDati;
public class ConsoleRegistrationView implements RegistrationView{
	
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
	
	// METODI PER L'INPUT
	
	public int askNumberOfPeople() {
		return InputDati.leggiInteroConMinimo(MSG_N_PERSONE_ISCRIZIONE, 0);
	}
	
	public String askRegistrationCode() {
		return InputDati.leggiStringaNonVuota(MSG_INS_COD);
	}
	public boolean confirmRegistration() {
		return InputDati.yesNo(MSG_CONFERMA_ISCRIZIONE);
	}
	
	public boolean confirmUnregistration() {
		return InputDati.yesNo(MSG_CONFERMA_DISDIZIONE);
	}
	
	public boolean confirmSearch() {
		return InputDati.yesNo(MSG_CONFERMA_RICERCA);
	}
	
	//METODI PER L'OUTPUT
	
	public void showRegistration(Registration registration) {
		System.out.println("\n Codice Prenotazione: " + registration.getCode() + "\n Nome Visita: " + registration.getVisit().getVisitType().getTitle()
					+ "\n Numero di persone: " + registration.getTotalRegistrations() + "\n Fruitore: " + registration.getUser().getUsername());
	}
	
	public void showMaxParticipantsExceededError() {
		System.out.println(MSG_ERR_N_MAX_PARTECIPANTI);
	}
	
	public void showAbsoluteMaxExceededError() {
		System.out.println(MSG_ERR_N_MAX);
	}
	
	public void showAlreadyRegisteredError() {
		System.out.println(MSG_ERR_ISCRIZIONE_GIA_EFFETTUATA);
	}
	
	public void showRegistrationSuccess(String code) {
		System.out.println(MSG_ISCRIZIONE_CONFERMATA);
		System.out.println(String.format(MSG_CODICE_PRENOTAZIONE, code));
	}
	
	public void showRegistrationNotFoundError() {
		System.out.println(MSG_ERR_REGISTRAZIONE_NON_TROVATA);
	}
	
	public void showRegistrationOwnerError() {
		System.out.println(MSG_ERR_REGISTRAZIONE_NON_EFFETTUATA);
	}
	
	public void showCancellationSuccess() {
		System.out.println(MSG_DISDIZIONE_CONFERMATA);
	}
	
	public void showMessage(String message) {
		System.out.println(message);
	}
	
	public void showCode(String code) {
		System.out.print(String.format(MSG_CODICE_PRENOTAZIONE, code));
	}
	
}

