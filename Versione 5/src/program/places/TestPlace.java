package program.places;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import utilities.JsonManager;

class TestPlace {

	private File tempJsonFile;
    private PlaceList placeList;

    @BeforeEach	//Creazione del setup prima del test
    public void setup() throws IOException {
        
        tempJsonFile = File.createTempFile("luoghi_test", ".json"); //--> creazione di un file Json di Test
        
        placeList = new PlaceList(new JsonManager(tempJsonFile.getAbsolutePath()));
    }

    @AfterEach	//Dopo l'esecuzione del test
    public void teardown() {
        if (tempJsonFile != null && tempJsonFile.exists()) {
            tempJsonFile.delete();	//Rimozione del Json di test
        }
    }
    
    @Test
    public void add() {

        // Valori simulati per input
        String name = "Parco Nazionale";
        String description = "Un bellissimo parco";
        String address = "Via del Bosco 1";

        // Flag per verificare se il messaggio di successo è stato mostrato
        final boolean[] successShown = {false};

        // Stub di IPlaceView tramite classe anonima
        PlaceView fakeView = new PlaceView() {
        	@Override public String askPlaceName() { return name; }
        	
            @Override public String askPlaceDescription() { return description; }

            @Override public String askPlaceAddress() { return address; }

            @Override public boolean confirmInsert() { return true; }

            @Override public void showInsertSuccess() { successShown[0] = true; }

            // Metodi non usati ma da implementare
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

        // Stub di PlaceManager che restituisce un nome fisso
        PlaceManager manager = new PlaceManager(fakeView, placeList);

        // Chiamata al metodo da testare
        Place result = manager.add();

        //Test
        assertNotNull(result);
        assertEquals(name, result.getName());
        assertEquals(description, result.getDescription());
        assertEquals(address, result.getAddress());
        assertTrue(manager.getPlaceList().findPlace(name));
        assertTrue(successShown[0]);
    }
    
    @Test
    public void modify() {

    	String name = "Parco Nazionale";
        String description = "Descrizione originale";
        String address = "Via Vecchia";

        Place initialPlace = new Place(name, description, address, "Lazio");
        placeList.addPlace(initialPlace); 

        // Valori per la modifica
        String newDescription = "Un bellissimo parco all'aperto";

        // Stub di IPlaceView tramite classe anonima
        PlaceView fakeView = new PlaceView() {
        	@Override 
        	public String askPlaceName() { return name; }
        	
        	@Override public int askMenuVoice() { return 1; }
        	
            @Override public String askPlaceDescription() { return newDescription; }

            @Override public String askPlaceAddress() { return address; }

            @Override public boolean confirmInsert() { return true; }
            
            @Override public String confirmModify() { return "SI"; }
            
            // Metodi non usati ma da implementare
            @Override public String askRegion() { return null; }
            
            @Override public void showPlace(Place place) {}
            @Override public void showAlreadyInsertedError() {}
            @Override public void showPlaceNotFoundError() {}
            @Override public void showModifySuccess() {}
            @Override public void showCancelModify() {}
            @Override public void showRemoveSuccess() {}
            @Override public void showCancelRemove() {}
            @Override public void showNoPlaceAvailable() {}
            @Override public void showNoPlace() {}
            @Override public void showInsertSuccess() {}
            @Override public boolean confirmInsertRegion() { return false; }
            @Override public boolean confirmRemove() { return false; }
        };

        // Stub di PlaceManager che restituisce un nome fisso
        PlaceManager manager = new PlaceManager(fakeView, placeList);

        // Chiamata al metodo da testare
        manager.modify();
        Place result = manager.getPlaceList().getPlace(name);

        //Test
        assertNotNull(result);
        assertEquals(name, result.getName());
        assertEquals(newDescription, result.getDescription());
        assertEquals(address, result.getAddress());
        assertTrue(manager.getPlaceList().findPlace(name));
    }

}
