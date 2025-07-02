package program.visitistance;

import java.time.LocalDate;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import utilities.RepositorySystem;
import utilities.TimeSimulator;

public class ExcludedDates {

	private TreeSet<LocalDate> dateExcluded;
	private RepositorySystem repositoryExcludedDates;
	private ExcludedDatesView view;

	public ExcludedDates(RepositorySystem repositoryExcludedDates) {
		this.repositoryExcludedDates = repositoryExcludedDates;
		this.dateExcluded = new TreeSet<>();
		this.view = new ConsoleExcludedDatesView();
		loadDates();
	}
	
	public ExcludedDates(RepositorySystem repositoryExcludedDates, ExcludedDatesView excludedDatesView) {
		this.repositoryExcludedDates = repositoryExcludedDates;
		this.dateExcluded = new TreeSet<>();
		this.view = excludedDatesView;
		loadDates();
	}

	private void loadDates() {
		List<LocalDate> list = repositoryExcludedDates.loadData(LocalDate.class);
		for(LocalDate date : list) {
			dateExcluded.add(date);
		}
	}

	public ArrayList<String> getArrayListExcludedDate(){
		ArrayList<String> dateExcludedStr = new ArrayList<>();
	    for (LocalDate date : dateExcluded) {
	        dateExcludedStr.add(date.toString()); // Converte LocalDate in String formato "YYYY-MM-DD"
	    }
	    return dateExcludedStr;
	}

	public void excludeDate(TimeSimulator time) {
		Month modifyMonth= time.getCurrentDate().getMonth().plus(3);
		int dateNumber;
		//scrivo la data esclusa nel file JSON
		LocalDate dataEsclusa;
		//lettura data da escludere
		do {
			do{
				dateNumber= view.askExcludedDate(modifyMonth, time);
				dataEsclusa = LocalDate.of(time.getCurrentDate().getYear(), modifyMonth.getValue(), dateNumber);

				if(dateExcluded.contains(dataEsclusa))
					view.showDateAlreadyExist();

			}while(dateExcluded.contains(dataEsclusa));

		}while(!view.askConfirmationExcludeData());


		repositoryExcludedDates.addElement(String.class, dataEsclusa.toString());
		dateExcluded.add(dataEsclusa);

		view.showConfirmationExcludeData();
	}

	public void viewExcludedDate(TimeSimulator time) {
		Month modifyMonth = time.getCurrentDate().getMonth().plus(3);
		TreeSet<LocalDate> filteredDates = new TreeSet<>();
		
		for(LocalDate date : dateExcluded) {
			if(date.getMonth() == modifyMonth)
				filteredDates.add(date);
		}

		if(filteredDates.isEmpty())
			view.showNoDateEntered();
		else {
			view.showMessageHeadView();
			for(LocalDate date : filteredDates)
				view.showDate(date);
		}
	}

	public void removeExcludedDate(TimeSimulator time) {
		Month modifyMonth = time.getCurrentDate().getMonth().plus(3);
		int dateNumber;
		LocalDate dataEsclusa;

		
		do {
			dateNumber= view.askExcludedDate(modifyMonth, time);
			dataEsclusa = LocalDate.of(time.getCurrentDate().getYear(), modifyMonth.getValue(), dateNumber);

			if(!dateExcluded.contains(dataEsclusa))
				view.showDateNotFound();
		}while(!dateExcluded.contains(dataEsclusa));
		
		if(view.askConfirmationRemoveData()) {
			repositoryExcludedDates.removeElement(String.class, dataEsclusa.toString());
			dateExcluded.remove(dataEsclusa);
			view.showConfirmRemoveDate();
		}else {
			view.showNoOperation();
		}
	}
	
	public boolean isDateExcluded(LocalDate date) {
		for(LocalDate dateExaminate: dateExcluded)
			if(dateExaminate.compareTo(date)==0)	return true;
		return false;
	}

}