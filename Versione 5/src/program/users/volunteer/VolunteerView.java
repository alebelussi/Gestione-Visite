package program.users.volunteer;

import java.time.LocalDate;
import java.time.Month;
import utilities.TimeSimulator;

public interface VolunteerView {
	
    String askNickname();
    boolean confirmInput();
    String askNameVolunteer();
    String askSurnameVolunteer();
    boolean askContinueAdding();
    boolean askConfirmAvailability();
    boolean askConfirmRemoval();
    boolean askRemoveAvailability();
    boolean askConfirmAddVolunteer();
    int askDayOfMonth(Month modifyMonth, TimeSimulator time);
    void showVolunteer(Volunteer volunteer);
    void showNameEqualConfiguratorError();
    void showNameEqualUserError();
    void showVolunteerNotFoundError();
    void showNoVolunteerRegistered();
    void showDateAlreadyExcludedError();
    void showDateExcludedError();
    void showNoVisitTypeError();
    void showConfirmAvailability();
    void showNoVolunteerAvailabile();
    void showConfimRemoval();
    void showRemovalCancelled();
    void showNotFoundDataError();
    String showMessageExcludedDate();
    String showMessageDateForAvailability();
    void showAvailabilityDate(LocalDate date);
    void showAvailabilityMessage();
}
