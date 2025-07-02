package program.users.volunteer;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import program.users.Configurator;
import program.users.User;
import program.visitistance.ExcludedDates;
import program.visitistance.VisitManager;
import program.visittype.VisitTypeList;
import utilities.JsonManager;
import utilities.RepositorySystem;
import utilities.TimeSimulator;

public class VolunteerManager {
	
	private VolunteerList volunteerList;
	private ArrayList<Volunteer> lastVolunteersInsert;
	private VolunteerView volunteerView;
	
	public VolunteerManager(VolunteerView view, RepositorySystem repositoryVolunteer) {
		this.volunteerList = new VolunteerList(repositoryVolunteer);
		this.lastVolunteersInsert = new ArrayList<>();
		this.volunteerView =view;
	}
	
	public VolunteerManager(VolunteerView view, VolunteerList volunteerList) {
		this.volunteerList = volunteerList;
		this.lastVolunteersInsert = new ArrayList<>();
		this.volunteerView =view;
	}
	/*@
		requires volunteerList != null;
		
	 	ensures volunteerList.findVolunteer(nickname);
	 	ensures volunteerList.size() == \old(volunteerList.size()) + 1;
	 @*/
	public void add() {
		String nickname;
		//pulisco la vecchia lista di volontari inseriti
		this.lastVolunteersInsert.clear();
		
		do {
			
			do {
				nickname = volunteerView.askNickname();
				processNickname(nickname);
				
			}while(lastVolunteersInsert.isEmpty());
			
		}while(volunteerView.askContinueAdding());
	}
	
	private void processNickname(String nickname) {
	    final String finalNickname = nickname;

	    if (Configurator.existConfigurator(finalNickname, new JsonManager("json/configuratore.json"))) {
	        volunteerView.showNameEqualConfiguratorError();
	        return;
	    }

	    if (User.existUser(finalNickname,new JsonManager("json/fruitore.json"))) {
	        volunteerView.showNameEqualUserError();
	        return;
	    }

	    if (!volunteerList.findVolunteer(nickname) &&
	        lastVolunteersInsert.stream().noneMatch(v -> finalNickname.equals(v.getNickname()))) {

	        volunteerView.showVolunteerNotFoundError();

	        if (volunteerView.confirmInput()) {
	            String name = volunteerView.askNameVolunteer();
	            String surname = volunteerView.askSurnameVolunteer();
	            Volunteer newVolunteer = new Volunteer(name, surname, nickname, "volunteer");
	            volunteerList.addVolunteer(newVolunteer);
	            lastVolunteersInsert.add(newVolunteer);
	        }

	    } else {
	        Volunteer volunteer = volunteerList.getVolunteer(nickname);
	        lastVolunteersInsert.add(volunteer);
	    }

	    if (lastVolunteersInsert.isEmpty()) {
	        volunteerView.showNoVolunteerRegistered();
	    }
	}

	
	public void view() {
		if(volunteerList.isEmpty())
			volunteerView.showNoVolunteerRegistered();
		else {
			for(Volunteer volunteer : volunteerList.getAllVolunteers()) {
				volunteerView.showVolunteer(volunteer);
			}
		}
	}
	
	public void viewAvailability(Volunteer volunteer) {
		volunteerView.showAvailabilityMessage();
		for(LocalDate date : volunteerList.getDateAvailability(volunteer)) {
			volunteerView.showAvailabilityDate(date);
		}
	}
	
	public void searchVolunteer() {
		String nickname;
		Volunteer volunteerSearched;
		
		nickname = volunteerView.askNickname();
		volunteerSearched= volunteerList.getVolunteer(nickname);
		
		if(volunteerSearched==null)
			volunteerView.showVolunteerNotFoundError();
		else
			volunteerView.showVolunteer(volunteerSearched); 
	}
	
	public void addAvailability(Volunteer volunteer, TimeSimulator time, ExcludedDates excludedDate, VisitTypeList visitTypeList) {
		ArrayList<LocalDate> dateAvailable = new ArrayList<>();
		LocalDate baseDate = time.getDateForAvailability();
		Month modifyMonth = baseDate.getMonth();
		int plusMonths = (baseDate.getMonthValue() - time.getCurrentDate().getMonthValue());
		LocalDate date;

		do {
			date = askValidDate(volunteer, time, excludedDate, visitTypeList, baseDate, modifyMonth, plusMonths);
			
		} while (!volunteerView.askConfirmAvailability());

		dateAvailable.add(date);
		this.volunteerList.addAvailability(volunteer, dateAvailable);
		volunteerView.showConfirmAvailability();
	}
	
	protected LocalDate askValidDate(Volunteer volunteer, TimeSimulator time, ExcludedDates excludedDate, VisitTypeList visitTypeList, LocalDate baseDate, Month modifyMonth, int plusMonths) {
		LocalDate date;
		boolean volunteerAvailable, dateNotOk, isDateOfTypeVisit;
		
		do {
			int dateNumber = volunteerView.askDayOfMonth(modifyMonth, time);
			date = LocalDate.of(baseDate.getYear(), modifyMonth.getValue(), dateNumber);
			
			volunteerAvailable = volunteer.isAvailableOn(date);
			dateNotOk = excludedDate.isDateExcluded(date);
			isDateOfTypeVisit = (new VisitManager(null, new JsonManager("json/tipoVisita.json"), new JsonManager("json/dateEscluse.json"),new JsonManager("json/visite.json"))).isDateOfVisitType(time, volunteer, date, plusMonths);
			
			if (volunteerAvailable)
				volunteerView.showDateAlreadyExcludedError();
			else if (dateNotOk)
				volunteerView.showDateExcludedError();
			else if (!isDateOfTypeVisit)
				volunteerView.showNoVisitTypeError();
		} while (volunteerAvailable || dateNotOk || !isDateOfTypeVisit);
		
		return date;
	}

	/*@
		requires volunteerList != null;
	
		ensures !volunteerList.findVolunteer(nickname);
		ensures volunteerList.size() == \old(volunteerList.size()) - 1;
	@*/
	public Volunteer remove() {
		String nickname;
		boolean confirmInsert;
		Volunteer removedVolunteer = new Volunteer();
		
		if(volunteerList.isEmpty()) {
			volunteerView.showNoVolunteerAvailabile();
			return null;
		}
		
		nickname = askExistingNickname();
		confirmInsert = volunteerView.askConfirmRemoval();
			
		if(confirmInsert) {
			removedVolunteer = volunteerList.getVolunteer(nickname);
			volunteerList.removeVolunteer(removedVolunteer);
			volunteerView.showConfimRemoval();
			return removedVolunteer;
		}
		else {
			volunteerView.showRemovalCancelled();
			return null;
		}
	}
	
	private String askExistingNickname() {
	    String nickname;

	    do {
	        nickname = volunteerView.askNickname();
	        if (!volunteerList.findVolunteer(nickname))
	            volunteerView.showVolunteerNotFoundError();
	    } while (!volunteerList.findVolunteer(nickname));

	    return nickname;
	}
	
	public void removeAvailability(Volunteer volunteer, TimeSimulator time) {
		LocalDate baseDate = time.getDateForAvailability();
		Month modifyMonth = baseDate.getMonth();

		LocalDate date;
		boolean volunteerAvailable;

		do {
			do {
				int dateNumber = volunteerView.askDayOfMonth(modifyMonth, time);
				date = LocalDate.of(baseDate.getYear(), modifyMonth.getValue(), dateNumber);
				volunteerAvailable = volunteer.isAvailableOn(date);

				if (!volunteerAvailable)
					volunteerView.showNotFoundDataError();

			} while (!volunteerAvailable);

		} while (!volunteerView.askRemoveAvailability());

		this.volunteerList.removeAvailability(volunteer, date);
	}
	
	public VolunteerList getVolunteerList() {
		return volunteerList;
	}
	
	public ArrayList<Volunteer> getArrayListVolunteerLastInsert(){
		return this.lastVolunteersInsert;
	}
	
	public void addInModify() {
		if(volunteerView.askConfirmAddVolunteer()) //se si allora richiamo la classica aggiunta
			add();
	}
}
