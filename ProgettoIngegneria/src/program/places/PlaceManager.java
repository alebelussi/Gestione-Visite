package program.places;

import java.util.Map;
import utilities.RepositorySystem;

public class PlaceManager {

	private PlaceList placeList;
	private PlaceView placeView; 

	private Map<Integer, Runnable> modifyActions;
	
	public PlaceManager(PlaceView placeView, RepositorySystem repositoryPlaceSystem) {
		this.placeList = new PlaceList(repositoryPlaceSystem);
		this.placeView = placeView;
	}
	
	public PlaceManager(PlaceView placeView, PlaceList placeList) {
		this.placeList = placeList;
		this.placeView = placeView;
	}
	
	private void initModifyActions(Place placeSearched) {
		modifyActions = Map.of(
				1, () -> placeSearched.setDescription(placeView.askPlaceDescription()),
				2, () -> placeSearched.setAddress(placeView.askPlaceAddress())
			);
	}

	/*@
		requires placeList != null;
		requires Place.GetRegion() != null;
	 	
	 	ensures \result != null;
	 	ensures placeList.findPlace(\result.getName());
	 	ensures placeList.size() == \old(placeList.size()) + 1;
	 @*/
	public Place add() {
		Place placeToAdd;

		do {
			String name = askUniquePlaceName();
			String description = placeView.askPlaceDescription();
			String address = placeView.askPlaceAddress();
			placeToAdd= new Place(name, description, address, Place.getRegion());
		}while(!placeView.confirmInsert());
		
		placeList.addPlace(placeToAdd);
		placeView.showInsertSuccess();
		
		return placeToAdd;
	}
	
	private String askUniquePlaceName() {
		String name;
		do {
			name = placeView.askPlaceName();

			if(placeList.findPlace(name))
				placeView.showAlreadyInsertedError();

		}while(placeList.findPlace(name));
		return name;
	}

	public void view() {
		if(placeList.getPlaceList().size() == 0)
			placeView.showNoPlace();
		else {
			for(Place place : placeList.getAllPlaces())
				placeView.showPlace(place);
		}
	}

	/*@
		requires placeList != null
		
		invariant placeList.size() == \old(placeList.size()); 
		
		assignable placeSearched.description, placeSearched.address;
		
		ensures placeList.findPlace(placeSearched.getName());
	 @*/
	
	
	public void modify() {
	    Place placeSearched;
	    String confirmInsert;

	    do {
	        placeSearched = askForExistingPlace();

	        this.initModifyActions(placeSearched);
	        int option = placeView.askMenuVoice();
	        Runnable action = modifyActions.get(option);

	        if (action != null) {
	            action.run();
	            confirmInsert = placeView.confirmModify();

	            if (confirmInsert.equalsIgnoreCase("SI")) {
	                placeList.modifyPlace(placeSearched);
	                placeView.showModifySuccess();
	            }
	        } else {
	            confirmInsert = "NOT";
	            placeView.showCancelModify();
	        }

	    } while (confirmInsert.equalsIgnoreCase("NO"));
	}

	private Place askForExistingPlace() {
	    Place place;
	    do {
	        String name = placeView.askPlaceName();
	        place = placeList.searchPlace(name);
	        if (place == null) {
	            placeView.showPlaceNotFoundError();
	        }
	    } while (place == null);
	    return place;
	}


	/*@
		requires placeList != null
		
		ensures !placeList.findPlace(placeSearched.getName());
		ensures placeList.size() == \old(placeList.size()) - 1; 
	 @*/
	public Place remove() {
		String name;
		boolean confirmInsert;
		Place removedPlace = new Place();

		if(placeList.isEmpty()) {
			placeView.showNoPlaceAvailable();
			return null;
		}
		
		name = askExistingPlaceName();
		confirmInsert = placeView.confirmRemove();
		if(confirmInsert) {
			removedPlace = placeList.getPlace(name);
			placeList.removePlace(removedPlace);
			placeView.showRemoveSuccess();
			return removedPlace;
		}
		else {
			placeView.showCancelRemove();
			return null;
		}
		
	}
	
	private String askExistingPlaceName() {
		String name;
		do {
			name = placeView.askPlaceName();

			if(!placeList.findPlace(name))
				placeView.showPlaceNotFoundError();
		}while(!placeList.findPlace(name));
		
		return name;
	}

	public void setRegion() {
		String region;

		do {
			region = placeView.askRegion();
		}while(!placeView.confirmInsertRegion()); 

		placeList.setRegion(region);
	}

	public void searchPlace() {
		String name;
		Place placeSearched;
		name = placeView.askPlaceName();
		placeSearched=placeList.searchPlace(name);
		if(placeSearched==null)
			placeView.showPlaceNotFoundError();
		else
			placeView.showPlace(placeSearched);
	}

	public PlaceList getPlaceList() {
		return this.placeList;
	}
}