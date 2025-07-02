package program.places;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utilities.*;

public class PlaceList {
	
	private Map<String, Place> placeList;
	private JsonManager jsonManager;
	
	public PlaceList(String jsonPath) {
		this.placeList = new HashMap<>();
		this.jsonManager = new JsonManager(jsonPath);
		Place.setRegion(this.jsonManager.loadFirstElement("region"));
		loadPlace();
	}
	
	private void loadPlace() {	
		List<Place> list = jsonManager.loadData(Place.class);
		for(Place place : list) {
			if(place.getName() != null)
				placeList.put(place.getName(), place);
		}
	}
	
	public void addPlace(Place place) {
		this.jsonManager.addElement(Place.class, place);
		this.placeList.put(place.getName(), place);
	}
	
	public void viewPlace() {	
		if(placeList.size() == 0)
			System.out.println("Nessun luogo presente...");
		else {
			for(Place place : placeList.values())
				System.out.println(place.toString());
		}
	}
	
	
	public boolean findPlace(String name) {
		return placeList.containsKey(name);
	}
	
	public Place getPlace(String name) {
		return this.placeList.get(name);
	}
	
	public void modifyPlace(Place place) {
		this.jsonManager.modifyElement("name", place.getName(), place);
	}
	
	public Place searchPlace(String key) {
		return this.jsonManager.searchElement(Place.class,"name", key);
	}
	
}
