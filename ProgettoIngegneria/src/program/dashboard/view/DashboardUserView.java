package program.dashboard.view;

import java.time.LocalDate;
import program.users.Person;

public interface DashboardUserView {

    void printMainMenu();
    void printGoodbye(Person user);
    void printCurrentDate(LocalDate currentDate);
    void printUserLogged(Person user);
    void printMenuVisit();
    void printMenuRegistration();
    void printMenuChoiceError();
}