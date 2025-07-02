package program.users;

import utilities.InputDati;
import utilities.JsonManager;
import utilities.TimeSimulator;

public class OpenCloseAvailabilityManager {

	private static final String MSG_ERR_DISP_NON_APRIBILE = "Attenzione: questa operazione è disponibile soltanto il giorno 16 del mese";
	private static final String MSG_APERTURA_ANNULATA = "Apertura annulata!";
	private static final String MSG_DISP_APERTA = "Apertura della raccolta delle disponibilità dei volontari";
	private static final String MSG_APRIRE_DISP = "Vuoi aprire la raccolta delle disponibilità?";
	private static final String FILE_AVAIL = "json/statoDisponibilita.json";

	private static boolean isAvailabilityOpen;
	private JsonManager jsonManager;

	public OpenCloseAvailabilityManager() {
		this.jsonManager = new JsonManager(FILE_AVAIL);
		loadData();
	}

	private void loadData() {
		setAvailabilityOpen(Boolean.parseBoolean(this.jsonManager.loadFirstElement("availabilityStatus")));
	}

	private void setAvailabilityOpen(boolean isAvailabilityOpen) {
		OpenCloseAvailabilityManager.isAvailabilityOpen = isAvailabilityOpen;
	}

	public static boolean getAvailabilityOpen() {
		return OpenCloseAvailabilityManager.isAvailabilityOpen;
	}

	public void closeAvaialbility() {	//chiusura delle raccolta delle disponibilità
		setAvailabilityOpen(false);
		this.jsonManager.modifyObject("availabilityStatus", "true", String.valueOf(isAvailabilityOpen));
	}

	public void openAvailability(TimeSimulator time) {	//apertura della raccolta delle disponibilità
		if(time.getNumberOfDay() == 16) {
			boolean confirmInsert;

			confirmInsert = InputDati.yesNo(MSG_APRIRE_DISP);
			if(confirmInsert) {
				setAvailabilityOpen(true);
				this.jsonManager.modifyObject("availabilityStatus", "false", String.valueOf(isAvailabilityOpen));
				System.out.println(MSG_DISP_APERTA);
			}
			else {
				System.out.println(MSG_APERTURA_ANNULATA);
				return;
			}

		}
		else
			System.out.println(MSG_ERR_DISP_NON_APRIBILE);

	}
}
