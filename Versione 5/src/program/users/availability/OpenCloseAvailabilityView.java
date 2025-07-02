package program.users.availability;

public interface OpenCloseAvailabilityView {

    boolean askAvaibilityOpen();
    void showAvailabilityOpen();
    void showAvailabilityOpeningCancelled();
    void showAvailabilityOpeningImpossible();
}
