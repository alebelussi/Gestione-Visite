package program.login.chain;

import program.users.Person;

//=> Interfaccia Handler che viene implementata dai singoli componenti della chain
public interface LoginHandler {
	Person handleRequest(Class<? extends Person> role, String user, String password);
}
