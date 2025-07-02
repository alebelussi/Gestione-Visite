package program.users;

import java.util.ArrayList;

import program.SystemService;
import utilities.InputDati;

public class VolunteerManager {
	
	private static final String MSG_ERR_NICK_USER = "Attenzione: è stato inserito un nickname uguale allo username di un configuratore...";
	private static final String MSG_CONFERMA_INS_NUOVO_VOLONTARIO = "Nella modifica del tipo di visita vuoi inserire un nuovo volontario?";
	private static final String MSG_CONTINUA_INS = "Vuoi associare un altro volontario?";
	private static final String MSG_ERR_NESSUN_VOLONTARIO_INS = "Attenzione: nessun volontario inserito!";
	private static final String MSG_CONFERMA_INSERIMENTO = "Vuoi inserire il volontario? (SI/NO) ";
	private static final String MSG_VOLONTARIO_NON_PRESENTE = "Il nickname inserito non corrisponde ad alcun volontario presente!";
	private static final String MSG_INS_NICKNAME = "Inserisci un nickname al volontario: ";
	private static final String MSG_INS_COGNOME_VOLONTARIO = "Inserisci il cognome del volontario: ";
	private static final String MSG_INS_NOME_VOLONTARIO = "Inserisci il nome del volontario: ";
	private static final String FILE_JSON_VOLONTARIO = "json/volontario.json";
	
	private VolunteerList volunteerList; 
	private ArrayList<Volunteer> lastVolunteersInsert;
	
	public VolunteerManager() {
		this.volunteerList = new VolunteerList(FILE_JSON_VOLONTARIO);
		this.lastVolunteersInsert = new ArrayList<>();
	}
	
	/*@
		requires volunteerList != null; 
		
	 	ensures volunteerList.findVolunteer(nickname);
	 	ensures volunteerList.size() == \old(volunteerList.size()) + 1;
	 @*/
	public void add() {
		String nickname;
		Volunteer volunteer;
		//pulisco la vecchia lista di volontari inseriti
		this.lastVolunteersInsert.clear();
		
		do {
			
			do {
				nickname = InputDati.leggiStringaNonVuota(MSG_INS_NICKNAME);
				final String finalNickname= nickname;
				
				//controllo che il nickname non coincida con nessun username
				if(SystemService.existConfigurator(finalNickname))
					System.out.println(MSG_ERR_NICK_USER);
				
				else {	
					
					if(!volunteerList.findVolunteer(nickname) && !lastVolunteersInsert.stream().anyMatch(el -> finalNickname.equals(el.getNickname()))) {
						System.out.println(MSG_VOLONTARIO_NON_PRESENTE);
						
						if(InputDati.yesNo(MSG_CONFERMA_INSERIMENTO)) {
							//inserisco i dati del nuovo volontario volontario
							String name, surname;
							name = InputDati.leggiStringaNonVuota(MSG_INS_NOME_VOLONTARIO);
							surname = InputDati.leggiStringaNonVuota(MSG_INS_COGNOME_VOLONTARIO);
							volunteer= new Volunteer(name, surname, nickname, "volunteer", true);
							volunteerList.addVolunteer(volunteer);
							lastVolunteersInsert.add(volunteer);
						}
					}else{
						volunteer = volunteerList.getVolunteer(nickname);
						lastVolunteersInsert.add(volunteer);				
					}
					if(lastVolunteersInsert.size() == 0)
						System.out.println(MSG_ERR_NESSUN_VOLONTARIO_INS);
				}
			}while(lastVolunteersInsert.size() == 0);
					
		}while(InputDati.yesNo(MSG_CONTINUA_INS)); 
	}
	
	public void view() {
		volunteerList.viewVolunteer();
	}
	
	public void searchVolunteer() {
		String nickname;
		Volunteer volunteerSearched;
		nickname = InputDati.leggiStringaNonVuota(MSG_INS_NICKNAME);
		volunteerSearched= volunteerList.getVolunteer(nickname);
		if(volunteerSearched==null)
			System.out.println(MSG_VOLONTARIO_NON_PRESENTE);
		else
			System.out.println(volunteerSearched.toString());
	}
	
	public VolunteerList getVolunteerList() {
		return volunteerList;
	}
	
	public ArrayList<Volunteer> getArrayListVolunteerLastInsert(){
		return this.lastVolunteersInsert;
	}
	
	public void addInModify() {
		if(InputDati.yesNo(MSG_CONFERMA_INS_NUOVO_VOLONTARIO)) //se si allora richiamo la classica aggiunta
			add();
	}
}
