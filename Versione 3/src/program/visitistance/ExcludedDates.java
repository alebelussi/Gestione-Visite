package program.visitistance;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;

import utilities.InputDati;
import utilities.JsonManager;
import utilities.TimeSimulator;

public class ExcludedDates {
	
	private static final String MSG_NESSUNA_DATA_ESCLUSA_INSERITA = "Nessuna data esclusa inserita...";
	private static final String MSG_CONFERMA_RIMOZIONE = "Data rimossa correttamente! \n";
	private static final String MSG_RIMOZIONE_DATA = "Vuoi rimuovere la data?";
	private static final String MSG_ERR_DATA_NON_PRESENTE = "Errore...data non presente";
	private static final String MSG_HEAD_VIEW = "Date escluse: ";
	private static final String MSG_CONFERMA_ESCLUSIONE = "Data esclusa correttamente! \n";
	private static final String MSG_ESCLUSIONE_DATA = "Vuoi escludere la data?";
	private static final String MSG_ERR_DATA_GIA_ESCLUSA = "Errore...data già presente";
	private static final String MSG_INS_DATA_ESCL = "Inserisci il giorno che desideri escludere del mese di %s : ";
	
	private TreeSet<LocalDate> dateExcluded;
	private JsonManager jsDateEscluse;
	
	public ExcludedDates(String path) {
		this.jsDateEscluse = new JsonManager(path);
		this.dateExcluded = new TreeSet<>();
		loadDates();
	}
	
	private void loadDates() {
		List<LocalDate> list = jsDateEscluse.loadData(LocalDate.class);
		for(LocalDate date : list) {
			dateExcluded.add(date);
		}
	}
	
	public ArrayList<String> getArrayListExcludedDate(){
		ArrayList<String> dateExcludedStr = new ArrayList<>();
	    for (LocalDate date : dateExcluded) {
	        dateExcludedStr.add(date.toString()); // Converte LocalDate in String formato "YYYY-MM-DD"
	    }
	    return dateExcludedStr;
	}
	
	public void excludeDate(TimeSimulator time) {
		Month modifyMonth= time.getCurrentDate().getMonth().plus(3);
		int dateNumber;
		//scrivo la data esclusa nel file JSON
		LocalDate dataEsclusa; 
		//lettura data da escludere
		do {
			do{
				dateNumber= InputDati.leggiIntero(String.format(MSG_INS_DATA_ESCL, modifyMonth.getDisplayName(java.time.format.TextStyle.FULL, Locale.ITALIAN)), 1, modifyMonth.length(Year.isLeap(time.getCurrentDate().getYear())));
				dataEsclusa = LocalDate.of(time.getCurrentDate().getYear(), modifyMonth.getValue(), dateNumber);
				
				if(dateExcluded.contains(dataEsclusa))
					System.out.println(MSG_ERR_DATA_GIA_ESCLUSA);
				
			}while(dateExcluded.contains(dataEsclusa));
		
		}while(!InputDati.yesNo(MSG_ESCLUSIONE_DATA));
		
		
		jsDateEscluse.addElement(String.class, dataEsclusa.toString());
		dateExcluded.add(dataEsclusa);
		
		System.out.println(MSG_CONFERMA_ESCLUSIONE);
	}
	
	public void viewExcludedDate(TimeSimulator time) {
		Month modifyMonth = time.getCurrentDate().getMonth().plus(3);
		System.out.println(MSG_HEAD_VIEW);
		for(LocalDate date : dateExcluded) {
			if(date.getMonth() == modifyMonth)
				System.out.println(date.toString());
		}
		
		if(dateExcluded.isEmpty())
			System.out.println(MSG_NESSUNA_DATA_ESCLUSA_INSERITA);
	}
	
	public void removeExcludedDate(TimeSimulator time) {
		Month modifyMonth = time.getCurrentDate().getMonth().plus(3);
		int dateNumber; 
		LocalDate dataEsclusa;
		
		do {
			do {
				dateNumber = InputDati.leggiIntero(String.format(MSG_INS_DATA_ESCL, modifyMonth.getDisplayName(java.time.format.TextStyle.FULL, Locale.ITALIAN)), 1, modifyMonth.length(Year.isLeap(time.getCurrentDate().getYear())));
				dataEsclusa = LocalDate.of(time.getCurrentDate().getYear(), modifyMonth.getValue(), dateNumber);
				
				if(!dateExcluded.contains(dataEsclusa))
					System.out.println(MSG_ERR_DATA_NON_PRESENTE);
			}while(!dateExcluded.contains(dataEsclusa));
		}while(!InputDati.yesNo(MSG_RIMOZIONE_DATA));
		
		jsDateEscluse.removeElement(String.class, dataEsclusa.toString());
		dateExcluded.remove(dataEsclusa);
		
		System.out.println(MSG_CONFERMA_RIMOZIONE);
	}
	
}