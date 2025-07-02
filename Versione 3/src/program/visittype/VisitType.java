package program.visittype;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;

import program.places.*;
import program.users.Volunteer;

public class VisitType {
	
	private String title; 
	private String description; 
	private String meetingPoint;
	private String startDate; 
	private String endDate; 
	
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private ArrayList<DayOfWeek> day;
	
	private String startHour;
	private int duration;
	private boolean buyTicket;
	private int minParticipant;
	private int maxParticipant;
	
	private Place place; 
	
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private ArrayList<Volunteer> volunteer;
	
	public VisitType() {};
	
	public VisitType(String title, String description, String meetingPoint, String startDate, String endDate, 
			ArrayList<DayOfWeek> day, String startHour, int duration, boolean buyTicket, int minParticipant, 
			int maxParticipant, Place place, ArrayList<Volunteer> volunteer) {
		this.title = title;
		this.description = description;
		this.meetingPoint = meetingPoint; 
		this.startDate = startDate;
		this.endDate = endDate;
		this.day = day;
		this.startHour = startHour;
		this.duration = duration;
		this.buyTicket = buyTicket;
		this.minParticipant = minParticipant;
		this.maxParticipant = maxParticipant;
		this.place = place; 
		this.volunteer = volunteer; 
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMeetingPoint() {
		return meetingPoint;
	}

	public void setMeetingPoint(String meetingPoint) {
		this.meetingPoint = meetingPoint;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public ArrayList<DayOfWeek> getDay() {
		return day;
	}

	public void setDay(ArrayList<DayOfWeek> day) {
		this.day = day;
	}

	public String getStartHour() {
		return startHour;
	}

	public void setStartHour(String startHour) {
		this.startHour = startHour;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public boolean isBuyTicket() {
		return buyTicket;
	}

	public void setBuyTicket(boolean buyTicket) {
		this.buyTicket = buyTicket;
	}

	public int getMinParticipant() {
		return minParticipant;
	}

	public void setMinParticipant(int minParticipant) {
		this.minParticipant = minParticipant;
	}

	public int getMaxParticipant() {
		return maxParticipant;
	}

	public void setMaxParticipant(int maxParticipant) {
		this.maxParticipant = maxParticipant;
	}
	
	public Place getPlace() {
		return this.place;
	}
	
	public void setPlace(Place place) {
		this.place = place;
	}
	
	public ArrayList<Volunteer> getVolunteer() {
		return this.volunteer;
	}
	
	public void setVolunteer(ArrayList<Volunteer> volunteer) {
		this.volunteer = volunteer; 
	}
	
	public void updateVolunteer(Volunteer vol) {
		for(int i = 0; i < this.volunteer.size(); i++) {
			Volunteer volElem = this.volunteer.get(i);
			if(volElem.getNickname().equals(vol.getNickname())) {
				this.volunteer.set(i, vol);
				break;
			}
				
		}
	}
	
	public void removeAllVolunteer() {
		this.volunteer.clear();
	}
	
	@Override
	public String toString() {
		return "  Titolo: " + getTitle() + "\n  Descrizione: " + getDescription() + "\n  Punto d'incontro: " + getMeetingPoint()
				+ "\n  Data d'inizio: " + getStartDate() + "  Data di fine: " + getEndDate() + "  Giorno: " + getDay() + "  Ora d'inizio: " + getStartHour()
				+ "  Durata: " + getDuration() + "\n  Necessario comprare il biglietto? " + (isBuyTicket() ? "Si" : "No") + "  Partecipanti minimi: " 
				+ getMinParticipant() + " Partecipanti massimi: " + getMaxParticipant() + " Luogo associato: " + getPlace().toString() 
				+ "  Volontario associato: " + getVolunteer();
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o)
			return true;
		if(o == null || o.getClass() != getClass())
			return false;
			
		VisitType visitType = (VisitType) o;
		return Objects.equals(title, visitType.getTitle());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(title);
	}
	
	//metodo per aggiungere nuovi volontari
	public void addVolunteer(ArrayList<Volunteer> volunteers) {
		for (Volunteer newVol : volunteers) {
			if(!this.volunteer.contains(newVol)) //se non già presente => lo aggiungo
				this.volunteer.add(newVol);
		}
	}
	
}
