package program.visittype;

import java.time.DayOfWeek;

import java.util.ArrayList;
import java.util.Arrays;

import program.users.volunteer.Volunteer;
import utilities.InputDati;
import utilities.MenuLayout;

public class ConsoleVisitTypeView implements VisitTypeView{
	
	private static final String MSG_ERR_NO_VISITTYPE = "Nessun tipo visita presente...";
	private static final String MSG_OPERAZIONE_EFFETTUATA = "Operazione Effettuata!!!";
	private static final String MSG_ASSOCIAZIONE_CONFERMATA = "Aggiunta dei volontari effettuata correttamente!";
	private static final String MSG_CONFERMA_ASSOCIAZIONE = "Vuoi associare i volontari al Tipo Visita? ";
	private static final String MSG_RIMOZIONE_ANNULLATA = "Rimozione annullata!";
	private static final String MSG_RIMOZIONE_CONFERMATA = "Rimozione completata con successo!";
	private static final String MSG_CONFERMA_RIMOZIONE = "Vuoi rimuovere il tipo visita?";
	private static final String MSG_ERR_NO_TIPI_VISITA_DISPONIBILI = "Attenzione: non ci sono tipi visita disponibili!";
	private static final String MSG_TITOLO_VISUALIZZA = " \n -*-* VISUALIZZAZIONE DEL TIPO DI VISITE *-*- \n \n";
	private static final String MSG_ERR_TIPO_VISITA_NON_PRESENTE = "Errore...tipo visita non presente!";
	private static final String MSG_MODIFICA_ANNULLATA = "Modifica annullata!";
	private static final String MSG_MODIFICA_CONFERMATA = "Modifica completata con successo!";
	private static final String MSG_CONFERMA_MODIFICA = "Vuoi modificare i dati? ";
	private static final String MSG_INS_CONFERMATO = "Inserimento completato con successo!";
	private static final String MSG_CONFERMA_INS = "Vuoi inserire i dati? ";
	private static final String MSG_ERR_SOVRAPPOSIZIONE_ORARIO = "Errore...l'orario si sovrappone con altri tipi di visita";
	private static final String MSG_INS_ORA_INIZIO = "Inserisci l'ora di inizio: ";
	private static final String MSG_INS_MAX_PARTECIPANTI = "Inserisci il numero massimo di partecipanti: ";
	private static final String MSG_INS_MIN_PARTECIPANTI = "Inserisci il numero minimo di partecipanti: ";
	private static final String MSG_INS_BIGLIETTO_VISITA = "Indicare se il biglietto è necessario: ";
	private static final String MSG_INS_DURATA_VISITA = "Inserisci la durata in minuti: ";
	private static final String MSG_INS_ALTRO_GIORNO = "Vuoi inserire un altro giorno?";
	private static final String MSG_INS_GIORNO_VISITA = "Inserisci il giorno del tipo visita: ";
	private static final String MSG_INS_DATA_FINE_VISITA = "Inserisci la data di fine (YYYY-MM-GG): ";
	private static final String MSG_INS_DATA_INIZIO_VISITA = "Inserisci la data di inizio (YYYY-MM-GG): ";
	private static final String MSG_INS_PUNTO_INCONTRO_VISITA = "Inserisci il punto di incontro: ";
	private static final String MSG_INS_DESCRIZIONE_VISITA = "Inserisci la descrizione del tipo visita: ";
	private static final String MSG_ERR_LUOGO_GIA_PRESENTE = "Errore...luogo già presente!";
	private static final String MSG_INS_TITOLO_VISITA = "Inserisci il titolo del tipo visita: ";
	private static final String MSG_INS_VOCI_DA_MODIFICARE="Inserisci quale delle seguenti voci vuoi modificare: ";
	private static final ArrayList<String> VOCI_MENU_MODIFICA = new ArrayList<>(Arrays.asList(
		    "Descrizione",
		    "Punto di incontro",
		    "Data di Inizio",
		    "Data di Fine",
		    "Durata",
		    "Giorni della settimana",
		    "Luogo",
		    "Necessita' di acquisto di un ticket",
		    "Numero minimo di partecipanti",
		    "Numero massimo di partecipanti",
		    "Ora di inizio",
		    "Annulla"
		));

	public String askTitle() {
		return InputDati.leggiStringaNonVuota(MSG_INS_TITOLO_VISITA);
	}
	
	public String askDescription() {
		return InputDati.leggiStringa(MSG_INS_DESCRIZIONE_VISITA);
	}
	
	public String askMeetingPoint() {
		return InputDati.leggiStringa(MSG_INS_PUNTO_INCONTRO_VISITA);
	}
	
	public String askStartDate() {
		return InputDati.leggiDataStringa(MSG_INS_DATA_INIZIO_VISITA);
	}
	
	public String askEndDate() {
		return InputDati.leggiDataStringa(MSG_INS_DATA_FINE_VISITA);
	}
	
	public DayOfWeek askDay() {
		return InputDati.leggiGiornoSettimanaItaliano(MSG_INS_GIORNO_VISITA);
	}
	
	public int askDuration() {
		return InputDati.leggiInteroConMinimo(MSG_INS_DURATA_VISITA, 1);
	}
	
	public boolean askNeedBuyTicket() {
		return InputDati.yesNo(MSG_INS_BIGLIETTO_VISITA);
	}
	
	public int askMinParticipant() {
		return InputDati.leggiInteroConMinimo(MSG_INS_MIN_PARTECIPANTI, 1);
	}
	
	public int askMaxParticipant(int minParticipant) {
		return InputDati.leggiInteroConMinimo(MSG_INS_MAX_PARTECIPANTI, minParticipant);
	}
	
	public String askStartHour() {
		return InputDati.leggiOrarioStringa(MSG_INS_ORA_INIZIO);
	}
	
	public int askMenuVoice() {
		MenuLayout menu= new MenuLayout(MSG_INS_VOCI_DA_MODIFICARE, VOCI_MENU_MODIFICA);

		//lettura campi che si vogliono modificare
		return InputDati.leggiIntero(menu.visualizzaMenu(), 0, menu.getNumVoci()-1);
	}
	
	public void showTitleVisitView() {
		System.out.print(MSG_TITOLO_VISUALIZZA);
	}
	
	public void showVisitType(VisitType visitType) {
		System.out.print("  Titolo: " + visitType.getTitle() + "\n  Descrizione: " + visitType.getDescription() + "\n  Punto d'incontro: " + visitType.getMeetingPoint()
					+ "\n  Data d'inizio: " + visitType.getStartDate() + "  Data di fine: " + visitType.getEndDate() + "  Giorno: " + visitType.getDay() + "  Ora d'inizio: " + visitType.getStartHour()
					+ "  Durata: " + visitType.getDuration() + "\n  Necessario comprare il biglietto? " + (visitType.isBuyTicket() ? "Si" : "No") + "  Partecipanti minimi: "
					+ visitType.getMinParticipant() + " Partecipanti massimi: " + visitType.getMaxParticipant() + "\n  Luogo associato: " + visitType.getPlace().getName()
					+ "\n  Volontario associato: " + viewNickNameVolunteers(visitType.getVolunteer())+ "\n\n"); 
		
	}
	
	private String viewNickNameVolunteers(ArrayList<Volunteer> volunteers) {
		StringBuffer str= new StringBuffer("[");
		for(Volunteer volunteer: volunteers)
			str.append(volunteer.getNickname()+" - ");
		String strFinal= (str.subSequence(0, str.length()-3)).toString();
		
		return strFinal +"]";
	}

	public void showAlreadyInserted() {
		System.out.println(MSG_ERR_LUOGO_GIA_PRESENTE);
	}
	
	public void showTimeOverlapError() {
		System.out.println(MSG_ERR_SOVRAPPOSIZIONE_ORARIO);
	}
	
	public void showInsertSuccess() {
		System.out.println(MSG_INS_CONFERMATO);
	}
	
	public void showModifySuccess() {
		System.out.println(MSG_MODIFICA_CONFERMATA);
	}
	
	public void showCancelModify() {
		System.out.println(MSG_MODIFICA_ANNULLATA);
	}
	
	public void showAssociationSuccess() {
		System.out.println(MSG_ASSOCIAZIONE_CONFERMATA);
	}
	
	public void showVisitTypeNotFoundError() {
		System.out.println(MSG_ERR_TIPO_VISITA_NON_PRESENTE);
	}
	
	public void showVisitTypeNoAvailable() {
		System.out.println(MSG_ERR_NO_TIPI_VISITA_DISPONIBILI);
	}
	
	public void showRemoveSuccess() {
		System.out.println(MSG_RIMOZIONE_CONFERMATA);
	}
	
	public void showCancelRemove() {
		System.out.println(MSG_RIMOZIONE_ANNULLATA);
	}
	
	public void showErrorNoVisitType() {
		System.out.println(MSG_ERR_NO_VISITTYPE);
	}
	
	public boolean confirmInsertAnotherDay() {
		return InputDati.yesNo(MSG_INS_ALTRO_GIORNO);
	}
	
	public boolean confirmInsert() {
		return InputDati.yesNo(MSG_CONFERMA_INS);
	}
	
	public void confirmInsertMessage() {
		System.out.println(MSG_OPERAZIONE_EFFETTUATA);
	}
	
	public boolean confirmModify() {
		return InputDati.yesNo(MSG_CONFERMA_MODIFICA);
	}
	
	public boolean confirmAssociation() {
		return InputDati.yesNo(MSG_CONFERMA_ASSOCIAZIONE);
	}
	
	public boolean confirmRemove() {
		return InputDati.yesNo(MSG_CONFERMA_RIMOZIONE);
	}

}
