package program.visitistance;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.Locale;

import utilities.InputDati;
import utilities.TimeSimulator;

public class ConsoleExcludedDatesView implements ExcludedDatesView{
	
	private static final String MSG_OPERAZIONE_ANNULLATA = "Operazione annullata!";
	private static final String MSG_NESSUNA_DATA_ESCLUSA_INSERITA = "Nessuna data esclusa inserita...";
	private static final String MSG_CONFERMA_RIMOZIONE = "Data rimossa correttamente! \n";
	private static final String MSG_RIMOZIONE_DATA = "Vuoi rimuovere la data?";
	private static final String MSG_ERR_DATA_NON_PRESENTE = "Errore...data non presente";
	private static final String MSG_HEAD_VIEW = "Date Escluse";
	private static final String MSG_CONFERMA_ESCLUSIONE = "Data esclusa correttamente! \n";
	private static final String MSG_ESCLUSIONE_DATA = "Vuoi escludere la data?";
	private static final String MSG_ERR_DATA_GIA_ESCLUSA = "Errore...data già presente";
	private static final String MSG_INS_DATA_ESCL = "Inserisci il giorno che desideri escludere del mese di %s : ";

	//INPUT
	
	public int askExcludedDate(Month modifyMonth, TimeSimulator time) {
		int year = time.getCurrentDate().getYear();
		boolean isLeap = Year.isLeap(year);
		int monthLength = modifyMonth.length(isLeap);
		String monthName = modifyMonth.getDisplayName(java.time.format.TextStyle.FULL, Locale.ITALIAN);
		
		return InputDati.leggiIntero(String.format(MSG_INS_DATA_ESCL, monthName), 1, monthLength);
	}
	
	public boolean askConfirmationExcludeData() {
		return InputDati.yesNo(MSG_ESCLUSIONE_DATA);
	}
	
	public boolean askConfirmationRemoveData() {
		return InputDati.yesNo(MSG_RIMOZIONE_DATA);
	}
	
	//OUTPUT
	
	public void showDateAlreadyExist() {
		System.out.println(MSG_ERR_DATA_GIA_ESCLUSA);
	}
	
	public void showConfirmationExcludeData() {
		 System.out.println(MSG_CONFERMA_ESCLUSIONE);
	}
	
	public void showMessageHeadView() {
		System.out.println(MSG_HEAD_VIEW);
	}
	
	public void showDate(LocalDate date) {
		System.out.println(date.toString());
	}
	
	public void showDateNotFound() {
		System.out.println(MSG_ERR_DATA_NON_PRESENTE);
	}
	
	public void showNoDateEntered() {
		System.out.println(MSG_NESSUNA_DATA_ESCLUSA_INSERITA);
	}
	
	public void showNoOperation() {
		System.out.println(MSG_OPERAZIONE_ANNULLATA);
	}
	
	public void showConfirmRemoveDate() {
		System.out.println(MSG_CONFERMA_RIMOZIONE);
	}
	
}