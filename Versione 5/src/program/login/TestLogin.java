package program.login;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import program.users.Configurator;
import program.users.Person;
import program.users.User;
import program.users.volunteer.Volunteer;
import utilities.JsonManager;

class TestLogin {
	
	private static final String FILE_FRUITORE = "json/fruitore.json";
	private static final String FILE_VOLONTARIO = "json/volontario.json";
	private static final String FILE_CONFIGURATORE = "json/configuratore.json";
	private static final String FILE_TIPOVISITA = "json/tipoVisita.json";
	
	private String name;
	String surname;
	String nickname;
	String password;
	
	//classe fake view per i test
	public class FakeLoginViewBase implements LoginView {
		@Override public String readUser() { return nickname; }
		@Override public String readSurname() { return surname; }
		@Override public Class<? extends Person> readRole() { return null; } // default
		@Override public String readPassword() { return password; }
		@Override public int readNumberOfSub() { return 0; }
		@Override public String readName() { return name; }
		@Override public boolean readConfirmRegistration() { return true; }
		@Override public boolean readConfirmChoiceRegistration() { return true; }
		@Override public void printMessageWelcomeNewVolunteer() {}
		@Override public void printMessageWelcomeNewUser() {}
		@Override public void printMessageWelcomeNewConfigurator() {}
		@Override public void printMessageWelcomeApp() {}
		@Override public void printMessageOkLogin() {}
		@Override public void printMessageForFirstAbsoluteAccess() {}
		@Override public void printMessageErrorUsernameInsert() {}
		@Override public void printMessageErrorLogin() {}
	}
	
	
	//testo il login per i volontari
	@Test
	public void loginVolunteer() {
		//setto i dati per il test del volontario
		name= "Alberto";
		surname= "Ferrari";
		nickname= "albertoferro";
		password= "0000";
		
		//Stub di ILoginView tramite classe anonima
		LoginView fakeLoginView = new FakeLoginViewBase() {
			@Override public Class<? extends Person> readRole() {
				return Volunteer.class;
			}
		};
		
		//istanzio i dati per i login
		LoginManager manager= new LoginManager(fakeLoginView, new JsonManager(FILE_CONFIGURATORE), new JsonManager(FILE_VOLONTARIO), new JsonManager(FILE_FRUITORE), new JsonManager(FILE_TIPOVISITA));
		
		//richiamo il metodo da testare
		Volunteer logged= (Volunteer)manager.login();
		
		//controllo che il login sia corretto
		assertTrue(logged.getRole().equals("Volunteer"));
		assertTrue(logged.getName().equals(name));
		assertTrue(logged.getSurname().equals(surname));
		assertTrue(logged.getNickname().equals(nickname));	
		assertTrue(logged.getPassword().equals(password));
	}
	
	//testo il login per i configuratori
	@Test
	public void loginConfigurator() {
		//setto i dati per il test del configuratore
		name= "Alessandro";
		surname= "Belussi";
		nickname= "alebelu";
		password= "0000";
			
		//Stub di ILoginView tramite classe anonima
		LoginView fakeLoginView = new FakeLoginViewBase() {
			@Override public Class<? extends Person> readRole() {
				return Configurator.class;
			}
		};
			
		//istanzio i dati per i login
		LoginManager manager= new LoginManager(fakeLoginView, new JsonManager(FILE_CONFIGURATORE), new JsonManager(FILE_VOLONTARIO), new JsonManager(FILE_FRUITORE), new JsonManager(FILE_TIPOVISITA));
		
		//richiamo il metodo da testare
		Configurator logged= (Configurator)manager.login();
		
		//controllo che il login sia corretto
		assertTrue(logged.getRole().equals("Configurator"));
		assertTrue(logged.getName().equals(name));
		assertTrue(logged.getSurname().equals(surname));
		assertTrue(logged.getUsername().equals(nickname));	
		assertTrue(logged.getPassword().equals(password));
	}
	
	//testo il login per i fruitori
	@Test
	public void loginUser() {
		//setto i dati per il test dell'utente
		name= "Mario";
		surname= "Rossi";
		nickname= "mariorossi";
		password= "italia";
				
		//Stub di ILoginView tramite classe anonima
		LoginView fakeLoginView = new FakeLoginViewBase() {
			@Override public Class<? extends Person> readRole() {
				return User.class;
			}
		};
				
		//istanzio i dati per i login
		LoginManager manager= new LoginManager(fakeLoginView, new JsonManager(FILE_CONFIGURATORE), new JsonManager(FILE_VOLONTARIO), new JsonManager(FILE_FRUITORE), new JsonManager(FILE_TIPOVISITA));
		
		//richiamo il metodo da testare
		User logged= (User)manager.login();
		
		//controllo che il login sia corretto
		assertTrue(logged.getRole().equals("User"));
		assertTrue(logged.getName().equals(name));
		assertTrue(logged.getSurname().equals(surname));
		assertTrue(logged.getUsername().equals(nickname));	
		assertTrue(logged.getPassword().equals(password));
	}
}
