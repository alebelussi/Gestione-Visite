package program.users;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	public void viewVolunteer() {
		this.jsonManager.viewData(Volunteer.class);
	}
	
	public void removeVolunteer(Volunteer volunteer) {
		this.jsonManager.removeElement(Volunteer.class, volunteer);
		this.volunteerList.remove(volunteer.getNickname());
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
	
}
