package program.visitistance;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import program.places.Place;
import program.users.volunteer.Volunteer;
import program.visitistance.state.VisitStateEnum;
import program.visittype.VisitType;
import utilities.JsonManager;
import utilities.RepositorySystem;

class TestVisit {

    private File tempVisitJsonFile;
    private File tempVisitTypeJsonFile;
    private File tempExcludedDatesJsonFile;
    
    private RepositorySystem visitRepository;
    private RepositorySystem visitTypeRepository;
    private RepositorySystem excludedDatesRepository;
    
    private VisitList visitList;
    private VisitManager visitManager;
    private Visit testVisit;
    private VisitType testVisitType;
    private int min; 
    private int max; 
    private LocalDate date;
    
    private VisitType createVisitType() {
    	String title = "Visita Guidata", description = "Visita culturale al Colosseo di Roma", meetingPoint = "Colosseo";
		String startDate = "2025-03-01", endDate = "2025-11-30", startHour = "11.30";
		ArrayList<DayOfWeek> days = new ArrayList<>(List.of(DayOfWeek.SUNDAY));
		int duration = 60, minParticipant = 0, maxParticipant = 20;
		
		ArrayList<Volunteer> volunteers = new ArrayList<>();
		volunteers.add(new Volunteer("Mario", "Rietti", "Marietto", "rietto"));
		
		Place place = new Place("Colosseo", "Anfiteatro romano", "Piazza del Colosseo", "Roma");
		return new VisitType(title, description, meetingPoint, startDate, endDate, days, startHour, 
				duration, false, minParticipant, maxParticipant, place, volunteers);
    }
    
    private void initRepository() throws IOException {
    	//=> Creazione dei file json
        tempVisitJsonFile = File.createTempFile("visite_test", ".json");
        tempVisitTypeJsonFile = File.createTempFile("visitType_test", ".json");
        tempExcludedDatesJsonFile = File.createTempFile("excludedDates_test", ".json");
        String initialJson = """
        	     [{ "numberOfSub" : 1000 }]
        	    """;
        Files.writeString(tempVisitJsonFile.toPath(), initialJson);
        
        visitRepository = new JsonManager(tempVisitJsonFile.getAbsolutePath());
        visitTypeRepository = new JsonManager(tempVisitTypeJsonFile.getAbsolutePath());
        excludedDatesRepository = new JsonManager(tempExcludedDatesJsonFile.getAbsolutePath());
    }

    @BeforeEach
    public void setup() throws IOException {
        
    	initRepository();
    	
    	date = LocalDate.of(2025, 6, 15);  // => Data di riferimento in modo da non dipendere da LocalDate.now()
        
        visitList = new VisitList(visitRepository);

        //=> STUB: VisitView
        VisitView fakeView = new ConsoleVisitView();
        
        visitManager = new VisitManager(fakeView, visitTypeRepository, excludedDatesRepository, visitRepository); //=> Creazione di VisitManager
        
        testVisitType = createVisitType(); //=> Creazione di VisitType

        testVisit = new Visit(testVisitType, date.toString(), VisitStateEnum.PROPOSTA); //=> Creazione di Visit

        visitList.addVisit(testVisit);  //=> Visit viene aggiunta a VisitList
        
        min = testVisitType.getMinParticipant();
        max = testVisitType.getMaxParticipant();

    }

    @AfterEach
    public void teardown() {
        if (tempVisitJsonFile != null && tempVisitJsonFile.exists()) {
        	tempVisitJsonFile.delete();
        }
        if (tempVisitTypeJsonFile != null && tempVisitTypeJsonFile.exists()) {
        	tempVisitTypeJsonFile.delete();
        }
        if (tempExcludedDatesJsonFile != null && tempExcludedDatesJsonFile.exists()) {
        	tempExcludedDatesJsonFile.delete();
        }
    }

    @Test
    public void updateVisitState() {
        
        int newParticipants = 10;
        LocalDate newDate = date.plusDays(5);
        
        Visit updatedVisit = visitManager.updateVisitState(testVisit, newParticipants, newDate);

        assertNotNull(updatedVisit);

        assertNotNull(updatedVisit.getState());

        assertTrue(newParticipants <= updatedVisit.returnMaxParticipant());

    }


    @Test //=> PROPOSTA --> COMPLETA
    public void proposedToComplete() {
        testVisit.setStateEnum(VisitStateEnum.PROPOSTA);
        testVisit.setDate(date.plusDays(10).toString());
        Visit result = visitManager.updateVisitState(testVisit, max, date);
        assertEquals(VisitStateEnum.COMPLETA, result.getStateEnum());
    }

    @Test //=> PROPOSTA --> CONFERMATA
    public void proposedToConfirmed() {
        testVisit.setStateEnum(VisitStateEnum.PROPOSTA);
        testVisit.setDate(date.plusDays(2).toString());
        Visit result = visitManager.updateVisitState(testVisit, min + 1, date);
        assertEquals(VisitStateEnum.CONFERMATA, result.getStateEnum());
    }

    @Test //=> PROPOSTA --> CANCELLATA
    public void proposedToCancelled() {
        testVisit.setStateEnum(VisitStateEnum.PROPOSTA);
        testVisit.setDate(date.plusDays(2).toString());
        Visit result = visitManager.updateVisitState(testVisit, min - 1, date);
        assertEquals(VisitStateEnum.CANCELLATA, result.getStateEnum());
    }

    @Test //=> PROPOSTA --> PROPOSTA
    public void proposedToProposed() {
        testVisit.setStateEnum(VisitStateEnum.PROPOSTA);
        testVisit.setDate(date.plusDays(10).toString());
        Visit result = visitManager.updateVisitState(testVisit, min + 1, date);
        assertEquals(VisitStateEnum.PROPOSTA, result.getStateEnum());
    }

    @Test  //=> COMPLETA --> PROPOSTA
    public void completeToProposed() {
        testVisit.setStateEnum(VisitStateEnum.COMPLETA);
        testVisit.setDate(date.plusDays(5).toString());
        Visit result = visitManager.updateVisitState(testVisit, max - 1, date);
        assertEquals(VisitStateEnum.PROPOSTA, result.getStateEnum());
    }

    @Test  //=> COMPLETA --> CONFERMATA
    public void completeToConfirmed() {
        testVisit.setStateEnum(VisitStateEnum.COMPLETA);
        testVisit.setDate(date.plusDays(2).toString());
        Visit result = visitManager.updateVisitState(testVisit, max, date);
        assertEquals(VisitStateEnum.CONFERMATA, result.getStateEnum());
    }

    @Test  //=> CONFERMATA --> EFFETTUATA
    public void confirmedToCompleted() {
        testVisit.setStateEnum(VisitStateEnum.CONFERMATA);
        testVisit.setDate(date.minusDays(1).toString());
        Visit result = visitManager.updateVisitState(testVisit, max, date);
        assertEquals(VisitStateEnum.EFFETTUATA, result.getStateEnum());
    }
}
