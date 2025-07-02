package program.users.volunteer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import utilities.RepositorySystem;

public class VolunteerList {
	private Map<String, Volunteer> volunteerList;
	private RepositorySystem repositoryVolunteerSystem;

	public VolunteerList(RepositorySystem repositoryVolunteerSystem) {
		this.volunteerList = new HashMap<>();
		this.repositoryVolunteerSystem = repositoryVolunteerSystem;
		loadVolunteer();
	}

	private void loadVolunteer() {
		List<Volunteer> list = repositoryVolunteerSystem.loadData(Volunteer.class);
		for(Volunteer volontario : list) {
			volunteerList.put(volontario.getNickname(), volontario);
		}
	}
	
	public Volunteer getVolunteer(String nickname) {
		return this.volunteerList.get(nickname);
	}
	
	public Map<String, Volunteer> getVolunteerList() {
		return this.volunteerList;
	}

	public void addVolunteer(Volunteer volunteer) {
		this.volunteerList.put(volunteer.getNickname(), volunteer);
		this.repositoryVolunteerSystem.addElement(Volunteer.class, volunteer);
	}
	
	public void addAvailability(Volunteer volunteer, ArrayList<LocalDate> dateAvailability) {
		volunteerList.get(volunteer.getNickname()).setDateAvailability(dateAvailability);
		this.repositoryVolunteerSystem.modifyElement("nickname", volunteer.getNickname(), volunteerList.get(volunteer.getNickname()));
	}

	public void removeAvailability(Volunteer volunteer, LocalDate date) {
		Volunteer vol = volunteerList.get(volunteer.getNickname());
		TreeSet<LocalDate> dateList = vol.getDateAvailability();
		dateList.removeIf(dateToRemove -> dateToRemove.equals(date));
		vol.modifyAvailability(dateList);
		this.repositoryVolunteerSystem.modifyElement("nickname", volunteer.getNickname(), vol);
	}

	public void modifyVolunteer(Volunteer volunteer) {
		this.repositoryVolunteerSystem.modifyElement("Nickname", volunteer.getNickname(), volunteer);
	}

	public boolean findVolunteer(String nickname) {
		return this.volunteerList.containsKey(nickname);
	}

	public TreeSet<LocalDate> getDateAvailability(Volunteer volunteer){
		return this.volunteerList.get(volunteer.getNickname()).getDateAvailability();
	}

	public void removeVolunteer(Volunteer volunteer) {
		this.repositoryVolunteerSystem.removeElement(Volunteer.class, volunteer);
		this.volunteerList.remove(volunteer.getNickname());
	}

	public boolean isEmpty() {
		return this.volunteerList.isEmpty();
	}
	
	public Volunteer findBy(Volunteer volunteer) {
	    return this.volunteerList.get(volunteer.getNickname());
	}
	
	public List<Volunteer> getAllVolunteers() {
		return new ArrayList<>(volunteerList.values());
	}

}
