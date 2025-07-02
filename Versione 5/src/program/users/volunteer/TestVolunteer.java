package program.users.volunteer;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import program.visitistance.ExcludedDates;
import program.visittype.VisitType;
import program.visittype.VisitTypeList;
import utilities.JsonManager;
import utilities.TimeSimulator;

class TestVolunteer{

	private File tempJsonFileVolunteer;
	private File tempJsonFileVisitType;
	private File tempJsonFileExcludedDates;
	private VolunteerList volunteerList;
	private VisitTypeList visitTypeList;
	private String name;
	private String surname;
	private String nikcname;
	private String password;
	private TimeSimulator time;
	private ExcludedDates excludedDates;
	
	@BeforeEach
	public void setup() throws IOException {
		tempJsonFileVolunteer = File.createTempFile("volontari_test", ".json");
		volunteerList = new VolunteerList(new JsonManager(tempJsonFileVolunteer.getAbsolutePath()));
		
		tempJsonFileVisitType = File.createTempFile("tipoVisita_test", ".json");
		visitTypeList = new VisitTypeList(new JsonManager(tempJsonFileVisitType.getAbsolutePath()));
		
		tempJsonFileExcludedDates = File.createTempFile("escludedDates_test", ".json");
		
		name = "Tommy";
		surname = "Belu";
		nikcname = "TommyB";
		password = "1234";
	}
	
	@AfterEach	//Dopo l'esecuzione del test
    public void teardown() {
        if (tempJsonFileVolunteer != null && tempJsonFileVolunteer.exists()) {
            tempJsonFileVolunteer.delete();	//Rimozione del Json di test
        }
        
        if (tempJsonFileVisitType != null && tempJsonFileVisitType.exists()) {
            tempJsonFileVisitType.delete();	//Rimozione del Json di test
        }
        
        if (tempJsonFileExcludedDates != null && tempJsonFileExcludedDates.exists()) {
        	tempJsonFileExcludedDates.delete();	//Rimozione del Json di test
        }
    }
	
	
	@Test
	public void add() {	
		
		VolunteerView fakeView = new VolunteerView() {
			@Override public String askNickname() { return nikcname;}
			@Override public String askSurnameVolunteer() {return surname;}
			@Override public String askNameVolunteer() {return name;}
			@Override public boolean askConfirmAddVolunteer() {return true;}
			@Override public void showVolunteerNotFoundError() {}
			@Override public void showVolunteer(Volunteer volunteer) {}
			@Override public void showRemovalCancelled() {}
			@Override public void showNotFoundDataError() {}
			@Override public void showNoVolunteerRegistered() {}
			@Override public void showNoVolunteerAvailabile() {}
			@Override public void showNoVisitTypeError() {}
			@Override public void showNameEqualUserError() {}
			@Override public void showNameEqualConfiguratorError() {	}
			@Override public String showMessageExcludedDate() {return null;}
			@Override public String showMessageDateForAvailability() {return null;}
			@Override public void showDateExcludedError() {}
			@Override public void showDateAlreadyExcludedError() {}
			@Override public void showConfirmAvailability() {}
			@Override public void showConfimRemoval() {}
			@Override public void showAvailabilityMessage() {}
			@Override public void showAvailabilityDate(LocalDate date) {}
			@Override public boolean confirmInput() {return true;}
			@Override public boolean askRemoveAvailability() {return false;}
			@Override public int askDayOfMonth(Month modifyMonth, TimeSimulator time) {return 0;}
			@Override public boolean askContinueAdding() {return false;}
			@Override public boolean askConfirmRemoval() {return false;}
			@Override public boolean askConfirmAvailability() {return false;}
		};
		
		VolunteerManager manager = new VolunteerManager(fakeView, volunteerList);
		
		//metodo testato
		manager.add();
		
		//controllo l'aggiunta
		Volunteer volunteerAdded = volunteerList.getVolunteer(nikcname);
		
		assertNotNull(volunteerAdded);
		assertEquals(name, volunteerAdded.getName());
		assertEquals(surname, volunteerAdded.getSurname());
		assertEquals(nikcname, volunteerAdded.getNickname());
	}
	
	
	@Test
	public void remove() {
		volunteerList.addVolunteer(new Volunteer(name, surname, nikcname, password));
		
		VolunteerView fakeView = new VolunteerView() {
			
			@Override public String askNickname() { return nikcname;}
			@Override public boolean askConfirmRemoval() {return true;}
			@Override public String askSurnameVolunteer() {return null;}
			@Override public String askNameVolunteer() {return null;}
			@Override public boolean askConfirmAddVolunteer() {return false;}
			@Override public void showVolunteerNotFoundError() {}
			@Override public void showVolunteer(Volunteer volunteer) {}
			@Override public void showRemovalCancelled() {}
			@Override public void showNotFoundDataError() {}
			@Override public void showNoVolunteerRegistered() {}
			@Override public void showNoVolunteerAvailabile() {}
			@Override public void showNoVisitTypeError() {}
			@Override public void showNameEqualUserError() {}
			@Override public void showNameEqualConfiguratorError() {	}
			@Override public String showMessageExcludedDate() {return null;}
			@Override public String showMessageDateForAvailability() {return null;}
			@Override public void showDateExcludedError() {}
			@Override public void showDateAlreadyExcludedError() {}
			@Override public void showConfirmAvailability() {}
			@Override public void showConfimRemoval() {}
			@Override public void showAvailabilityMessage() {}
			@Override public void showAvailabilityDate(LocalDate date) {}
			@Override public boolean confirmInput() {return false;}
			@Override public boolean askRemoveAvailability() {return false;}
			@Override public int askDayOfMonth(Month modifyMonth, TimeSimulator time) {return 0;}
			@Override public boolean askContinueAdding() {return false;}
			@Override public boolean askConfirmAvailability() {return false;}
			
		};
		
		int initialSize = volunteerList.getVolunteerList().size();
		VolunteerManager manager = new VolunteerManager(fakeView, volunteerList);
		Volunteer volunteerRemoved = manager.remove();
		assertNotNull(volunteerRemoved);
		assertEquals(initialSize - 1, volunteerList.getVolunteerList().size());
		assertEquals(nikcname, volunteerRemoved.getNickname());
	}
	
	
	@Test
	public void addAvailability() {
	    Volunteer volunteer = new Volunteer(name, surname, nikcname, password);
	    volunteerList.addVolunteer(volunteer);

	    // Istanza fittizia per simulare una data attuale
	    time = new TimeSimulator(LocalDate.of(2025, 6, 16));
	    excludedDates = new ExcludedDates(new JsonManager(tempJsonFileExcludedDates.getAbsolutePath())); // Nessuna data esclusa
	    //inserisco i dati di una visita fittizia
	    visitTypeList.addVisitType(new VisitType("titolo", "descrizione", "puntodiincontro", "2020-01-01","2027-01-01",new ArrayList<>(Arrays.asList(DayOfWeek.FRIDAY)), "10:00",20,true,20,30,null,new ArrayList<>(Arrays.asList(volunteer)) ));
	    
	    // Data disponibile simulata
	    LocalDate expectedDate = LocalDate.of(2025, 6, 20);

	    VolunteerView fakeView = new VolunteerView() {
	        @Override public int askDayOfMonth(Month modifyMonth, TimeSimulator time) { return 20; }
	        @Override public boolean askConfirmAvailability() { return true; }

	        @Override public void showConfirmAvailability() {}
	        @Override public void showDateAlreadyExcludedError() {}
	        @Override public void showDateExcludedError() {}
	        @Override public void showNoVisitTypeError() {}

	        // Metodi non usati in questo test
	        @Override public String askNickname() { return null; }
	        @Override public boolean askConfirmRemoval() { return false; }
	        @Override public String askSurnameVolunteer() { return null; }
	        @Override public String askNameVolunteer() { return null; }
	        @Override public boolean askConfirmAddVolunteer() { return false; }
	        @Override public void showVolunteerNotFoundError() {}
	        @Override public void showVolunteer(Volunteer volunteer) {}
	        @Override public void showRemovalCancelled() {}
	        @Override public void showNotFoundDataError() {}
	        @Override public void showNoVolunteerRegistered() {}
	        @Override public void showNoVolunteerAvailabile() {}
	        @Override public void showNameEqualUserError() {}
	        @Override public void showNameEqualConfiguratorError() {}
	        @Override public String showMessageExcludedDate() { return null; }
	        @Override public String showMessageDateForAvailability() { return null; }
	        @Override public void showAvailabilityMessage() {}
	        @Override public void showAvailabilityDate(LocalDate date) {}
	        @Override public boolean confirmInput() { return false; }
	        @Override public boolean askRemoveAvailability() { return false; }
	        @Override public boolean askContinueAdding() { return false; }
			@Override public void showConfimRemoval() {}
	    };

	    VolunteerManager manager = new VolunteerManager(fakeView, volunteerList) {
	        @Override
	    	protected LocalDate askValidDate(Volunteer v, TimeSimulator t, ExcludedDates e, VisitTypeList vl, LocalDate bd, Month m, int pm) {
	            return expectedDate; // Simula direttamente la data valida
	        }
	    };
	    
	    //testo il metodo
	    manager.addAvailability(volunteer, time, excludedDates, visitTypeList);
	    
	    assertNotNull(volunteerList.getVolunteer(nikcname));
	    assertEquals(volunteer.isAvailableOn(expectedDate), true);
	}
	
	
	@Test
	public void removeAvailability() {
	    LocalDate availabilityToRemove = LocalDate.of(2025, 6, 20);
	    
	    Volunteer volunteer = new Volunteer(name, surname, nikcname, password);
	    volunteerList.addVolunteer(volunteer);

	    VolunteerView fakeView = new VolunteerView() {
	        @Override public String askNickname() { return nikcname; }
	        @Override public boolean askRemoveAvailability() { return true; } // Conferma rimozione
	        @Override public int askDayOfMonth(Month modifyMonth, TimeSimulator time) {
	            return availabilityToRemove.getDayOfMonth(); // Giorno da rimuovere
	        }
	        @Override public void showNotFoundDataError() {} 
	        @Override public void showVolunteerNotFoundError() {}
	        @Override public void showVolunteer(Volunteer v) {}
	        @Override public void showRemovalCancelled() {}
	        @Override public void showNoVolunteerRegistered() {}
	        @Override public void showNoVolunteerAvailabile() {}
	        @Override public void showNoVisitTypeError() {}
	        @Override public void showNameEqualUserError() {}
	        @Override public void showNameEqualConfiguratorError() {}
	        @Override public String showMessageExcludedDate() { return null; }
	        @Override public String showMessageDateForAvailability() { return null; }
	        @Override public void showDateExcludedError() {}
	        @Override public void showDateAlreadyExcludedError() {}
	        @Override public void showConfirmAvailability() {}
	        @Override public void showConfimRemoval() {}
	        @Override public void showAvailabilityMessage() {}
	        @Override public void showAvailabilityDate(LocalDate date) {}
	        @Override public boolean confirmInput() { return false; }
	        @Override public boolean askConfirmAvailability() { return true; }
	        @Override public boolean askConfirmRemoval() { return true; }
	        @Override public boolean askContinueAdding() { return false; }
	        @Override public String askSurnameVolunteer() { return null; }
	        @Override public String askNameVolunteer() { return null; }
			@Override public boolean askConfirmAddVolunteer() {return true;}
	    }; 

	    TimeSimulator fakeTime = new TimeSimulator(availabilityToRemove) {
	        @Override public LocalDate getDateForAvailability() {
	            return availabilityToRemove.withDayOfMonth(1); // 1 giugno 2025, inizio mese
	        }
	    };

	    VolunteerManager manager = new VolunteerManager(fakeView, volunteerList) {
	        @Override
	    	protected LocalDate askValidDate(Volunteer v, TimeSimulator t, ExcludedDates e, VisitTypeList vl, LocalDate bd, Month m, int pm) {
	            return availabilityToRemove; // Simula direttamente la data valida
	        }
	    };
	    
	    //aggiungo la disponibilità e testo
	    manager.addAvailability(volunteer, fakeTime, excludedDates, visitTypeList);
	    assertTrue(volunteer.isAvailableOn(availabilityToRemove));
	    
	    //rimuovo la disponibilità e testo
	    manager.removeAvailability(volunteer, fakeTime);
	    assertFalse(volunteer.isAvailableOn(availabilityToRemove));
	}
}

