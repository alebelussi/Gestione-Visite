package program.dashboard.view;

import java.time.LocalDate;
import program.users.Person;

public interface DashboardConfiguratorView {

    void printMenuChoiceError();
    void printMenuPlace();
    void printMenuVisitType();
    void printMenuVisitIstance();
    void printMenuVolunteer();
    void printMainMenu();
    void printUserLogged(Person user);
    void printCurrentDate(LocalDate currentDate);
    void printGoodbye(Person user);
}
