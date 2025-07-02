package program.visittype;

import java.time.DayOfWeek;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import program.places.PlaceView;
import program.places.Place;
import program.places.PlaceList;
import program.users.volunteer.Volunteer;
import utilities.RepositorySystem;

public class VisitTypeManager {
	
	private VisitTypeList visitTypeList;
	private VisitTypeView visitTypeView;
	private PlaceList placeList;
	private PlaceView placeView; 

	public VisitTypeManager(VisitTypeView visitTypeView, PlaceView placeView, RepositorySystem repositoryVisitTypeSystem,RepositorySystem repositoryPlaceSystem) {
		this.visitTypeList = new VisitTypeList(repositoryVisitTypeSystem);
		this.placeList= new PlaceList(repositoryPlaceSystem);
		this.visitTypeView = visitTypeView;
		this.placeView = placeView;
	}
	
	public VisitTypeManager(VisitTypeView visitTypeView, PlaceView placeView, VisitTypeList visitTypeList, RepositorySystem repositoryPlaceSystem) {
		this.visitTypeList = visitTypeList;
		this.placeList= new PlaceList(repositoryPlaceSystem);
		this.visitTypeView = visitTypeView;
		this.placeView = placeView;
	}
	
	/*@
	requires visitTypeList != null;
	requires placeList != null;
	requires volunteer != null && volunteer.size() > 0; 
	
		ensures visitTypeList.findVisit(title);
		ensures visitTypeList.size() == \old(visitTypeList.size()) + 1;
	@*/
	//leggo il luogo, poi applico la logica di aggiunta
	public void add(ArrayList<Volunteer> volunteer) {
		String namePlace;
		Place place;

		do {
			namePlace = placeView.askPlaceName();

			if (!placeList.exists(namePlace))
				placeView.showPlaceNotFoundError();

			place = placeList.getPlace(namePlace);
		} while (place == null);
	
		createVisitType(place, volunteer);
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
		createVisitType(place, volunteer);
	}
	
	//logica di aggiunta del volontario
	private void createVisitType(Place place, ArrayList<Volunteer> volunteer) {
		String title;
		boolean confirmInsert;
		ArrayList<DayOfWeek> days;

		do {
			title = readTitleVisitType();

			String description = visitTypeView.askDescription();
			String meetingPoint = visitTypeView.askMeetingPoint();
			String startDate = visitTypeView.askStartDate();
			String endDate = visitTypeView.askEndDate();

			days = readDaysVisitType();

			int duration = visitTypeView.askDuration();
			boolean buyTicket = visitTypeView.askNeedBuyTicket();
			int minParticipant = visitTypeView.askMinParticipant();
			int maxParticipant = visitTypeView.askMaxParticipant(minParticipant);

			String startHour = readStartHour(place, days, startDate, endDate);

			confirmInsert = visitTypeView.confirmInsert();
			if (confirmInsert) {
				visitTypeList.addVisitType(new VisitType(title, description, meetingPoint, startDate, endDate,
						days, startHour, duration, buyTicket, minParticipant, maxParticipant, place, volunteer));
				visitTypeView.showInsertSuccess();
			}
		} while (!confirmInsert);
	}

	//metodo di lettura dell'ora di inizio della visita
	private String readStartHour(Place place, ArrayList<DayOfWeek> days, String startDate, String endDate) {
		String startHour;
		do {
			startHour = visitTypeView.askStartHour();
			if (visitTypeList.hasSameTime(startHour, place.getName(), LocalDate.parse(startDate), LocalDate.parse(endDate), days))
				visitTypeView.showTimeOverlapError();
		} while (visitTypeList.hasSameTime(startHour, place.getName(), LocalDate.parse(startDate), LocalDate.parse(endDate), days));
		return startHour;
	}

	//metodo di lettura dei giorni della visita
	private ArrayList<DayOfWeek> readDaysVisitType() {
		ArrayList<DayOfWeek> days;
		days = new ArrayList<>();
		do {
			days.add(visitTypeView.askDay());
			if (days.size() == 7) break;
		} while (visitTypeView.confirmInsertAnotherDay());
		return days;
	}

	//metodo di lettura del tipo visita
	private String readTitleVisitType() {
		String title;
		do {
			title = visitTypeView.askTitle();

			if (visitTypeList.findVisitType(title))
				visitTypeView.showAlreadyInserted();

		} while (visitTypeList.findVisitType(title));
		return title;
	}
	
	public void view() {
		if(visitTypeList.isEmpty())
			visitTypeView.showErrorNoVisitType();
		else {
			visitTypeView.showTitleVisitView();
			for(VisitType visitType: visitTypeList.getAllVisitTypes())
				visitTypeView.showVisitType(visitType);
		}
	}

	public void viewVisitTypeForVolunteer(Volunteer volunteer, VisitTypeList visitTypeList) {
		for(VisitType visitType : visitTypeList.getAllVisitTypes()) {
			if(visitType.hasVolunteer(volunteer))
				visitTypeView.showVisitType(visitType);
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
	private Map<Integer, Runnable> modifyActions = new HashMap <>(); 
	
	private void initModifyActions(VisitType visitSearched, ArrayList<DayOfWeek> day) {
		modifyActions.put(1, () -> visitSearched.setDescription(visitTypeView.askDescription()));
		modifyActions.put(2, () -> visitSearched.setMeetingPoint(visitTypeView.askMeetingPoint()));
		modifyActions.put(3, () -> visitSearched.setStartDate(visitTypeView.askStartDate()));
		modifyActions.put(4, () -> visitSearched.setEndDate(visitTypeView.askEndDate()));
		modifyActions.put(5, () -> visitSearched.setDuration(visitTypeView.askDuration()));

		modifyActions.put(6, () -> {
			String namePlace;
			do {
				namePlace = placeView.askPlaceName();
				if (!placeList.exists(namePlace))
					placeView.showPlaceNotFoundError();
			} while (!placeList.exists(namePlace));

			visitSearched.setPlace(placeList.getPlace(namePlace)); // AGGIUNTO: aggiorno effettivamente il luogo
		});

		modifyActions.put(7, () -> day.add(visitTypeView.askDay()));
		modifyActions.put(8, () -> visitSearched.setBuyTicket(visitTypeView.askNeedBuyTicket()));
		modifyActions.put(9, () -> visitSearched.setMinParticipant(visitTypeView.askMinParticipant()));
		modifyActions.put(10, () -> visitSearched.setMaxParticipant(
			visitTypeView.askMaxParticipant(visitSearched.getMinParticipant())
		));

		modifyActions.put(11, () -> {
			String startHour, startDate = visitSearched.getStartDate();
			String endDate = visitSearched.getEndDate();
			Place place = visitSearched.getPlace();

			startHour= readStartHour(place, day, startDate, endDate);

			visitSearched.setStartHour(startHour); 
		});
	}

	
	public void modify(ArrayList<Volunteer> volunteer) {
		
		boolean confirmInsert = false;
		ArrayList<DayOfWeek> day = new ArrayList<>();
		VisitType visitSearched;
		do {
			//ricerco il tipo di visita
			visitSearched=readAndSearchVisitType();

			//definisco il menu per selezionare cosa voglio modificare
			int opz = visitTypeView.askMenuVoice();
			//selezione operazione di modifica
			
			this.initModifyActions(visitSearched, day);

			Runnable action = modifyActions.get(opz);
			
			if(action != null)
				action.run();

			
			confirmInsert = visitTypeView.confirmModify();

			if(confirmInsert) {
				visitTypeList.modifyVisitType(visitSearched);
				visitTypeView.showModifySuccess();
			}
			else
				visitTypeView.showCancelModify();
		}while(!confirmInsert);
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
			visitTypeView.showVisitTypeNoAvailable();
			return null;
		}

		
		do {
			title = visitTypeView.askTitle();
			if(!visitTypeList.findVisitType(title))
				visitTypeView.showVisitTypeNotFoundError();
		}while(!visitTypeList.findVisitType(title));

		confirmInsert = visitTypeView.confirmRemove();
		if(confirmInsert) {
			removedVisitType = visitTypeList.getVisitType(title);
			visitTypeList.removeVisitType(removedVisitType);
			visitTypeView.showRemoveSuccess();
			return removedVisitType;
		}
		else {
			visitTypeView.showCancelRemove();
			return null;
		}
	}

	//metodo ausiliario per la lettura e la ricerca di un determinato visitType
	private VisitType readAndSearchVisitType() {
		String title;
		VisitType visitTypeSearched;

		do {
			title = visitTypeView.askTitle();
			visitTypeSearched = visitTypeList.searchVisitType(title);

			if(visitTypeSearched==null)
				visitTypeView.showVisitTypeNotFoundError();

		}while(visitTypeSearched==null);

		return visitTypeSearched;
	}

	//metodo che ritorna la lista dei tipi visita
	public VisitTypeList getVisitTypeList() {
		return visitTypeList;
	}

	//metodo che ricerca un determinato tipo di visita
	public void searchVisitType() {
		String title = visitTypeView.askTitle();
		VisitType visitTypeSearched=visitTypeList.searchVisitType(title);

		if(visitTypeSearched==null)
			visitTypeView.showVisitTypeNotFoundError();
		else
			visitTypeView.showVisitType(visitTypeSearched);
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
		}while(!visitTypeView.confirmAssociation());

		visitTypeView.showAssociationSuccess();
	}
}