package program.registration;

public interface RegistrationView {
    
    int askNumberOfPeople();
    String askRegistrationCode();
    boolean confirmRegistration();
    boolean confirmUnregistration();
    boolean confirmSearch();
    void showRegistration(Registration registration);
    void showMaxParticipantsExceededError();
    void showAbsoluteMaxExceededError();
    void showAlreadyRegisteredError();
    void showRegistrationSuccess(String code);
    void showRegistrationNotFoundError();
    void showRegistrationOwnerError();
    void showCancellationSuccess();
    void showMessage(String message);
    void showCode(String code);
}
