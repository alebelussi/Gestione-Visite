package program.users.volunteer;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.Locale;

import utilities.InputDati;
import utilities.TimeSimulator;
public class ConsoleVolunteerView implements VolunteerView{
	
	private static final String MSG_DISPONIBILITA_INS = "Disponibilità inserite: ";
	private static final String MSG_RIMOZIONE_ANNULLATA = "Rimozione annullata!";
	private static final String MSG_RIMOZIONE_CONFERMATA = "Rimozione completata con successo!";
	private static final String MSG_CONFERMA_RIMOZIONE = "Vuoi rimuovere il volontario?";
	private static final String MSG_NO_VOLONTARI_DISPONIBILI = "Attenzione: non ci sono volontari disponibili!";
	private static final String MSG_ERR_NICK_CONF = "Attenzione: è stato inserito un nickname uguale allo username di un configuratore...";
	private static final String MSG_ERR_NICK_USER = "Attenzione: è stato inserito un nickname uguale allo username di un fruitore...";
	private static final String MSG_CONF_RIMUOVI_DISPONIBILITA = "Vuoi rimuovere la disponibilità?";
	private static final String MSG_ERR_DATA_NON_PRESENTE = "Attenzione: la data inserita non è presente!";
	private static final String MSG_DISPONIBILITA_CONFERMATA = "Disponibilità salvata con successo!";
	private static final String MSG_CONF_DISPONIBILITA = "Vuoi confermare la disponibilità?";
	private static final String MSG_ERR_NO_TIPI_VISITA = "Attenzione: non hai tipi visita associati per questo giorno!";
	private static final String MSG_ERR_DATA_ESCLUSA = "Attenzione: la data è esclusa da qualsiasi visita!";
	private static final String MSG_CONFERMA_INS_NUOVO_VOLONTARIO = "Nella modifica del tipo di visita vuoi inserire un nuovo volontario?";
	private static final String MSG_CONTINUA_INS = "Vuoi associare un altro volontario?";
	private static final String MSG_ERR_NESSUN_VOLONTARIO_INS = "Attenzione: nessun volontario inserito!";
	private static final String MSG_CONFERMA_INSERIMENTO = "Vuoi inserire il volontario?";
	private static final String MSG_VOLONTARIO_NON_PRESENTE = "Il nickname inserito non corrisponde ad alcun volontario presente!";
	private static final String MSG_INS_NICKNAME = "Inserisci un nickname al volontario: ";
	private static final String MSG_INS_COGNOME_VOLONTARIO = "Inserisci il cognome del volontario: ";
	private static final String MSG_INS_NOME_VOLONTARIO = "Inserisci il nome del volontario: ";
	private static final String MSG_ERR_DATA_GIA_ESCLUSA = "Errore...data già presente";
	private static final String MSG_INS_DATA_ESCL = "Inserisci il giorno che desideri escludere del mese di %s : ";
	private static final String MSG_INS_DATA_DISP = "Inserisci il giorno in cui desideri dare la disponibilta' del mese di %s : ";
	
	//INPUT
	public String askNickname() {
		return InputDati.leggiStringaNonVuota(MSG_INS_NICKNAME);
	}
	
	public boolean confirmInput() {
		return InputDati.yesNo(MSG_CONFERMA_INSERIMENTO);
	}
	
	public String askNameVolunteer() {
		return InputDati.leggiStringaNonVuota(MSG_INS_NOME_VOLONTARIO);
	}
	
	public String askSurnameVolunteer() {
		return InputDati.leggiStringaNonVuota(MSG_INS_COGNOME_VOLONTARIO);
	}
	
	public boolean askContinueAdding() {
		return InputDati.yesNo(MSG_CONTINUA_INS);
	}
	
	public boolean askConfirmAvailability() {
		return InputDati.yesNo(MSG_CONF_DISPONIBILITA);
	}
	
	public boolean askConfirmRemoval() {
		return InputDati.yesNo(MSG_CONFERMA_RIMOZIONE);
	}
	
	public boolean askRemoveAvailability() {
		return InputDati.yesNo(MSG_CONF_RIMUOVI_DISPONIBILITA);
	}
	
	public boolean askConfirmAddVolunteer() {
		return InputDati.yesNo(MSG_CONFERMA_INS_NUOVO_VOLONTARIO);
	}
	
	public int askDayOfMonth(Month modifyMonth, TimeSimulator time) {
		int year = time.getCurrentDate().getYear();
		boolean isLeap = Year.isLeap(year);
		int monthLength = modifyMonth.length(isLeap);
		String monthName = modifyMonth.getDisplayName(java.time.format.TextStyle.FULL, Locale.ITALIAN);
		
		return InputDati.leggiIntero(String.format(MSG_INS_DATA_DISP, monthName), 1, monthLength);
	}
	
	
	//OUTPUT
	public void showVolunteer(Volunteer volunteer) {
		System.out.println("Nome: " + volunteer.getName() + " Cognome: " + volunteer.getSurname() + 
				" Nickname: " + volunteer.getNickname() + " Disponibile: " + volunteer.getDateAvailability());
	}
	
	public void showNameEqualConfiguratorError() {
		System.out.println(MSG_ERR_NICK_CONF);
	}
	
	public void showNameEqualUserError() {
		System.out.println(MSG_ERR_NICK_USER);
	}
	
	public void showVolunteerNotFoundError() {
		System.out.println(MSG_VOLONTARIO_NON_PRESENTE);
	}
	
	public void showNoVolunteerRegistered() {
		System.out.println(MSG_ERR_NESSUN_VOLONTARIO_INS);
	}
	
	public void showDateAlreadyExcludedError() {
		System.out.println(MSG_ERR_DATA_GIA_ESCLUSA);
	}
	
	public void showDateExcludedError() {
		System.out.println(MSG_ERR_DATA_ESCLUSA);
	}
	
	public void showNoVisitTypeError() {
		System.out.println(MSG_ERR_NO_TIPI_VISITA);
	}
	
	public void showConfirmAvailability() {
		System.out.println(MSG_DISPONIBILITA_CONFERMATA);
	}
	
	public void showNoVolunteerAvailabile() {
		System.out.println(MSG_NO_VOLONTARI_DISPONIBILI);
	}
	
	public void showConfimRemoval() {
		System.out.println(MSG_RIMOZIONE_CONFERMATA);
	}
	
	public void showRemovalCancelled() {
		System.out.println(MSG_RIMOZIONE_ANNULLATA);
	}
	
	public void showNotFoundDataError() {
		System.out.println(MSG_ERR_DATA_NON_PRESENTE);
	}
	
	public String showMessageExcludedDate() {
		return MSG_INS_DATA_ESCL;
	}
	
	public String showMessageDateForAvailability() {
		return MSG_INS_DATA_DISP;
	}
	
	public void showAvailabilityDate(LocalDate date) {
		System.out.println(date.toString());
	}
	
	public void showAvailabilityMessage() {
		System.out.println(MSG_DISPONIBILITA_INS);
	}
}

