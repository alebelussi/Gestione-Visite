package program.users;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import utilities.*;

public class VolunteerList {
	private Map<String, Volunteer> volunteerList;
	private JsonManager jsonManager;
	
	public VolunteerList(String path) {	
		this.volunteerList = new HashMap<>();
		this.jsonManager = new JsonManager(path);
		loadVolunteer();
	}
	
	private void loadVolunteer() {
		List<Volunteer> list = jsonManager.loadData(Volunteer.class);
		for(Volunteer volontario : list) {
			volunteerList.put(volontario.getNickname(), volontario);
		}
	}
	
	public void addVolunteer(Volunteer volunteer) {
		this.volunteerList.put(volunteer.getNickname(), volunteer);
		this.jsonManager.addElement(Volunteer.class, volunteer);
	}
	public void addAvailability(Volunteer volunteer, ArrayList<LocalDate> dateAvailability) {
		volunteerList.get(volunteer.getNickname()).setDateAvailability(dateAvailability);
		this.jsonManager.modifyElement("nickname", volunteer.getNickname(), volunteerList.get(volunteer.getNickname()));
	}
	
	public void viewVolunteer() {
		loadVolunteer();
		this.jsonManager.viewData(Volunteer.class);
	}
	
	public String viewVolunteerAvailability(Volunteer volunteer) {
		return volunteerList.get(volunteer.getNickname()).viewDateAvailability();
	}
	
	public void removeAvailability(Volunteer volunteer, LocalDate date) {
		Volunteer vol = volunteerList.get(volunteer.getNickname());
		TreeSet<LocalDate> dateList = vol.getDateAvailability();
		dateList.removeIf(dateToRemove -> dateToRemove.equals(date));
		vol.modifyAvailability(dateList);
		this.jsonManager.modifyElement("nickname", volunteer.getNickname(), vol);
	}
	
	public void modifyVolunteer(Volunteer volunteer) {
		this.jsonManager.modifyElement("Nickname", volunteer.getNickname(), volunteer);
	}
	
	public boolean findVolunteer(String nickname) {
		return this.volunteerList.containsKey(nickname);
	}
	
	public Volunteer getVolunteer(String nickname) {
		return this.volunteerList.get(nickname);
	}
	
	public TreeSet<LocalDate> getDateAvailability(Volunteer volunteer){
		return this.volunteerList.get(volunteer.getNickname()).getDateAvailability();
	}
	
	public void removeVolunteer(Volunteer volunteer) {
		this.jsonManager.removeElement(Volunteer.class, volunteer);
		this.volunteerList.remove(volunteer.getNickname());
	}
	
	public boolean isEmpty() {
		return this.volunteerList.isEmpty();
	}
	
}
