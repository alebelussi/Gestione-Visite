package program.users;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.Locale;

import program.SystemService;
import program.visitistance.ExcludedDates;
import program.visittype.VisitTypeList;
import utilities.InputDati;
import utilities.TimeSimulator;

public class VolunteerManager {
	
	private static final String MSG_ERR_NICK_USER = "Attenzione: è stato inserito un nickname uguale allo username di un configuratore...";
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
	
	private static final String FILE_JSON_VOLONTARIO = "json/volontario.json";
	
	private VolunteerList volunteerList; 
	private ArrayList<Volunteer> lastVolunteersInsert;
	
	public VolunteerManager() {
		this.volunteerList = new VolunteerList(FILE_JSON_VOLONTARIO);
		this.lastVolunteersInsert = new ArrayList<>();
	}
	
	/*@
		requires volunteerList != null; 
		
	 	ensures volunteerList.findVolunteer(nickname);
	 	ensures volunteerList.size() == \old(volunteerList.size()) + 1;
	 @*/
	public void add() {
		String nickname;
		Volunteer volunteer;
		//pulisco la vecchia lista di volontari inseriti
		this.lastVolunteersInsert.clear();
		
		do {
			
			do {
				nickname = InputDati.leggiStringaNonVuota(MSG_INS_NICKNAME);
				final String finalNickname= nickname;
				
				//controllo che il nickname non coincida con nessun username
				if(SystemService.existConfigurator(finalNickname))
					System.out.println(MSG_ERR_NICK_USER);
				
				else {	
					
					if(!volunteerList.findVolunteer(nickname) && !lastVolunteersInsert.stream().anyMatch(el -> finalNickname.equals(el.getNickname()))) {
						System.out.println(MSG_VOLONTARIO_NON_PRESENTE);
						
						if(InputDati.yesNo(MSG_CONFERMA_INSERIMENTO)) {
							//inserisco i dati del nuovo volontario volontario
							String name, surname;
							name = InputDati.leggiStringaNonVuota(MSG_INS_NOME_VOLONTARIO);
							surname = InputDati.leggiStringaNonVuota(MSG_INS_COGNOME_VOLONTARIO);
							volunteer= new Volunteer(name, surname, nickname, "volunteer");
							volunteerList.addVolunteer(volunteer);
							lastVolunteersInsert.add(volunteer);
						}
					}else{
						volunteer = volunteerList.getVolunteer(nickname);
						lastVolunteersInsert.add(volunteer);				
					}
					if(lastVolunteersInsert.size() == 0)
						System.out.println(MSG_ERR_NESSUN_VOLONTARIO_INS);
				}
			}while(lastVolunteersInsert.size() == 0);
					
		}while(InputDati.yesNo(MSG_CONTINUA_INS)); 
	} 
	
	public void view() {
		volunteerList.viewVolunteer();
	}
	
	public void viewAvailability(Volunteer volunteer) {
		System.out.println("Disponibilità inserite: ");
		for(LocalDate date : volunteerList.getDateAvailability(volunteer)) {
			System.out.println(date.toString());
		}
	}
	
	public void searchVolunteer() {
		String nickname;
		Volunteer volunteerSearched;
		nickname = InputDati.leggiStringaNonVuota(MSG_INS_NICKNAME);
		volunteerSearched= volunteerList.getVolunteer(nickname);
		if(volunteerSearched==null)
			System.out.println(MSG_VOLONTARIO_NON_PRESENTE);
		else
			System.out.println(volunteerSearched.toString());
	}
	
	public void addAvailability(Volunteer volunteer, TimeSimulator time, ExcludedDates excludedDate, VisitTypeList visitTypeList) {
		ArrayList<LocalDate> dateAvailable = new ArrayList<>();
		Month modifyMonth;
		int plusMonths, dateNumber; 
		LocalDate date; 
		boolean volunteerAvailable, dateNotOk, isDateOfTypeVisit; 
		
		//l'operazione si svolge dal giorno 16 al 15 e si riferisce al mese i+2 
		if(time.getCurrentDate().getDayOfMonth() >= 16) {	//dal 16 del mese corrrente fino al 30/31 -> mese i+2
			modifyMonth = time.getCurrentDate().getMonth().plus(2);
			plusMonths = 2;
		}
		else {	//dal 1 al 15 del mese successivo -> mese i+1
			modifyMonth = time.getCurrentDate().getMonth().plus(1);
			plusMonths = 1;
		}

		do {
			do {
				dateNumber= InputDati.leggiIntero(String.format(MSG_INS_DATA_DISP, modifyMonth.getDisplayName(java.time.format.TextStyle.FULL, Locale.ITALIAN)), 1, modifyMonth.length(Year.isLeap(time.getCurrentDate().getYear())));
				date = LocalDate.of(time.getCurrentDate().getYear(), modifyMonth.getValue(), dateNumber);
				
				volunteerAvailable = volunteer.getDateAvailability().contains(date);
				dateNotOk = excludedDate.getArrayListExcludedDate().contains(date.toString());
				isDateOfTypeVisit = SystemService.isDateOfVisitType(time, volunteer, visitTypeList, excludedDate, date, plusMonths);
				
				if(volunteerAvailable)
					System.out.println(MSG_ERR_DATA_GIA_ESCLUSA);
				else if(dateNotOk)
					System.out.println(MSG_ERR_DATA_ESCLUSA);
				else if(!isDateOfTypeVisit)
					System.out.println(MSG_ERR_NO_TIPI_VISITA);
				
			}while(volunteerAvailable || dateNotOk || !isDateOfTypeVisit);
			
		}while(!InputDati.yesNo(MSG_CONF_DISPONIBILITA));
		
		dateAvailable.add(date);
		
		this.volunteerList.addAvailability(volunteer, dateAvailable);
		
		System.out.println(MSG_DISPONIBILITA_CONFERMATA);
	}
	
	public void removeAvailability(Volunteer volunteer, TimeSimulator time) {
		Month modifyMonth;
		
		int dateNumber; 
		LocalDate date;
		boolean volunteerAvailable;
		
		//l'operazione si svolge dal giorno 16 al 15 e si riferisce al mese i+2 
		if(time.getCurrentDate().getDayOfMonth() >= 16) 	//dal 16 del mese corrrente fino al 30/31 -> mese i+2
			modifyMonth = time.getCurrentDate().getMonth().plus(2);

		else 	//dal 1 al 15 del mese successivo -> mese i+1
			modifyMonth = time.getCurrentDate().getMonth().plus(1);
		
		do {
			do {
				dateNumber = InputDati.leggiIntero(String.format(MSG_INS_DATA_ESCL, modifyMonth.getDisplayName(java.time.format.TextStyle.FULL, Locale.ITALIAN)), 1, modifyMonth.length(Year.isLeap(time.getCurrentDate().getYear())));
				date = LocalDate.of(time.getCurrentDate().getYear(), modifyMonth.getValue(), dateNumber);
				
				volunteerAvailable = volunteer.getDateAvailability().contains(date);
				
				if(!volunteerAvailable)
					System.out.println(MSG_ERR_DATA_NON_PRESENTE);
				
			}while(!volunteerAvailable);
		}while(!InputDati.yesNo(MSG_CONF_RIMUOVI_DISPONIBILITA));
		
		this.volunteerList.removeAvailability(volunteer, date);
		
	}
	
	public VolunteerList getVolunteerList() {
		return volunteerList;
	}
	
	public ArrayList<Volunteer> getArrayListVolunteerLastInsert(){
		return this.lastVolunteersInsert;
	}
	
	public void addInModify() {
		if(InputDati.yesNo(MSG_CONFERMA_INS_NUOVO_VOLONTARIO)) //se si allora richiamo la classica aggiunta
			add();
	}
}
