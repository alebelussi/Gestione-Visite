package program.places;

import java.util.ArrayList;
import java.util.Arrays;

import utilities.InputDati;
import utilities.MenuLayout;

public class ConsolePlaceView implements PlaceView{
	
	private static final String MSG_NESSUN_LUOGO_PRESENTE = "Nessun luogo presente...";
	private static final String MSG_RIMOZIONE_ANNULLATA = "Rimozione annullata!";
	private static final String MSG_RIMOZIONE_COMPLETATA = "Rimozione completata con successo!";
	private static final String MSG_CONFERMA_RIMOZIONE = "Vuoi rimuovere il luogo? ";
	private static final String MSG_ERR_NO_LUOGHI_DISPONIBILI = "Attenzione: non ci sono luoghi disponibili!";
	private static final String MSG_CONFERMA_AMBITO = "Vuoi inserire l'ambito territoriale? ";
	private static final String MSG_INSERIMENTO_AMBITO_TERRITORIALE = "Inserisci l'ambito territoriale: ";
	private static final String MSG_MODIFICA_ANNULLATA = "Modifica annullata!";
	private static final String MSG_MODIFICA_CONFERMATA = "Modifica completata con successo!";
	private static final String MSG_CONFERMA_MODIFICA = "Vuoi modificare i dati? ";
	private static final String MSG_INSERIMENTO_CONFERMATO = "Inserimento completato con successo!";
	private static final String MSG_INSERIMENTO_NDIRIZZO_LUOGO = "Inserisci l'indirizzo del luogo: ";
	private static final String MSG_INSERIMENTO_DESCRIZIONE_LUOGO = "Inserisci la descrizione del luogo: ";
	private static final String MSG_ERRORE_LUOGO_NON_PRESENTE = "Errore...luogo non presente!";
	private static final String MSG_ERRORE_NOME_NON_UNIVOCO = "Errore...luogo già presente!";
	private static final String MSG_CONFERMA_INSERIMENTO = "Vuoi inserire i dati? ";
	private static final String MSG_INSERIMENTO_LUOGO = "Inserisci il nome del luogo: ";
	private static final String MSG_INS_VOCI_DA_MODIFICARE="Inserisci quale delle seguenti voci vuoi modificare: ";
	private static final ArrayList<String> VOCI_MENU_MODIFICA = new ArrayList<>(Arrays.asList(
		    "Descrizione",
		    "Indirizzo",
		    "Annulla"
		));
	
	public String askPlaceName() {
		return InputDati.leggiStringaNonVuota(MSG_INSERIMENTO_LUOGO);
	}
	
	public String askPlaceDescription() {
		return InputDati.leggiStringa(MSG_INSERIMENTO_DESCRIZIONE_LUOGO);
	}
	
	public String askPlaceAddress() {
		return InputDati.leggiStringa(MSG_INSERIMENTO_NDIRIZZO_LUOGO);
	}
	
	public String askRegion() {
		return InputDati.leggiStringaNonVuota(MSG_INSERIMENTO_AMBITO_TERRITORIALE);
	}
	
	public int askMenuVoice() {
		MenuLayout menu= new MenuLayout(MSG_INS_VOCI_DA_MODIFICARE, VOCI_MENU_MODIFICA);

		//lettura campi che si vogliono modificare
		return InputDati.leggiIntero(menu.visualizzaMenu(), 0, menu.getNumVoci()-1);
	}
	
	public void showPlace(Place place) {
		System.out.println("  Nome: " + place.getName() + "\n  Descrizione: "+ place.getDescription() +"\n  Indirizzo: " + place.getAddress()
				+"\n  Ambito Territoriale: " + Place.getRegion() + "\n");
	}
	
	public void showAlreadyInsertedError() {
		System.out.println(MSG_ERRORE_NOME_NON_UNIVOCO);
	}
	
	public void showPlaceNotFoundError() {
		System.out.println(MSG_ERRORE_LUOGO_NON_PRESENTE);
	}
	
	public void showInsertSuccess() {
		System.out.println(MSG_INSERIMENTO_CONFERMATO);
	}
	
	public void showModifySuccess() {
		System.out.println(MSG_MODIFICA_CONFERMATA);
	}
	
	public void showCancelModify() {
		System.out.println(MSG_MODIFICA_ANNULLATA);
	}
	
	public void showRemoveSuccess() {
		System.out.println(MSG_RIMOZIONE_COMPLETATA);
	}
	
	public void showCancelRemove() {
		System.out.println(MSG_RIMOZIONE_ANNULLATA);
	}
	
	public void showNoPlaceAvailable() {
		System.out.println(MSG_ERR_NO_LUOGHI_DISPONIBILI);
	}
	
	public void showNoPlace() {
		System.out.println(MSG_NESSUN_LUOGO_PRESENTE);
	}

	public boolean confirmInsert() {
		return InputDati.yesNo(MSG_CONFERMA_INSERIMENTO);
	}
	
	public boolean confirmInsertRegion() {
		return InputDati.yesNo(MSG_CONFERMA_AMBITO);
	}
	
	public String confirmModify() {
		return InputDati.leggiStringa(MSG_CONFERMA_MODIFICA);
	}
	
	public boolean confirmRemove() {
		return InputDati.yesNo(MSG_CONFERMA_RIMOZIONE);
	}

}
