package program.users.availability;

import utilities.RepositorySystem;
import utilities.TimeSimulator;

public class OpenCloseAvailabilityManager {
	
	
	
	private static boolean isAvailabilityOpen;
	private RepositorySystem repositoryAvailability;
	private OpenCloseAvailabilityView view;
	
	public OpenCloseAvailabilityManager(OpenCloseAvailabilityView view, RepositorySystem repositoryAvailability) {
		this.repositoryAvailability = repositoryAvailability;
		this.view = view;
		loadData();
	}
	
	private void loadData() {
		setAvailabilityOpen(Boolean.parseBoolean(this.repositoryAvailability.loadFirstElement("availabilityStatus")));
	}
	
	private void setAvailabilityOpen(boolean isAvailabilityOpen) {
		OpenCloseAvailabilityManager.isAvailabilityOpen = isAvailabilityOpen;
	}
	
	public static boolean getAvailabilityOpen() {
		return OpenCloseAvailabilityManager.isAvailabilityOpen;
	}
	
	public void closeAvaialbility() {	//chiusura delle raccolta delle disponibilità
		setAvailabilityOpen(false);
		this.repositoryAvailability.modifyObject("availabilityStatus", "true", String.valueOf(isAvailabilityOpen));
	}
	
	
	public void openAvailability(TimeSimulator time) {	//apertura della raccolta delle disponibilità
		if(time.getNumberOfDay() == 16) {
			boolean confirmInsert;
			confirmInsert = view.askAvaibilityOpen();
			if(confirmInsert) {
				setAvailabilityOpen(true);
				this.repositoryAvailability.modifyObject("availabilityStatus", "false", String.valueOf(isAvailabilityOpen));
				view.showAvailabilityOpen();
			}
			else {
				view.showAvailabilityOpeningCancelled();
				return;
			}
		}
		else
			view.showAvailabilityOpeningImpossible();
	}
}
