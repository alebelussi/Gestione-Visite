package program.dashboard.view;

import java.time.LocalDate;
import program.users.Person;

public interface DashboardVolunteerView {

    void printMainMenu();
    void printMenuChoiceError();
    void printGoodbye(Person user);
    void printCurrentDate(LocalDate currentDate);
    void printUserLogged(Person user);
}