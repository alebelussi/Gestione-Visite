package program.users.availability;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import utilities.*;
import org.junit.jupiter.api.*;

class TestOpenCloseAvailability{

    private static File tempJsonFile;
    private OpenCloseAvailabilityManager manager;

    @BeforeEach
    public void setup() throws IOException {
        tempJsonFile = File.createTempFile("availability_test", ".json");
    }

    @AfterEach
    public void teardown() {
        if (tempJsonFile != null && tempJsonFile.exists()) {
            tempJsonFile.delete();
        }
    }

    @Test
    public void openAvailability() {
       TimeSimulator simulator = new TimeSimulator(LocalDate.of(2025, 6, 16)) {
            @Override
            public int getNumberOfDay() {
                return 16;
            }
        };

        OpenCloseAvailabilityView fakeView = new OpenCloseAvailabilityView() {
            @Override public boolean askAvaibilityOpen() { return true; }
            @Override public void showAvailabilityOpen() {}
            @Override public void showAvailabilityOpeningCancelled() {}
            @Override public void showAvailabilityOpeningImpossible() {}
        };

        manager = new OpenCloseAvailabilityManagerStub(fakeView);
        manager.openAvailability(simulator);

        assertTrue(OpenCloseAvailabilityManagerStub.getAvailabilityOpen());
        assertEquals(true, OpenCloseAvailabilityManagerStub.getAvailabilityOpen());
    }
    
    @Test
    public void closeAvailability() {
    	 OpenCloseAvailabilityView fakeView = new OpenCloseAvailabilityView() {
             @Override public boolean askAvaibilityOpen() { return true; }
             @Override public void showAvailabilityOpen() {}
             @Override public void showAvailabilityOpeningCancelled() {}
             @Override public void showAvailabilityOpeningImpossible() {}
         };

         manager = new OpenCloseAvailabilityManagerStub(fakeView);
         manager.closeAvaialbility();
        
         assertEquals(false, OpenCloseAvailabilityManagerStub.getAvailabilityOpen());
    }

    // Stub di OpenCloseAvailabilityManager per usare il file temporaneo
    private static class OpenCloseAvailabilityManagerStub extends OpenCloseAvailabilityManager {
        public OpenCloseAvailabilityManagerStub(OpenCloseAvailabilityView view) {
            super(view, new JsonManager(tempJsonFile.getAbsolutePath()));
        }
    }
}


