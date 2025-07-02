package program;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import program.visittype.*;
import program.places.*;
import program.users.*;
import program.visitistance.*;
import utilities.*;

public class SystemService {
	
	private static final String MSG_ERR_USER_NICK = "Errore: username gia' usato come nickname per un volontario";
	private static final String MSG_TITOLO_WELCOME_VOLUNTEER = "Benvenuto nell'applicazione dei volontari!";
	private static final String MSG_CONFERMA_NUMERO = "Vuoi inserire il numero di persone iscrivibili per singola iscrizione?";
	private static final String MSG_INS_PERSONE_ISCRIVIBILI_SINGOLA_SESSIONE = "Inserisci il numero di persone iscrivibili per singola iscrizione: ";
	private static final String MSG_INS_PERSONE_ISCRIVIBILI = "Numero impostato di persone iscrivibili per singola iscrizione: ";
	private static final String MSG_TITOLO_BENTORNATO = "Bentornato nell'applicazione!";
	private static final String MSG_ERR_PASSWORD_ERRATA = "Errore...password errata";
	private static final String MSG_ERR_USERNAME_NON_PRESENTE = "Errore...username errato";
	private static final String MSG_TITOLO_WELCOME_CONFIG = "Benvenuto nell'applicazione dei configuratori!";
	private static final String MSG_INS_PASSWORD = "Inserisci la password: ";
	private static final String MSG_TITOLO_PRIMO_ACCESSO_ASSOLUTO = "Primo accesso assoluto";
	private static final String MSG_REGISTRAZIONE_CONFERMATA = "Regitrazione completata con successo";
	private static final String MSG_CONFERMA_REGISTRAZIONE = "Vuoi registrarti?";
	private static final String MSG_ERR_USERNAME_GIA_PRESENTE = "Errore...username già utilizzato";
	private static final String MSG_INS_USERNAME = "Inserisci lo username: ";
	private static final String MSG_INS_COGNOME = "Inserisci il cognome dell'utente: ";
	private static final String MSG_INS_NOME = "Inserici il nome dell'utente: ";
	private static final String FILE_CONFIGURATOR = "json/configuratore.json";
	private static final String FILE_VOLONTARIO = "json/volontario.json";
	private static final String FILE_VISITE = "json/visite.json";

	/*--------> metodo per generare le visite --------*/
	public static void generateVisit(TimeSimulator time, VisitTypeList visitTypeList, VisitList visitList, ArrayList<String> excludedDate) {
		
		for(VisitType visitType : visitTypeList.getVisitTypeList().values()) {
			
			LocalDate endDate = LocalDate.parse(visitType.getEndDate());
			LocalDate startDate = LocalDate.parse(visitType.getStartDate());
			
			for(DayOfWeek day : visitType.getDay()) {
				
				List<LocalDate> availableDates = getAvailableDate(time, day, startDate, endDate, excludedDate, 1);	//--> date in cui possono essere generate le visite
				
				for(LocalDate date : availableDates) {
					List<Volunteer> availableVolunteer = getAvailableVolunteer(visitType, date);	//-->volontari disponibili per questa data
					
					for(Volunteer selectedVolunteer : availableVolunteer) {
						ArrayList<Volunteer> volunteer = new ArrayList<>();
						volunteer.add(selectedVolunteer);
						VisitType visType = new VisitType(visitType.getTitle(), visitType.getDescription(), visitType.getMeetingPoint(),
															visitType.getStartDate(), visitType.getEndDate(), visitType.getDay(), visitType.getStartHour(),
															visitType.getDuration(), visitType.isBuyTicket(), visitType.getMinParticipant(), visitType.getMaxParticipant(),
															visitType.getPlace(), volunteer);
						Visit visit = new Visit(visType, date.toString(), VisitState.PROPOSTA);
			
						if(!visitList.isVolunteerScheduled(selectedVolunteer.getNickname(), date.toString()) && !visitList.findVisit(visit)) 
							visitList.addVisit(visit);
						else
							Visit.decreaseGlobalCounter();	
					}
				}
			}
		}
	}
		
	/*--------> metodo per generare le date disponibili per una visita; tutti i vincoli sulla data sono applicati al suo interno --------*/
	private static List<LocalDate> getAvailableDate(TimeSimulator time, DayOfWeek day, LocalDate startDate, LocalDate endDate, ArrayList<String> excludedDate, int plusMonths) {
		Set<LocalDate> dateSet = new HashSet<>(); //--> Memorizza le date ed evita automaticamente i duplicati
	
		LocalDate date = time.getCurrentDate().plusMonths(plusMonths).withDayOfMonth(1);  //--> Primo giorno del mese i+1
		
		LocalDate stopDate = date.withDayOfMonth(date.lengthOfMonth());  //--> Ultimo giorno del mese i+1
		
		date = date.with(TemporalAdjusters.nextOrSame(day)); //--> Avanza fino alla prima data del giorno (param day) specificato
		
		while(!date.isAfter(stopDate)) {	//--> Itera fino a quando date non è dopo stopDate e quindi fino al mese successivo
			if(!date.isBefore(startDate) && !date.isAfter(endDate)) {	//--> Verifica se è compresa nel periodo del tipo visita
				boolean excluededDate = false;	//--> Tiene traccia se la data è esclusa
				for(String excludedElem : excludedDate ) {	//--> Verifica se la data è esclusa 
					if(date.equals(LocalDate.parse(excludedElem))) {
						excluededDate = true;
						break;	//--> Se la data è esclusa termino
					}
				}
				if(!excluededDate) //--> La data non è esclusa, viene inserita
					dateSet.add(date);
			}
			
			date = date.plusWeeks(1); 
		}
		List<LocalDate> dateList = new ArrayList<>(dateSet);	
		Collections.sort(dateList);
		return dateList;
	}

	private static List<Volunteer> getAvailableVolunteer(VisitType visitType, LocalDate date){
		List<Volunteer> availableVolunteer = new ArrayList<>();
		for(Volunteer volElem : visitType.getVolunteer()) {
			if(volElem.getDateAvailability().contains(date))
				availableVolunteer.add(volElem);
		}	
		return availableVolunteer;
	}
	
	public static boolean isDateOfVisitType(TimeSimulator time, Volunteer volunteer, VisitTypeList visitTypeList, ExcludedDates excludedDate, LocalDate date, int plusMonths) {
		List<LocalDate> listDate = new ArrayList<>();
		for(VisitType visitType : visitTypeList.getVisitTypeList().values()) {
			for(Volunteer vol : visitType.getVolunteer()) {
				if(vol.getNickname().equals(volunteer.getNickname())) {
					for(DayOfWeek day : visitType.getDay()) {
						listDate.addAll(getAvailableDate(time, day, LocalDate.parse(visitType.getStartDate()), LocalDate.parse(visitType.getEndDate()), excludedDate.getArrayListExcludedDate(), plusMonths));
					}
				}
			}
		}
		if(!listDate.contains(date))
			return false;
		return true;
	}
	
	//metodo di primo accesso di un configuratore
	private static Configurator firstAccess(JsonManager jsonManager) {
		String name, surname, user, password;
		VolunteerList volunteerList= new VolunteerList(FILE_VOLONTARIO);
		
		do {
			name = InputDati.leggiStringaNonVuota(MSG_INS_NOME);
			surname = InputDati.leggiStringaNonVuota(MSG_INS_COGNOME);
			
			do {
				user = InputDati.leggiStringaNonVuota(MSG_INS_USERNAME);
				
				//controllo che lo username non sia già presente 
				if(jsonManager.searchElement("username", user)) 
					System.out.println(MSG_ERR_USERNAME_GIA_PRESENTE);
				
				//controllo che lo username non sia uguale a nessun nickname di nessun volontario
				
				if(volunteerList.findVolunteer(user))
					System.out.println(MSG_ERR_USER_NICK);
				
			}while(jsonManager.searchElement("username", user) || volunteerList.findVolunteer(user));
			
			password = InputDati.leggiStringaNonVuota(MSG_INS_PASSWORD);
			
		}while(!InputDati.yesNo(MSG_CONFERMA_REGISTRAZIONE));
		
		Configurator utenteRegistrato= new Configurator(name, surname, user, password);
		jsonManager.addElement(Configurator.class, utenteRegistrato);
		
		if(jsonManager.searchElement("firstAbsoluteAccess", "true"))
			firstAbsoluteAccess(jsonManager);
		
		System.out.println(MSG_REGISTRAZIONE_CONFERMATA);
		return utenteRegistrato;
	}
	
	//metodo di primo accesso di un volontario
	private static void firstAccess(JsonManager jsonManager, Volunteer volunteer) {
		String password; 
		
		do {
			password = InputDati.leggiStringaNonVuota(MSG_INS_PASSWORD);
		}while(!InputDati.yesNo(MSG_CONFERMA_REGISTRAZIONE));
		
		volunteer.setPassowrd(password);
		jsonManager.modifyElement("nickname", volunteer.getNickname(), volunteer);
		
		DataConsistencyService.updatePasswordVolunteer(volunteer); //--> modifica della password anche nei tipi visita
	}
	
	private static void firstAbsoluteAccess(JsonManager jsonManager) {
		System.out.println(MSG_TITOLO_PRIMO_ACCESSO_ASSOLUTO);
		
		PlaceManager.setRegion();
		
		setNumberOfSub();
		
		jsonManager.modifyObject("firstAbsoluteAccess", "true", "false");
		
	}
	
	//++++++++++++++++++ Metodo che effettua il LOGIN ++++++++++++++++++++++++++
	public static Person login(String pathConfigurator, String pathVolunteer) {
		String user, password; 
		JsonManager jsConfigurator = new JsonManager(pathConfigurator);
		JsonManager jsVolunteer = new JsonManager(pathVolunteer);
		boolean userFound, pwFound, nicknameFound, passwordFound;
		
		do {
			user = InputDati.leggiStringaNonVuota(MSG_INS_USERNAME);
			password = InputDati.leggiStringaNonVuota(MSG_INS_PASSWORD);
			
			userFound = jsConfigurator.searchElement("username", user);
			pwFound = jsConfigurator.searchElement("password", password);
			nicknameFound = jsVolunteer.searchElement("nickname", user);
			passwordFound = jsVolunteer.searchElement("password", password);
			
			if(jsConfigurator.searchElement("user", user) && jsConfigurator.searchElement("pw", password)) { //accesso con le credenziali standard = primo accesso
				System.out.println(MSG_TITOLO_WELCOME_CONFIG);
				return firstAccess(jsConfigurator); 
			}
				
			else if(!userFound && !nicknameFound)
				System.out.println(MSG_ERR_USERNAME_NON_PRESENTE);
			else if(!pwFound && !passwordFound)
				System.out.println(MSG_ERR_PASSWORD_ERRATA);
			else if(nicknameFound) {
				Volunteer volunteer = jsVolunteer.searchElement(Volunteer.class, "nickname", user);
				if(volunteer.getPassword().equals("volunteer")) {
					System.out.println(MSG_TITOLO_WELCOME_VOLUNTEER);
					
					//faccio il primo accesso per il volontario
					firstAccess(jsVolunteer, volunteer);
					
					return volunteer;
				}
					
			}
			else { //ACCESSO CORRETTO E UTENTE GIà INSERITO
				System.out.println(MSG_TITOLO_BENTORNATO);
			}
		}while((!userFound || !pwFound) && (!nicknameFound || !passwordFound));
		
		if(userFound && pwFound)
			return jsConfigurator.searchElement(Configurator.class,"username", user);
		else if(nicknameFound && passwordFound)
			return jsVolunteer.searchElement(Volunteer.class, "nickname", user);
		else 
			return null;
	}
	
	public static void setNumberOfSub() {
		int numberOfSub; 
		JsonManager jsNumberOfSub = new JsonManager(FILE_VISITE);
		Visit.setNumberOfSub(jsNumberOfSub.loadFirstElement("numberOfSub"));
		
		do {	
			if(Visit.getNumberOfSub() != 0)
				System.out.println(MSG_INS_PERSONE_ISCRIVIBILI + Visit.getNumberOfSub());
			numberOfSub = InputDati.leggiInteroConMinimo(MSG_INS_PERSONE_ISCRIVIBILI_SINGOLA_SESSIONE, 0);
		}while(!InputDati.yesNo(MSG_CONFERMA_NUMERO));
		

		jsNumberOfSub.modifyObject("numberOfSub", String.valueOf(Visit.getNumberOfSub()), numberOfSub);
		Visit.setNumberOfSub(numberOfSub);
	}
	
	public static void checkDay(TimeSimulator time, VisitTypeList visitTypeList, OpenCloseAvailabilityManager availabilityManager, ArrayList<String> excludedDate) {
		VisitList visitList = new VisitList(FILE_VISITE);
		if(time.getNumberOfDay() == 16) {
			availabilityManager.closeAvaialbility();
			SystemService.generateVisit(time, visitTypeList, visitList, excludedDate);
		}
	}
	
	//metodo che verifica se esiste un configuratore con lo username passato come parametro
	public static boolean existConfigurator(String username) {
		JsonManager jsConf= new JsonManager(FILE_CONFIGURATOR);
			
		//ricerco se esiste l'elemento e ritorno l'esito
		return (jsConf.searchElement("username", username));
				
	}		

}
