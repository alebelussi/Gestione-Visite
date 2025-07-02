package program.visittype;

import static org.junit.jupiter.api.Assertions.*;


import java.io.File;
import java.io.IOException;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import program.places.PlaceView;
import program.places.Place;
import program.places.ConsolePlaceView;
import program.users.volunteer.Volunteer;
import utilities.JsonManager;
import utilities.RepositorySystem;

class TestVisitType {
	
	private File tempJsonFileVisitType;
	private VisitTypeList visitTypeList;
	private String title;
	private String description; 
	private String meetingPoint; 
	private String startDate; 
	private String endDate; 
	private String startHour; 
	private String namePlace; 
	private int duration;
	private int minParticipant; 
	private int maxParticipant; 
	private RepositorySystem repositoryVisitType;
	
	private Place place; 
	private ArrayList<Volunteer> volunteers;
	
	@BeforeEach
	public void setup() throws IOException {
		tempJsonFileVisitType = File.createTempFile("tipoVisita_test", ".json");
		repositoryVisitType = new JsonManager(tempJsonFileVisitType.getAbsolutePath());
		visitTypeList = new VisitTypeList(repositoryVisitType);
		
		//=> PREPARAZIONE DEI DATI NECESSARI
		title = "Visita Guidata";
		description = "Visita culturale al Colosseo di Roma"; 
		meetingPoint = "Colosseo";
		startDate = "2025-03-01";
		endDate = "2025-11-30"; 
		startHour = "11.30";
		namePlace = "Colosseo";
		duration = 60; 
		minParticipant = 0;
		maxParticipant = 30;
		
		place = new Place("Colosseo", "Anfiteatro romano", "Piazza del Colosseo", "Roma");
		
		volunteers = new ArrayList<>();
		volunteers.add(new Volunteer("Mario", "Rietti", "Marietto", "rietto"));
	}
	
	@AfterEach	//Dopo l'esecuzione del test
    public void teardown() {
        if (tempJsonFileVisitType != null && tempJsonFileVisitType.exists()) {
            tempJsonFileVisitType.delete();	//Rimozione del Json di test
        }
    }

	@Test
	public void add() {
		
		//=> PREPARAZIONE DEI DATI NECESSARI
		DayOfWeek day = DayOfWeek.SUNDAY;
		
		//=> STUB: VisitTypeView
		VisitTypeView fakeView = new VisitTypeView() {

			@Override
			public String askTitle() { return title; }

			@Override public String askDescription() { return description; }

			@Override public String askMeetingPoint() { return meetingPoint; }

			@Override public String askStartDate() { return startDate; }

			@Override public String askEndDate() { return endDate; }

			@Override public DayOfWeek askDay() { return day; }

			@Override public int askDuration() { return duration; }

			@Override public boolean askNeedBuyTicket() { return false; }

			@Override public int askMinParticipant() { return minParticipant; }

			@Override public int askMaxParticipant(int minParticipant) { return maxParticipant; }
			
			@Override public String askStartHour() { return startHour; }
			
			@Override public boolean confirmInsertAnotherDay() { return false; }
			
			@Override public boolean confirmInsert() { return true; }
			
			//Metodi da inserire ma non necessari
			@Override public int askMenuVoice() { return 0; }
			@Override public boolean confirmModify() { return false; }
			@Override public boolean confirmAssociation() { return false; }
			@Override public boolean confirmRemove() { return false; }
			@Override public void showTitleVisitView() {}
			@Override public void showVisitType(VisitType visitType) {}
			@Override public void showAlreadyInserted() {}
			@Override public void showTimeOverlapError() {}
			@Override public void showInsertSuccess() {}
			@Override public void showModifySuccess() {}
			@Override public void showCancelModify() {}
			@Override public void showAssociationSuccess() {}
			@Override public void showVisitTypeNotFoundError() {}
			@Override public void showVisitTypeNoAvailable() {}
			@Override public void showRemoveSuccess() {}
			@Override public void showCancelRemove() {}
			@Override public void confirmInsertMessage() {}
			@Override public void showErrorNoVisitType() {}
			
		};
		
		//=> STUB: PlaceView
		PlaceView fakePlaceView = new PlaceView() {
			@Override public String askPlaceName() { return namePlace; }
        	
            // Metodi non usati ma da implementare
			@Override public String askPlaceDescription() { return null; }
            @Override public String askPlaceAddress() { return null; }
            @Override public boolean confirmInsert() { return true; }
            @Override public void showInsertSuccess() {}
            @Override public String askRegion() { return null; }
            @Override public int askMenuVoice() { return 0; }
            @Override public void showPlace(Place place) {}
            @Override public void showAlreadyInsertedError() {}
            @Override public void showPlaceNotFoundError() {}
            @Override public void showModifySuccess() {}
            @Override public void showCancelModify() {}
            @Override public void showRemoveSuccess() {}
            @Override public void showCancelRemove() {}
            @Override public void showNoPlaceAvailable() {}
            @Override public void showNoPlace() {}
            @Override public boolean confirmInsertRegion() { return false; }
            @Override public String confirmModify() { return null; }
            @Override public boolean confirmRemove() { return false; }
		};
		
		int initialSize = visitTypeList.getVisitTypeList().size();
		VisitTypeManager manager = new VisitTypeManager(fakeView, fakePlaceView, visitTypeList, repositoryVisitType);
		
		manager.add(place, volunteers);
		
		assertEquals(initialSize + 1, visitTypeList.getVisitTypeList().size()); //=> verifica che l'elemento sia stato aggiunto
	    
		VisitType visitTypeAdded = visitTypeList.getVisitType(title);
		
		assertNotNull(visitTypeAdded); //=> verifica che l'elemento non sia null
		assertEquals(title, visitTypeAdded.getTitle());	//=> verifica che il titolo sia lo stesso
		assertEquals(description, visitTypeAdded.getDescription()); //=> verifica che la descrizione sia la stessa
		assertEquals(place, visitTypeAdded.getPlace());	//=> verifica che il luogo sia lo stesso
		assertTrue(visitTypeAdded.hasVolunteer(volunteers.get(0))); //=> verifica che i volontari siano stati assegnati
		   
	   
	}
	
	@Test
	public void modify() {
		
		//=> PREPARAZIONE DEI DATI NECESSARI
		DayOfWeek day = DayOfWeek.SUNDAY;
		ArrayList<DayOfWeek> days = new ArrayList<>(List.of(day));
		String newDescription = "Visita culturale all'anfiteatro Flavio";
		
		visitTypeList.addVisitType(new VisitType(title, description, meetingPoint, startDate, endDate, days, startHour, 
				duration, false, minParticipant, maxParticipant, place, volunteers));
		
		//=> STUB: visitTypeView
		VisitTypeView fakeView = new VisitTypeView() {

			@Override public String askTitle() { return title; }

			@Override public String askDescription() { return newDescription; }

			@Override public String askMeetingPoint() { return meetingPoint; }

			@Override public String askStartDate() { return startDate; }

			@Override public String askEndDate() { return endDate; }

			@Override public DayOfWeek askDay() { return day; }

			@Override public int askDuration() { return duration; }

			@Override public boolean askNeedBuyTicket() { return false; }

			@Override public int askMinParticipant() { return minParticipant; }

			@Override public int askMaxParticipant(int minParticipant) { return maxParticipant; }
			
			@Override public String askStartHour() { return startHour; }
			
			@Override public boolean confirmInsertAnotherDay() { return false; }
			
			@Override public boolean confirmInsert() { return true; }
			
			@Override public int askMenuVoice() { return 1; }
			
			@Override public boolean confirmModify() { return true; }
			
			//Metodi da inserire ma non necessari
			@Override public boolean confirmAssociation() { return false; }
			@Override public boolean confirmRemove() { return false; }
			@Override public void showTitleVisitView() {}
			@Override public void showVisitType(VisitType visitType) {}
			@Override public void showAlreadyInserted() {}
			@Override public void showTimeOverlapError() {}
			@Override public void showInsertSuccess() {}
			@Override public void showErrorNoVisitType() {}
			@Override public void showModifySuccess() {}
			@Override public void showCancelModify() {}
			@Override public void showAssociationSuccess() {}
			@Override public void showVisitTypeNotFoundError() {}
			@Override public void showVisitTypeNoAvailable() {}
			@Override public void showRemoveSuccess() {}
			@Override public void showCancelRemove() {}
			@Override public void confirmInsertMessage() {}
			
		};
		
		PlaceView fakePlaceView = new ConsolePlaceView();
		
		int initialSize = visitTypeList.getVisitTypeList().size();
		VisitTypeManager manager = new VisitTypeManager(fakeView, fakePlaceView, visitTypeList, repositoryVisitType);
		
		manager.modify(volunteers);
		
		assertEquals(initialSize, visitTypeList.getVisitTypeList().size()); //=> verifica che la dimensione non sia stata modificata
	    
		VisitType visitTypeAdded = visitTypeList.getVisitType(title);
		
		assertNotNull(visitTypeAdded);	//=> verifica che visitType non sia null
		assertEquals(newDescription, visitTypeAdded.getDescription()); //=> verifica che la descrizione sia stata modificata
	
	}
	
	@Test
	public void remove() {
		
		//=> PREPARAZIONE DEI DATI NECESSARI
		ArrayList<DayOfWeek> days = new ArrayList<>(List.of(DayOfWeek.SUNDAY));
			
		visitTypeList.addVisitType(new VisitType(title, description, meetingPoint, startDate, endDate, days, startHour, 
				duration, false, minParticipant, maxParticipant, place, volunteers));
		
		//=> STUB: visitTypeView
		VisitTypeView fakeView = new VisitTypeView() {

			@Override public String askTitle() { return title; }

			@Override public boolean confirmRemove() { return true; }
			
			//Metodi da inserire ma non necessari
			@Override public String askDescription() { return null; }
			@Override public String askMeetingPoint() { return null; }
			@Override public String askStartDate() { return null; }
			@Override public String askEndDate() { return null; }
			@Override public DayOfWeek askDay() { return null; }
			@Override public int askDuration() { return 0; }
			@Override public boolean askNeedBuyTicket() { return false; }
			@Override public int askMinParticipant() { return 0; }
			@Override public int askMaxParticipant(int minParticipant) { return 0; }
			@Override public String askStartHour() { return null; }
			@Override public boolean confirmInsertAnotherDay() { return false; }
			@Override public boolean confirmInsert() { return true; }
			@Override public void showErrorNoVisitType() {}
			@Override public int askMenuVoice() { return 0; }
			@Override public boolean confirmAssociation() { return false; }
			@Override public boolean confirmModify() { return false; }
			@Override public void showTitleVisitView() {}
			@Override public void showVisitType(VisitType visitType) {}
			@Override public void showAlreadyInserted() {}
			@Override public void showTimeOverlapError() {}
			@Override public void showInsertSuccess() {}
			@Override public void showModifySuccess() {}
			@Override public void showCancelModify() {}
			@Override public void showAssociationSuccess() {}
			@Override public void showVisitTypeNotFoundError() {}
			@Override public void showVisitTypeNoAvailable() {}
			@Override public void showRemoveSuccess() {}
			@Override public void showCancelRemove() {}
			@Override public void confirmInsertMessage() {}
			
		};
		
		PlaceView fakePlaceView = new ConsolePlaceView();
		
		int initialSize = visitTypeList.getVisitTypeList().size();
		
		VisitTypeManager manager = new VisitTypeManager(fakeView, fakePlaceView, visitTypeList, repositoryVisitType);
		VisitType visitTypeRemoved = manager.remove();
		
		assertNotNull(visitTypeRemoved); //=> verifica che visitType non sia null
		assertEquals(title, visitTypeRemoved.getTitle()); //=> verifica che l'elemento rimosso sia quello corretto
		assertEquals(initialSize - 1, visitTypeList.getVisitTypeList().size());	//=> verifica che la dimensione sia stata decrementata 
	
	}
}
