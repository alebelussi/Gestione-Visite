package program.login;

import program.users.Person;

public interface LoginView {	
	
	Class<? extends Person> readRole();
	String readUser();
	String readPassword();
	void printMessageWelcomeApp();
	boolean readConfirmRegistration();
	boolean readConfirmChoiceRegistration();
	void printMessageWelcomeNewVolunteer();
	void printMessageWelcomeNewUser();
	void printMessageWelcomeNewConfigurator();
	void printMessageErrorLogin();
	void printMessageOkLogin();
	String readName();
	String readSurname();
	void printMessageErrorUsernameInsert();
	void printMessageForFirstAbsoluteAccess();
	int readNumberOfSub();
	
}
