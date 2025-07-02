package program.login;

import program.users.Person;

public interface LoginStrategy {

	//metodo di login
	Person login(String user, String psw);
}
