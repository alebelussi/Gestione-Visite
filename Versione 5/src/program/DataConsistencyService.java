package program;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import program.places.Place;
import program.places.PlaceList;
import program.registration.Registration;
import program.registration.RegistrationList;
import program.users.volunteer.Volunteer;
import program.users.volunteer.VolunteerList;
import program.visitistance.Visit;
import program.visittype.VisitType;
import program.visittype.VisitTypeList;
import utilities.RepositorySystem;

public class DataConsistencyService {

	//verifica se volunteer è ancora assegnato ad altri visitType
	private static boolean isVolunteerAssigned(VisitTypeList visitTypeList, Volunteer volunteer) {
		for(VisitType visitType : visitTypeList.getAllVisitTypes()) {
			if(visitType.getVolunteer().contains(volunteer))
				return true;
		}
		return false;
	}

	//verifica se place è ancora assegnato ad altri visitType
	private static boolean isPlaceAssigned(VisitTypeList visitTypeList, Place place) {
		for(VisitType visitType : visitTypeList.getAllVisitTypes()) {
			if(visitType.getPlace().equals(place))
				return true;
		}
		return false;
	}

	//gestisce la coerenza dei dati dopo la rimozione del luogo
	public static void cleanUpRemovedPlace(VisitTypeList visitTypeList, VolunteerList volunteerList, Place removedPlace) {
		List<VisitType> removedVisitType = new ArrayList<>();
		Set<Volunteer> removedVolunteer = new HashSet<>();

		for(VisitType visitType : visitTypeList.getAllVisitTypes()) {
			if(visitType.hasPlace(removedPlace))
				removedVisitType.add(visitType);	//selezione dei visitType da rimuovere
		}

		for(VisitType visitType : removedVisitType) {
			visitTypeList.removeVisitType(visitType);	//rimozione dei visitType
			removedVolunteer.addAll(visitType.getVolunteer());	//selezione dei volunteer di ogni visitType rimosso
		}

		for(Volunteer volunteer : removedVolunteer) {	//verifica se il volunteer è associato ad altri visitType
			if(!isVolunteerAssigned(visitTypeList, volunteer))	//se non è associato ad altri visitType --> rimozione
				volunteerList.removeVolunteer(volunteer);
		}

	}

	//gestisce la coerenza dei dati dopo la rimozione del tipo visita
	public static void cleanUpRemovedVisitType(VisitTypeList visitTypeList, PlaceList placeList, VolunteerList volunteerList, VisitType removedVisitType) {
		if(removedVisitType == null)
			return;

		Place placeOfVisitType = removedVisitType.getPlace();

		if(!isPlaceAssigned(visitTypeList, placeOfVisitType))	//se il luogo non è associato ad altri visitType --> rimozione
			placeList.removePlace(placeOfVisitType);

		for(Volunteer volunteer : removedVisitType.getVolunteer()) {	//verifica se il volontario è associato ad altri visitType
			if(!isVolunteerAssigned(visitTypeList, volunteer))	//se non è associato ad altri visitType --> rimozione
				volunteerList.removeVolunteer(volunteer);
		}
	}

	//gestisce la coerenza dei dati dopo la rimozione del volontario
	public static void cleanUpRemovedVolunteer(VisitTypeList visitTypeList, PlaceList placeList, Volunteer removedVolunteer) {
		List<VisitType> removedVisitType = new ArrayList<>();
		Set<Place> removedPlace = new HashSet<>();

		for(VisitType visitType : visitTypeList.getAllVisitTypes()) {
			visitType.removeVolunteer(removedVolunteer);

			if(visitType.getVolunteer().isEmpty())	//se il tipo visita non ha più volontari --> rimozione
				removedVisitType.add(visitType);
			else									//il tipo visita ha ancora volontari, ho tolto quello rimosso e aggiorno
				visitTypeList.modifyVisitType(visitType);
		}

		for(VisitType visitType : removedVisitType) {
			visitTypeList.removeVisitType(visitType);	//rimozione dei tipi visita selezionati precedentemente
			removedPlace.add(visitType.getPlace());		//rimuovo il tipo visita --> gestisco il luogo
		}

		for(Place place : removedPlace) {
			if(!isPlaceAssigned(visitTypeList,place))	//se il luogo non è associato ad altri tipi visita --> rimuovo
				placeList.removePlace(place);
		}
	}

	//gestisce la coerenza dei dati dopo l'aggiornamento delle disponibilità del volontario, aggiornandole in visit type
	public static void updateAvailabilityInVisitType(VolunteerList volunteerList, VisitTypeList visitTypeList) {
		for(VisitType visitType : visitTypeList.getAllVisitTypes()) {
			for(Volunteer volElem : visitType.getVolunteer()) {
				Volunteer volList = volunteerList.findBy(volElem);
				visitTypeList.updateAvailability(volList);
			}
		}
	}

	//gestisce la coerenza dei dati dopo l'aggiornamento della password del volontario, aggiornandola anche in visit type
	public static void updatePasswordVolunteer(Volunteer volunteer, RepositorySystem repositoryVisitType) {
		VisitTypeList visitTypeList = new VisitTypeList(repositoryVisitType);

		for(VisitType visitType : visitTypeList.getAllVisitTypes()) {
			List<Volunteer> vol = visitType.getVolunteer();
			for(int i = 0; i < vol.size(); i++) {
				if(vol.get(i).equals(volunteer)) {
					vol.set(i, volunteer);
					repositoryVisitType.modifyElement("title", visitType.getTitle(), visitType);
					return;
				}
			}
		}
	}

	//gestisce lo stato delle visite all'interno delle iscrizioni del fruitore
	public static void updateRegistrationState(Visit visit, RepositorySystem repositoryRegistration) {
		RegistrationList registrationList = new RegistrationList(repositoryRegistration);

		for(Registration registration : registrationList.getRegistrationList()) {
			if(registration.getVisit().getCounterId() == visit.getCounterId()) {
				registration.setVisit(visit);
				repositoryRegistration.modifyElement("code", registration.getCode(), registration);
			}
		}
	}
	
	public static void startControl(RepositorySystem repositoryStateAvailability) {
		if(LocalDate.now().getDayOfMonth()!=16) {
			repositoryStateAvailability.modifyObject("availabilityStatus", "false", String.valueOf(true));
		}
	}
}
