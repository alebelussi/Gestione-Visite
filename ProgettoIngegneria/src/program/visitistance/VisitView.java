package program.visitistance;

import java.util.Map;

import program.visitFormatter.VisitFormatter;
import program.visitistance.state.VisitStateEnum;

public interface VisitView {
    
    int askCodeVisit();
    boolean confirmSearch();
    int readNumberOfSub();
    void showNoVisitAvailable();
    void showVisitNotFound();
    void showVisit(Visit visit);
    void showVisit(Visit visit, Map<VisitStateEnum, VisitFormatter> visitFormatter);
    Map<VisitStateEnum, VisitFormatter> getVisitFormatter();
}
