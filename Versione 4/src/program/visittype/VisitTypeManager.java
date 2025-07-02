package program.visittype;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import program.places.Place;
import program.places.PlaceList;
import program.users.Volunteer;
import utilities.InputDati;
import utilities.MenuLayout;

public class VisitTypeManager {

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
	private static final String MSG_CONFERMA_MODIFICA = "Vuoi modificare i dati? (SI/NO)";
	private static final String MSG_INS_CONFERMATO = "Inserimento completato con successo!";
	private static final String MSG_CONFERMA_INS = "Vuoi inserire i dati? (SI/NO)";
	private static final String MSG_ERR_SOVRAPPOSIZIONE_ORARIO = "Errore...l'orario si sovrappone con altri tipi di visita";
	private static final String MSG_INS_ORA_INIZIO = "Inserisci l'ora di inizio: ";
	private static final String MSG_ERR_LUOGO_NON_PRESENTE = "Errore...luogo non presente!";
	private static final String MSG_INS_NOME_LUOGO = "Inserisci il nome del luogo da associare: ";
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

	private static final String FILE_LUOGHI = "json/luoghi.json";
	private static final String FILE_TIPO_VISITA = "json/tipoVisita.json";

	private VisitTypeList visitTypeList;
	private PlaceList placeList;

	public VisitTypeManager() {
		this.visitTypeList = new VisitTypeList(FILE_TIPO_VISITA);
		this.placeList= new PlaceList(FILE_LUOGHI);
	}

	/*@
		requires visitTypeList != null;
		requires placeList != null;
		requires volunteer != null && volunteer.size() > 0; 
 	
 		ensures visitTypeList.findVisit(title);
 		ensures visitTypeList.size() == \old(visitTypeList.size()) + 1;
 	@*/
	public void add(ArrayList<Volunteer> volunteer) {
		String title, confirmInsert, namePlace;
		ArrayList<DayOfWeek> day = new ArrayList<>();

		do {
			do {
				title = InputDati.leggiStringaNonVuota(MSG_INS_TITOLO_VISITA);

				if(visitTypeList.findVisitType(title))
					System.out.println(MSG_ERR_LUOGO_GIA_PRESENTE);

			}while(visitTypeList.findVisitType(title));

			String description = InputDati.leggiStringa(MSG_INS_DESCRIZIONE_VISITA);
			String meetingPoint = InputDati.leggiStringa(MSG_INS_PUNTO_INCONTRO_VISITA);

			String startDate = InputDati.leggiDataStringa(MSG_INS_DATA_INIZIO_VISITA);
			String endDate = InputDati.leggiDataStringa(MSG_INS_DATA_FINE_VISITA);

			do {
				day.add(InputDati.leggiGiornoSettimanaItaliano(MSG_INS_GIORNO_VISITA));
			}while(InputDati.yesNo(MSG_INS_ALTRO_GIORNO));

			int duration = InputDati.leggiInteroConMinimo(MSG_INS_DURATA_VISITA, 1);
			boolean buyTicket = InputDati.yesNo(MSG_INS_BIGLIETTO_VISITA);
			int minParticipant = InputDati.leggiInteroConMinimo(MSG_INS_MIN_PARTECIPANTI, 1);
			int maxParticipant = InputDati.leggiInteroConMinimo(MSG_INS_MAX_PARTECIPANTI, minParticipant);

			do {
				namePlace = InputDati.leggiStringaNonVuota(MSG_INS_NOME_LUOGO);

				if(placeList.getPlace(namePlace) == null)
					System.out.println(MSG_ERR_LUOGO_NON_PRESENTE);
			}while(placeList.getPlace(namePlace) == null);

			String startHour;
			do {
				startHour = InputDati.leggiOrarioStringa(MSG_INS_ORA_INIZIO);
				if(visitTypeList.hasSameTime(startHour, namePlace, LocalDate.parse(startDate), LocalDate.parse(endDate), day))
					System.out.println(MSG_ERR_SOVRAPPOSIZIONE_ORARIO);
			}while(visitTypeList.hasSameTime(startHour, namePlace, LocalDate.parse(startDate), LocalDate.parse(endDate), day));

			confirmInsert = InputDati.leggiStringa(MSG_CONFERMA_INS);
			if(confirmInsert.equalsIgnoreCase("SI")) {
				visitTypeList.addVisitType(new VisitType(title, description, meetingPoint, startDate, endDate,
						 day, startHour, duration, buyTicket, minParticipant, maxParticipant, placeList.getPlace(namePlace), volunteer));
				System.out.println(MSG_INS_CONFERMATO);
			}

		}while(confirmInsert.equalsIgnoreCase("NO"));
	}

	
	/*@
		requires visitTypeList != null;
		requires placeList != null;
		requires volunteer != null && volunteer.size() > 0; 
		requires place != null;
	
		ensures visitTypeList.findVisit(title);
		ensures visitTypeList.size() == \old(visitTypeList.size()) + 1;
	@*/
	//METODO USATO QUANDO AGGIUNGO UN LUOGO => DEVO AVERE UN TIPO VISITA ASSOCIATO
	public void add(Place place, ArrayList<Volunteer> volunteer) {
		String title, confirmInsert;
		ArrayList<DayOfWeek> day = new ArrayList<>();

		do {
			do {
				title = InputDati.leggiStringaNonVuota(MSG_INS_TITOLO_VISITA);

				if(visitTypeList.findVisitType(title))
					System.out.println(MSG_ERR_LUOGO_GIA_PRESENTE);

			}while(visitTypeList.findVisitType(title));

			String description = InputDati.leggiStringa(MSG_INS_DESCRIZIONE_VISITA);
			String meetingPoint = InputDati.leggiStringa(MSG_INS_PUNTO_INCONTRO_VISITA);

			String startDate = InputDati.leggiDataStringa(MSG_INS_DATA_INIZIO_VISITA);
			String endDate = InputDati.leggiDataStringa(MSG_INS_DATA_FINE_VISITA);

			do {
				day.add(InputDati.leggiGiornoSettimanaItaliano(MSG_INS_GIORNO_VISITA));
				if(day.size()==7)
					break;
			}while(InputDati.yesNo(MSG_INS_ALTRO_GIORNO));

			int duration = InputDati.leggiInteroConMinimo(MSG_INS_DURATA_VISITA, 1);
			boolean buyTicket = InputDati.yesNo(MSG_INS_BIGLIETTO_VISITA);
			int minParticipant = InputDati.leggiInteroConMinimo(MSG_INS_MIN_PARTECIPANTI, 1);
			int maxParticipant = InputDati.leggiInteroConMinimo(MSG_INS_MAX_PARTECIPANTI, minParticipant);

			String startHour;
			do {
				startHour = InputDati.leggiOrarioStringa(MSG_INS_ORA_INIZIO);
				if(visitTypeList.hasSameTime(startHour, place.getName(), LocalDate.parse(startDate), LocalDate.parse(endDate), day))
					System.out.println(MSG_ERR_SOVRAPPOSIZIONE_ORARIO);
			}while(visitTypeList.hasSameTime(startHour, place.getName(), LocalDate.parse(startDate), LocalDate.parse(endDate), day));

			confirmInsert = InputDati.leggiStringa(MSG_CONFERMA_INS);
			if(confirmInsert.equalsIgnoreCase("SI")) {
				visitTypeList.addVisitType(new VisitType(title, description, meetingPoint, startDate, endDate,
						 day, startHour, duration, buyTicket, minParticipant, maxParticipant, place, volunteer));
				System.out.println(MSG_INS_CONFERMATO);
			}

		}while(confirmInsert.equalsIgnoreCase("NO"));
	}

	public void view() {
		System.out.println(MSG_TITOLO_VISUALIZZA+this.visitTypeList.toString());
	}

	public void viewVisitTypeForVolunteer(Volunteer volunteer, VisitTypeList visitTypeList) {
		for(VisitType visitType : visitTypeList.getVisitTypeList().values()) {
			for(Volunteer vol : visitType.getVolunteer()) {
				if(vol.getNickname().equals(volunteer.getNickname()))
					System.out.println(visitType.toString());
			}
		}
	}

	/*@
		requires visitTypeList != null;
		requires placeList != null;
		requires volunteer != null && volunteer.size() > 0; 

		invariant visitTypeList.size() == \old(visitTypeList.size());
		
		assignable visitSearched.descprtion, visitSearched.meetingPoint, visitSearched.startDate, visitSearched.endDate,
				   visitSearched.day, visitSearched.startHour, visitSearched.duration, visitSearched.buyTicket, visitSearched.minParticipant,
				   visitSearched.maxParticipant, visitSearched.place;

		ensures visitTypeList.findVisit(title);
	@*/
	public void modify(ArrayList<Volunteer> volunteer) {
		String confirmInsert = "", namePlace;
		ArrayList<DayOfWeek> day = new ArrayList<>();
		VisitType visitSearched;
		do {
			//ricerco il tipo di visita
			visitSearched=readAndSearchVisitType();

			//definisco il menu per selezionare cosa voglio modificare
			MenuLayout menu= new MenuLayout(MSG_INS_VOCI_DA_MODIFICARE, VOCI_MENU_MODIFICA);

			//lettura campi che si vogliono modificare
			int opz= InputDati.leggiIntero(menu.visualizzaMenu(), 0, menu.getNumVoci()-1);

			//selezione operazione di modifica
			switch (opz) {
				case 1: { //MODIFICO LA DESCRIZIONE
					visitSearched.setDescription(InputDati.leggiStringa(MSG_INS_DESCRIZIONE_VISITA));
					break;
				}

				case 2: { //MODIFICO IL PUNTO DI INCONTRO
					visitSearched.setMeetingPoint(InputDati.leggiStringa(MSG_INS_PUNTO_INCONTRO_VISITA));
					break;
				}

				case 3: { //MODIFICO LA DATA DI INZIO
					visitSearched.setStartDate(InputDati.leggiDataStringa(MSG_INS_DATA_INIZIO_VISITA));
					break;
				}

				case 4: { //MODIFICO LA DATA DI FINE
					visitSearched.setEndDate(InputDati.leggiDataStringa(MSG_INS_DATA_FINE_VISITA));
					break;
				}

				case 5: { //MODIFICO LA DURATA
					visitSearched.setDuration(InputDati.leggiInteroConMinimo(MSG_INS_DURATA_VISITA, 1));
					break;
				}

				case 6: { //MODIFICO IL LUOGO
					do {
						namePlace = InputDati.leggiStringaNonVuota(MSG_INS_NOME_LUOGO);

						if(placeList.getPlace(namePlace) == null)
							System.out.println(MSG_ERR_LUOGO_NON_PRESENTE);
					}while(placeList.getPlace(namePlace) == null);
					break;
				}

				case 7:{ //MODIFICO I GIORNI DELLA SETTIMANA
					day.add(InputDati.leggiGiornoSettimanaItaliano(MSG_INS_GIORNO_VISITA));
					break;
				}

				case 8:{ //MODIFICO BUY TICKET
					visitSearched.setBuyTicket(InputDati.yesNo(MSG_INS_BIGLIETTO_VISITA));
					break;
				}

				case 9:{ //MODIFICO N MIN PARTECIPANTE
					visitSearched.setMinParticipant(InputDati.leggiInteroConMinimo(MSG_INS_MIN_PARTECIPANTI, 1));
					break;
				}

				case 10:{ //MODIFICO N MAX PARTECIPANTE
					visitSearched.setMaxParticipant(InputDati.leggiInteroConMinimo(MSG_INS_MAX_PARTECIPANTI, visitSearched.getMinParticipant()));
					break;
				}

				case 11:{ //MODIFICA ORA DI INZIO
					String startHour, startDate=visitSearched.getStartDate(), endDate=visitSearched.getEndDate();
					namePlace=visitSearched.getPlace().getName();

					do {
						startHour = InputDati.leggiOrarioStringa(MSG_INS_ORA_INIZIO);
						if(visitTypeList.hasSameTime(startHour, namePlace, LocalDate.parse(startDate), LocalDate.parse(endDate), day))
							System.out.println(MSG_ERR_SOVRAPPOSIZIONE_ORARIO);
					}while(visitTypeList.hasSameTime(startHour, namePlace, LocalDate.parse(startDate), LocalDate.parse(endDate), day));
					break;
				}

				default: //ANNULLA MODIFICA
					confirmInsert="NOT";
					break;
			}

			if(!confirmInsert.equals("NOT")) {
				confirmInsert = InputDati.leggiStringa(MSG_CONFERMA_MODIFICA);

				if(confirmInsert.equalsIgnoreCase("SI")) {
					visitTypeList.modifyVisitType(visitSearched);
					System.out.println(MSG_MODIFICA_CONFERMATA);
				}
			}else
				System.out.println(MSG_MODIFICA_ANNULLATA);
		}while(confirmInsert.equalsIgnoreCase("NO"));
	}

	/*@
		requires visitTypeList != null;
		requires placeList != null;

		ensures !visitTypeList.findVisit(title);
		ensures visitTypeList.size() == \old(visitTypeList.size()) - 1;
	@*/
	//metodo per la rimozione del tipo di visita
	public VisitType remove() {
		String title;
		boolean confirmInsert;
		VisitType removedVisitType = new VisitType();

		if(visitTypeList.isEmpty()) {
			System.out.println(MSG_ERR_NO_TIPI_VISITA_DISPONIBILI);
			return null;
		}

		do {
			do {
				title = InputDati.leggiStringaNonVuota(MSG_INS_TITOLO_VISITA);
				if(!visitTypeList.findVisitType(title))
					System.out.println(MSG_ERR_TIPO_VISITA_NON_PRESENTE);
			}while(!visitTypeList.findVisitType(title));

			confirmInsert = InputDati.yesNo(MSG_CONFERMA_RIMOZIONE);
			if(confirmInsert) {
				removedVisitType = visitTypeList.getVisitType(title);
				visitTypeList.removeVisitType(removedVisitType);
				System.out.println(MSG_RIMOZIONE_CONFERMATA);
			}
			else
				System.out.println(MSG_RIMOZIONE_ANNULLATA);

		}while(!confirmInsert);

		return removedVisitType;
	}

	//metodo ausiliario per la lettura e la ricerca di un determinato visitType
	private VisitType readAndSearchVisitType() {
		String title;
		VisitType visitTypeSearched;

		do {
			title = InputDati.leggiStringaNonVuota(MSG_INS_TITOLO_VISITA);
			visitTypeSearched=visitTypeList.searchVisitType(title);

			if(visitTypeSearched==null)
				System.out.println(MSG_ERR_TIPO_VISITA_NON_PRESENTE);

		}while(visitTypeSearched==null);

		return visitTypeSearched;
	}

	//metodo che ritorna la lista dei tipi visita
	public VisitTypeList getVisitTypeList() {
		return visitTypeList;
	}

	//metodo che ricerca un determinato tipo di visita
	public void searchVisitType() {
		String title = InputDati.leggiStringaNonVuota(MSG_INS_TITOLO_VISITA);
		VisitType visitTypeSearched=visitTypeList.searchVisitType(title);

		if(visitTypeSearched==null)
			System.out.println(MSG_ERR_TIPO_VISITA_NON_PRESENTE);
		else
			System.out.println(visitTypeSearched.toString());
	}


	//metodo per l'aggiunta di un volontario ad una classe esistente
	public void addVolunteer(ArrayList<Volunteer> volunteer) {
		do {
			//leggo il tipo visita
			VisitType visitTypeSearched= readAndSearchVisitType();
			//aggiungo i volontari al tipo visita passato
			visitTypeList.addVolunteer(visitTypeSearched,volunteer);
			//modifico il file per garantire la coerenza
			visitTypeList.modifyVisitType(visitTypeSearched);
		}while(!InputDati.yesNo(MSG_CONFERMA_ASSOCIAZIONE));

		System.out.println(MSG_ASSOCIAZIONE_CONFERMATA);
	}
}