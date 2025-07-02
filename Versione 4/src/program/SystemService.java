package program;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import program.places.PlaceManager;
import program.registration.RegistrationList;
import program.users.Configurator;
import program.users.OpenCloseAvailabilityManager;
import program.users.Person;
import program.users.User;
import program.users.Volunteer;
import program.visitistance.ExcludedDates;
import program.visitistance.Visit;
import program.visitistance.VisitList;
import program.visitistance.VisitState;
import program.visittype.VisitType;
import program.visittype.VisitTypeList;
import utilities.InputDati;
import utilities.JsonManager;
import utilities.MenuLayout;
import utilities.TimeSimulator;

public class SystemService {

	private static final String FILE_FRUITORE = "json/fruitore.json";
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
	private static final String FILE_ISCRIZIONI = "json/iscrizioni.json";

	private static final String MSG_WELCOME="\n Inserisci una delle seguenti opzioni: ";
	private static final String MSG_EXIT="\n"+"+++++ ARRIVEDERCI %s %s +++++ \n";
	private static final ArrayList<String> VOCI_MENU_GENERALE = new ArrayList<>(Arrays.asList(
		    "Configuratore",
		    "Volontario",
		    "Fruitore",
		    "Esci"
		));
	private static final ArrayList<String> VOCI_MENU_UTENTE = new ArrayList<>(Arrays.asList(
		    "Registrazione",
		    "Accesso",
		    "Esci"
		));


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

	/*--------> metodo per individuare le disposinibilità del volontario */
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

	/*--------> metodo per caricare le credenziali del configuratore, volontario e fruitore */
	private static String[] getCredentials(JsonManager jsConfigurator, JsonManager jsVolunteer, JsonManager jsUser) {
		String name, surname, user, password;
		boolean confFound, volFound, userFound;

		do {
			name = InputDati.leggiStringaNonVuota(MSG_INS_NOME);
			surname = InputDati.leggiStringaNonVuota(MSG_INS_COGNOME);

			do {
				user = InputDati.leggiStringaNonVuota(MSG_INS_USERNAME);

				//controllo che lo username non sia già presente
				confFound = jsConfigurator.searchElement("username", user);
				if(confFound)
					System.out.println(MSG_ERR_USERNAME_GIA_PRESENTE);

				//controllo che lo username non sia uguale a nessun nickname di nessun volontario
				volFound = jsVolunteer.searchElement("nickname", user);
				if(volFound)
					System.out.println(MSG_ERR_USER_NICK);

				userFound = jsUser.searchElement("username", user);
				if(userFound)
					System.out.println(MSG_ERR_USERNAME_GIA_PRESENTE);

			}while(confFound || volFound || userFound);

			password = InputDati.leggiStringaNonVuota(MSG_INS_PASSWORD);

		}while(!InputDati.yesNo(MSG_CONFERMA_REGISTRAZIONE));

		return new String[]{name, surname, user, password};
	}

	/*--------> metodo primo accesso configuratore */
	private static Configurator firstAccess(JsonManager jsConfigurator) {
		JsonManager jsVolunteer = new JsonManager(FILE_VOLONTARIO);
		JsonManager jsUser = new JsonManager(FILE_FRUITORE);

		String[] credentials = getCredentials(jsConfigurator, jsVolunteer, jsUser);

		Configurator utenteRegistrato= new Configurator(credentials[0], credentials[1], credentials[2], credentials[3]);
		jsConfigurator.addElement(Configurator.class, utenteRegistrato);

		if(jsConfigurator.searchElement("firstAbsoluteAccess", "true"))
			firstAbsoluteAccess(jsConfigurator);

		System.out.println(MSG_REGISTRAZIONE_CONFERMATA);
		return utenteRegistrato;
	}

	/*--------> metodo primo accesso utente */
	private static User firstAccess(String path) {
		JsonManager jsVolunteer = new JsonManager(FILE_VOLONTARIO);
		JsonManager jsConfigurator = new JsonManager(FILE_FRUITORE);
		JsonManager jsUser = new JsonManager(path);

		String[] credentials = getCredentials(jsConfigurator, jsVolunteer, jsUser);

		User utenteRegistrato= new User(credentials[0], credentials[1], credentials[2], credentials[3]);
		jsConfigurator.addElement(User.class, utenteRegistrato);

		if(jsConfigurator.searchElement("firstAbsoluteAccess", "true"))
			firstAbsoluteAccess(jsConfigurator);

		System.out.println(MSG_REGISTRAZIONE_CONFERMATA);
		return utenteRegistrato;
	}

	// metodo di primo accesso di un volontario
	private static void firstAccess(JsonManager jsonManager, Volunteer volunteer) {
		String password;

		do {
			password = InputDati.leggiStringaNonVuota(MSG_INS_PASSWORD);
		}while(!InputDati.yesNo(MSG_CONFERMA_REGISTRAZIONE));

		volunteer.setPassowrd(password);
		jsonManager.modifyElement("nickname", volunteer.getNickname(), volunteer);

		DataConsistencyService.updatePasswordVolunteer(volunteer); //--> modifica della password anche nei tipi visita
	}

	/*--------> metodo per il primo accesso assoluto */
	private static void firstAbsoluteAccess(JsonManager jsonManager) {
		System.out.println(MSG_TITOLO_PRIMO_ACCESSO_ASSOLUTO);

		PlaceManager.setRegion();

		setNumberOfSub();

		jsonManager.modifyObject("firstAbsoluteAccess", "true", "false");

	}

	//++++++++++++++++++ Metodo che effettua il LOGIN ++++++++++++++++++++++++++
	private static Person login(String path, String field, Class<? extends Person> classType) {
		String user, password;
		JsonManager jsonManager = new JsonManager(path);
		boolean userFound, pwFound;

		do {
			user = InputDati.leggiStringaNonVuota(MSG_INS_USERNAME);
			password = InputDati.leggiStringaNonVuota(MSG_INS_PASSWORD);

			userFound = jsonManager.searchElement(field, user);
			pwFound = jsonManager.searchElement("password", password);

			if(jsonManager.searchElement("user", user) && jsonManager.searchElement("pw", password)) { //accesso con le credenziali standard = primo accesso
				System.out.println(MSG_TITOLO_WELCOME_CONFIG);
				return firstAccess(jsonManager);
			}
			else if(!userFound)
				System.out.println(MSG_ERR_USERNAME_NON_PRESENTE);
			else if(!pwFound)
				System.out.println(MSG_ERR_PASSWORD_ERRATA);
			else if(userFound && password.equals("volunteer")) {
				Volunteer volunteer = jsonManager.searchElement(Volunteer.class, "nickname", user);
				if(volunteer.getPassword().equals("volunteer")) {
					System.out.println(MSG_TITOLO_WELCOME_VOLUNTEER);

					//faccio il primo accesso per il volontario
					firstAccess(jsonManager, volunteer);

					return volunteer;
				}
			}
			else  //ACCESSO CORRETTO E UTENTE GIà INSERITO
				System.out.println(MSG_TITOLO_BENTORNATO);

		}while(!userFound || !pwFound);

		if(userFound && pwFound)
			return jsonManager.searchElement(classType, field, user);
		else
			return null;
	}

	/* metodo per il login del configuratore */
	private static Person loginConfigurator(String path) {
		return login(path, "username", Configurator.class);
	}

	/* metodo per il login del volontario */
	private static Person loginVolunteer(String path) {
		return login(path, "nickname", Volunteer.class);
	}

	/* metodo per il login del fruitore */
	private static Person loginUser(String path) {
		return login(path, "username", User.class);
	}

	/* metodo per il menù iniziale del login */
	public static Person run(String pathConfigurator, String pathVolunteer, String pathUser) {
		MenuLayout menu= new MenuLayout(MSG_WELCOME,VOCI_MENU_GENERALE);
		int opz;
		Person person = null;

		//ciclo di esecuzione
		do {
			opz=InputDati.leggiIntero(menu.visualizzaMenu(), 0, menu.getNumVoci()-1);

			switch (opz) {
				case 1: {
					person = loginConfigurator(pathConfigurator);
					break;
				}
				case 2: {
					return loginVolunteer(pathVolunteer);
				}
				case 3: {
					person = runUser(pathUser);
					break;
				}
				default:
					System.out.println(MSG_EXIT);
					break;
			}

		}while(opz!=0 && person == null);

		return person;
	}
	/* metodo per il menù del login dell'utente */
	private static Person runUser(String pathUser) {
		MenuLayout menu= new MenuLayout(MSG_WELCOME,VOCI_MENU_UTENTE);
		int opz;

		//ciclo di esecuzione
		do {
			opz=InputDati.leggiIntero(menu.visualizzaMenu(), 0, menu.getNumVoci()-1);

			switch (opz) {
				case 1: {
					return firstAccess(pathUser);
				}
				case 2: {
					return loginUser(pathUser);
				}
				default:
					break;
			}

		}while(opz!=0);

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

	public static boolean existUser(String username) {
		JsonManager jsUser = new JsonManager(FILE_FRUITORE);
		return (jsUser.searchElement("username", username));
	}

	//metodo per aggiornare lo stato delle visite quando si aggiorna il giorno
	public static void updateVisitState(LocalDate time) {
		VisitList visitList = new VisitList(FILE_VISITE);
		RegistrationList registrationList = new RegistrationList(FILE_ISCRIZIONI);

		for(Visit visit : visitList.getVisitList().keySet()) {
			visit = visitList.nextState(visit, registrationList.getTotalRegistrationForVisit(visit), time);
			DataConsistencyService.updateRegistrationState(visit);
		}
	}

}
