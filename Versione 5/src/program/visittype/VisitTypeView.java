package program.visittype;

import java.time.DayOfWeek;

public interface VisitTypeView {

    String askTitle();
    String askDescription();
    String askMeetingPoint();
    String askStartDate();
    String askEndDate();
    DayOfWeek askDay();
    int askDuration();
    boolean askNeedBuyTicket();
    int askMinParticipant();
    int askMaxParticipant(int minParticipant);
    String askStartHour();
    int askMenuVoice();
    boolean confirmInsertAnotherDay();
    boolean confirmInsert();
    boolean confirmModify();
    boolean confirmAssociation();
    boolean confirmRemove();
    void showTitleVisitView();
    void showVisitType(VisitType visitType);
    void showAlreadyInserted();
    void showTimeOverlapError();
    void showInsertSuccess();
    void showModifySuccess();
    void showCancelModify();
    void showAssociationSuccess();
    void showVisitTypeNotFoundError();
    void showVisitTypeNoAvailable();
    void showRemoveSuccess();
    void showCancelRemove();
    void confirmInsertMessage();
    void showErrorNoVisitType();
}
