package program.places;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import utilities.RepositorySystem;

public class PlaceList {

	private Map<String, Place> placeList;
	private RepositorySystem repositoryPlaceSystem;

	public PlaceList(RepositorySystem repositoryPlaceSystem) {
		this.placeList = new HashMap<>();
		this.repositoryPlaceSystem = repositoryPlaceSystem;
		Place.setRegion(this.repositoryPlaceSystem.loadFirstElement("region"));
		loadPlace();
	}

	private void loadPlace() {
		List<Place> list = repositoryPlaceSystem.loadData(Place.class);
		for(Place place : list) {
			if(place.getName() != null)
				placeList.put(place.getName(), place);
		}
	}
	
	public Map<String, Place> getPlaceList() {
		return this.placeList;
	}

	public void addPlace(Place place) {
		this.repositoryPlaceSystem.addElement(Place.class, place);
		this.placeList.put(place.getName(), place);
	}

	public void removePlace(Place place) {
		this.repositoryPlaceSystem.removeElement(Place.class, place);
		this.placeList.remove(place.getName());
	}


	public boolean findPlace(String name) {
		return placeList.containsKey(name);
	}

	public Place getPlace(String name) {
		return this.placeList.get(name);
	}

	public void modifyPlace(Place place) {
		this.placeList.put(place.getName(), place);
		this.repositoryPlaceSystem.modifyElement("name", place.getName(), place);
	}

	public Place searchPlace(String key) {
		return this.repositoryPlaceSystem.searchElement(Place.class,"name", key);
	}

	public boolean isEmpty() {
		return this.placeList.isEmpty();
	}
	
	public void setRegion(String region) {
		repositoryPlaceSystem.modifyObject("region", "null", region);
		Place.setRegion(region);
	}
	
	public Collection <Place> getAllPlaces() {
		return placeList.values();
	}
	
	public boolean exists(String placeName) {
	    return placeList.containsKey(placeName);
	}

}
