package program.users.availability;

import utilities.InputDati;
public class ConsoleOpenCloseAvailabilityView implements OpenCloseAvailabilityView{
	
	private static final String MSG_ERR_DISP_NON_APRIBILE = "Attenzione: questa operazione è disponibile soltanto il giorno 16 del mese";
	private static final String MSG_APERTURA_ANNULATA = "Apertura annulata!";
	private static final String MSG_DISP_APERTA = "Apertura della raccolta delle disponibilità dei volontari";
	private static final String MSG_APRIRE_DISP = "Vuoi aprire la raccolta delle disponibilità?";
	
	//INPUT
	
	public boolean askAvaibilityOpen() {
		return InputDati.yesNo(MSG_APRIRE_DISP);
	}
	
	//OUTPUT
	
	public void showAvailabilityOpen() {
		System.out.println(MSG_DISP_APERTA);
	}
	
	public void showAvailabilityOpeningCancelled() {
		System.out.println(MSG_APERTURA_ANNULATA);
	}
	
	public void showAvailabilityOpeningImpossible() {
		System.out.println(MSG_ERR_DISP_NON_APRIBILE);
	}
}
