package program.visitistance;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import utilities.JsonManager;
import utilities.RepositorySystem;
import utilities.TimeSimulator;

class TestExcludedDates {
	
	private ExcludedDates excludedDates;
	private TimeSimulator time; 
	private File tempJsonFileExcludedDates;
    private LocalDate expectedExcludedDate;
    private RepositorySystem excludedDatesRepository;
    
    private Month modifyMonth;
    private int day; 

    @BeforeEach
    void setUp() throws IOException {
        time = new TimeSimulator(LocalDate.of(2025, 1, 1));
        tempJsonFileExcludedDates = File.createTempFile("escludedDates_test", ".json");
        excludedDatesRepository = new JsonManager(tempJsonFileExcludedDates.getAbsolutePath());
        excludedDates = new ExcludedDates(excludedDatesRepository);
        
        modifyMonth = time.getCurrentDate().getMonth().plus(3); //=> Aprile
        day = 29;
        expectedExcludedDate = LocalDate.of(time.getCurrentDate().getYear(), modifyMonth, day);
    }

    @AfterEach
    void tearDown() {
    	if (tempJsonFileExcludedDates != null && tempJsonFileExcludedDates.exists()) {
        	tempJsonFileExcludedDates.delete();	//Rimozione del Json di test
        }
    }

    @Test
    void testExcludeDateAddsNewDate() {
        
        //=> STUB: ExcludedDatesView
        ExcludedDatesView view = new ExcludedDatesView() {
            @Override public int askExcludedDate(Month modifyMonth, TimeSimulator time) { return day; }
            @Override public boolean askConfirmationExcludeData() { return true; }
            //Metodi non usati nel test
            @Override public boolean askConfirmationRemoveData() { return false; }
            @Override public void showDateAlreadyExist() {}
            @Override public void showConfirmationExcludeData() {}
            @Override public void showMessageHeadView() {}
            @Override public void showDate(LocalDate date) {}
            @Override public void showDateNotFound() {}
            @Override public void showNoDateEntered() {}
            @Override public void showNoOperation() {}
            @Override public void showConfirmRemoveDate() {}
        };
        
        excludedDates = new ExcludedDates(excludedDatesRepository, view);
        excludedDates.excludeDate(time); //=> Escludo la data

        assertTrue(excludedDates.isDateExcluded(expectedExcludedDate)); //=> Verifica che sia esclusa
    }
    
    @Test
    void testExcludeAndRemoveDate() {
  
    	//=> STUB: ExcludedDatesView
        ExcludedDatesView view = new ExcludedDatesView() {
            @Override public int askExcludedDate(Month modifyMonth, TimeSimulator time) { return day; }

            @Override public boolean askConfirmationExcludeData() {  return true; }

            @Override public boolean askConfirmationRemoveData() { return true;  }

            //Metodi non usati nel test
            @Override public void showDateAlreadyExist() {}
            @Override public void showNoOperation() {}
            @Override public void showConfirmationExcludeData() {}
            @Override public void showMessageHeadView() {}
            @Override public void showDate(LocalDate date) {}
            @Override public void showDateNotFound() {}
            @Override public void showNoDateEntered() {}
            @Override public void showConfirmRemoveDate() {}
        };

        excludedDates = new ExcludedDates(excludedDatesRepository, view);

        excludedDates.excludeDate(time); //=> Escludo la data
        assertTrue(excludedDates.isDateExcluded(expectedExcludedDate));

        excludedDates.removeExcludedDate(time); //=> Rimuovo la data esclusa
        assertFalse(excludedDates.isDateExcluded(expectedExcludedDate));
    }

}
