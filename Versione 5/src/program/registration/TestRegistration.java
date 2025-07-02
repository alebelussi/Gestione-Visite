package program.registration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

//import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import program.login.LoginView;
import program.login.LoginManager;
import program.login.UserLoginStrategy;
import program.places.Place;
import program.users.Person;
import program.users.User;
import program.users.volunteer.Volunteer;
import program.visitFormatter.VisitFormatter;
import program.visitistance.VisitView;
import program.visitistance.ConsoleVisitView;
import program.visitistance.Visit;
import program.visitistance.VisitList;
import program.visitistance.VisitManager;
import program.visitistance.state.VisitStateEnum;
import program.visittype.VisitType;
import utilities.JsonManager;
import utilities.RepositorySystem;

class TestRegistration {
	
	private File tempVisitJsonFile;
    private File tempVisitTypeJsonFile;
    private File tempExcludedDatesJsonFile;
    private File tempRegistrationFile; 
    private File tempUserJsonFile; 
    
    private RepositorySystem visitRepository;
    private RepositorySystem visitTypeRepository;
    private RepositorySystem excludedDatesRepository;
    private RepositorySystem userRepository;
    private RepositorySystem registrationRepository;
    
    private LoginManager fakeLoginManager;
    private LocalDate time; 
    private VisitType visitType;
    private VisitList visitList;
    private Visit testVisit;
	
    private VisitType createVisitType() {
    	String title = "Visita Guidata", description = "Visita culturale al Colosseo di Roma", meetingPoint = "Colosseo";
		String startDate = "2025-03-01", endDate = "2025-11-30", startHour = "11.30";
		ArrayList<DayOfWeek> days = new ArrayList<>(List.of(DayOfWeek.SUNDAY));
		int duration = 60, minParticipant = 0, maxParticipant = 20;
		
		ArrayList<Volunteer> volunteers = new ArrayList<>();
		volunteers.add(new Volunteer("Mario", "Rietti", "Marietto", "rietto"));
		
		Place place = new Place("Colosseo", "Anfiteatro romano", "Piazza del Colosseo", "Roma");
		return new VisitType(title, description, meetingPoint, startDate, endDate, days, startHour, 
				duration, false, minParticipant, maxParticipant, place, volunteers);
    }
    
    private void initRepository() throws IOException {
    	tempRegistrationFile = File.createTempFile("registration_test", ".json"); //--> creazione di un file Json di Test
		registrationRepository= new JsonManager(tempRegistrationFile.getPath());
		 
		tempVisitJsonFile = File.createTempFile("visite_test", ".json");
		tempVisitTypeJsonFile = File.createTempFile("visitType", ".json");
		tempExcludedDatesJsonFile = File.createTempFile("excludedDates_test", ".json");
		String initialJson = """
				[
				  {
				    "numberOfSub": 50
				  } 
				]
				""";

		Files.writeString(tempVisitJsonFile.toPath(), initialJson);
		Visit.setNumberOfSub(50); // imposta il numero massimo globale di iscritti per test
		    
		visitRepository = new JsonManager(tempVisitJsonFile.getAbsolutePath());
		visitTypeRepository = new JsonManager(tempVisitTypeJsonFile.getAbsolutePath());
		excludedDatesRepository = new JsonManager(tempExcludedDatesJsonFile.getAbsolutePath());
		
		tempUserJsonFile = File.createTempFile("users_test_ test", ".json");
		userRepository = new JsonManager(tempUserJsonFile.getAbsolutePath());

		// Pre-popola con l'utente apo03 se ti serve evitare la registrazione:
		String initialUserJson = """
			    [{"name": "Alice", "surname": "Rossi", "username": "apo03", "password": "hinge"}]
			""";
		Files.writeString(tempUserJsonFile.toPath(), initialUserJson);
    }
    
	 @BeforeEach	//Creazione del setup prima del test
	 public void setup() throws IOException {      
		
		initRepository();
		 
		time = LocalDate.of(2025, 6, 18);
		
		
		LoginView loginView = new LoginView() {

			@Override public Class<? extends Person> readRole() { return User.class; }

			@Override public String readUser() { return "apo03"; }
			@Override public String readPassword() { return "hinge"; }

			@Override public void printMessageWelcomeApp() {}
			@Override public boolean readConfirmRegistration() { return true; }
			@Override public boolean readConfirmChoiceRegistration() { return false; }
			@Override public void printMessageWelcomeNewVolunteer() {}
			@Override public void printMessageWelcomeNewUser() {}
			@Override public void printMessageWelcomeNewConfigurator() {} 
			@Override public void printMessageErrorLogin() {} 
			@Override public void printMessageOkLogin() {}
			@Override public String readName() { return null; }
			@Override public String readSurname() { return null; }
			@Override public void printMessageErrorUsernameInsert() {}
			@Override public void printMessageForFirstAbsoluteAccess() {}
			@Override public int readNumberOfSub() { return 0; }
			
		};
		
		fakeLoginManager = new LoginManager(loginView, null, null, userRepository, null) {
		    @Override
		    protected boolean isOkUsernameInsert(String username) { return true; }
		    @Override
		    protected boolean isChoiceRegistration() { return false; }
		    @Override
		    protected User readNewUserData(String username, String password) {
		        return new User("Alice", "Rossi", username, password);
		    }
		};
		
		visitList = new VisitList(visitRepository);
		
		visitType = createVisitType();
		testVisit = new Visit(visitType, LocalDate.of(2025, 6, 15).toString(), VisitStateEnum.PROPOSTA);
		visitList.addVisit(testVisit);
		
	 }

	 @AfterEach	//Dopo l'esecuzione del test
	 public void teardown() {
		    tempRegistrationFile.delete();
		    tempVisitJsonFile.delete();
		    tempVisitTypeJsonFile.delete();
		    tempExcludedDatesJsonFile.delete();
		    tempUserJsonFile.delete();
		}
	
	 
	 @Test
	 public void add() {
		 
		 //valori simulati per input
		 String code= "SUB-CB2991";
		 int numberOfSub= 5;
		 String userName= "apo03";
		 String pswUser= "hinge";

		 //Flag per verificare se il messaggio di successo è stato mostrato
	     //final boolean[] successShown = {false};

	     //Stub di IRegistrationView mediante classe anonima
	     RegistrationView fakeView = new RegistrationView() {
	    	//metodo che richiede il numero di persone => ritorna il dato da testare
			@Override public int askNumberOfPeople() { return numberOfSub; }
	    	 
			//metodo che richiede la conferma della registrazione => ritorna true
			@Override public boolean confirmRegistration() { return true; }
	    	 
			//metodi della view di stampa NO-OP
			@Override public void showRegistrationSuccess(String code) {}
			@Override public void showRegistrationOwnerError() {}
			@Override public void showRegistrationNotFoundError() {}
			@Override public void showRegistration(Registration registration) {}
			@Override public void showMessage(String message) {}
			@Override public void showMaxParticipantsExceededError() {}
			@Override public void showCancellationSuccess() {}
			@Override public void showAlreadyRegisteredError() {}
			@Override public void showAbsoluteMaxExceededError() {}
			@Override public boolean confirmUnregistration() { return false; }
			@Override public boolean confirmSearch() { return false; }
			@Override public String askRegistrationCode() { return null; }
			@Override public void showCode(String code) {}
			
		};
		
		VisitView visitView = new VisitView() {
			@Override public int askCodeVisit() { return 1; }
			@Override public boolean confirmSearch() { return true; }
			//NO OP
			@Override public int readNumberOfSub() { return 0; }
			@Override public void showNoVisitAvailable() {}
			@Override public void showVisitNotFound(){}
			@Override public void showVisit(Visit visit) {}
			@Override public void showVisit(Visit visit, Map<VisitStateEnum, VisitFormatter> visitFormatter) {}
			@Override public Map<VisitStateEnum, VisitFormatter> getVisitFormatter() { return null; }
			
		};
	    
		//setto le informazioni per settare il metodo
		UserLoginStrategy strategy = new UserLoginStrategy(fakeLoginManager, userRepository);
		User userLogged = (User) strategy.login(userName, pswUser);
		VisitManager visitManager = new VisitManager(visitView, visitTypeRepository, excludedDatesRepository, visitRepository);
		RegistrationManager manager = new RegistrationManager(visitManager, fakeView, visitView, registrationRepository) {
			@Override
			protected String generateCode() {
				return code;
			}
		};
		
		//chiamo il metodo da testare
		manager.add(userLogged, time);
		assertEquals(numberOfSub, numberOfSub);
		
		Registration registration = manager.getRegistrationList().searchRegistration(code);
		
		assertNotNull(registration);
		assertEquals(1, manager.getRegistrationList().getRegistrationList().size());
		assertEquals(code, registration.getCode());
		assertEquals(userName, registration.getUser().getUsername());
	 }
	 
	 
	 @Test
	 public void remove() {
		 //valori simulati per input
		 String code= "SUB-CB2991";
		 int numberOfSub= 5;
		 String userName= "apo03";
		 String pswUser= "hinge";

		 //Flag per verificare se il messaggio di successo è stato mostrato
	     //final boolean[] successShown = {false};

	     //Stub di IRegistrationView mediante classe anonima
	     ConsoleRegistrationView fakeView = new ConsoleRegistrationView() {
	    	//metodo che richiede il numero di persone => ritorna il dato da testare
			@Override public int askNumberOfPeople() { return numberOfSub; }
	    	 
			//metodo che richiede la conferma della registrazione => ritorna true
			@Override public boolean confirmRegistration() { return true; }
			
			@Override public boolean confirmUnregistration() { return true; }
			
			@Override public String askRegistrationCode() { return code; }
	    	 
			//metodi della view di stampa NO-OP
			@Override public void showRegistrationSuccess(String code) {}
			@Override public void showRegistrationOwnerError() {}
			@Override public void showRegistrationNotFoundError() {}
			@Override public void showRegistration(Registration registration) {}
			@Override public void showMessage(String message) {}
			@Override public void showMaxParticipantsExceededError() {}
			@Override public void showCancellationSuccess() {}
			@Override public void showAlreadyRegisteredError() {}
			@Override public void showAbsoluteMaxExceededError() {}
			@Override public boolean confirmSearch() { return false; }
		};
		
		ConsoleVisitView visitView = new ConsoleVisitView() {

			@Override public int askCodeVisit() { return 1; }

			@Override public boolean confirmSearch() { return true; }

			//NO OP
			@Override public int readNumberOfSub() { return 0; }
			@Override public void showNoVisitAvailable() {}
			@Override public void showVisitNotFound() {}
			@Override public void showVisit(Visit visit) {}
			@Override public void showVisit(Visit visit, Map<VisitStateEnum, VisitFormatter> visitFormatter) {}
			@Override public Map<VisitStateEnum, VisitFormatter> getVisitFormatter() { return null; }
			
		}; 
	    
		//setto le informazioni per settare il metodo
		UserLoginStrategy strategy = new UserLoginStrategy(fakeLoginManager, userRepository);
		User userLogged = (User) strategy.login(userName, pswUser);
		VisitManager visitManager = new VisitManager(visitView, visitTypeRepository, excludedDatesRepository, visitRepository);
		//RegistrationManager manager= new RegistrationManager(registrationList, visitManager, fakeView, null);
		RegistrationManager manager = new RegistrationManager(visitManager, fakeView, visitView, registrationRepository) {
			@Override
			protected String generateCode() {
				return code;
			}
		};

		manager.add(userLogged, time);
		Registration registration = manager.getRegistrationList().searchRegistration(code);
		
		manager.remove(userLogged, time);
		
		assertEquals(0, manager.getRegistrationList().getRegistrationList().size());
		assertFalse(manager.getRegistrationList().getRegistrationList().contains(registration));
		
	 }
}