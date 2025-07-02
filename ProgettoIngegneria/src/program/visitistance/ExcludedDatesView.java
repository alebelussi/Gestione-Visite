package program.visitistance;

import java.time.LocalDate;
import java.time.Month;

import utilities.TimeSimulator;

public interface ExcludedDatesView {
	
	int askExcludedDate(Month modifyMonth, TimeSimulator time);
	boolean askConfirmationExcludeData();
	boolean askConfirmationRemoveData();
	void showDateAlreadyExist();
	void showConfirmationExcludeData();
	void showMessageHeadView();
	void showDate(LocalDate date);
	void showDateNotFound();
	void showNoDateEntered();
	void showConfirmRemoveDate();
	void showNoOperation();
}
