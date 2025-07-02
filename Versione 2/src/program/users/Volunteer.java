package program.users;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import java.util.TreeSet;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Volunteer extends Person {
	
	private String nickname; 
	private String password;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private TreeSet<LocalDate> dateAvailability = new TreeSet<>(); 
	
	public Volunteer() {}; //necessario per Jackson
		
	public Volunteer(String name, String surname, String nickname, String password) {
		super(name, surname);
		this.nickname = nickname;
		this.password = password;
		this.dateAvailability = new TreeSet<>();
	}
	
	@Override
	public String getName() {
		return super.getName();
	}
	
	@Override
	public String getSurname() {
		return super.getSurname();
	}
	
	public String getNickname() {
		return this.nickname;
	}
	
	public void setPassowrd(String password) {
		this.password = password;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public TreeSet<LocalDate> getDateAvailability() {
		return this.dateAvailability;
	}

	public void setDateAvailability(ArrayList<LocalDate> dateAvailability) {
		for(LocalDate date : dateAvailability) {
			this.dateAvailability.add(date);
		}
	    
	}
	
	public void modifyAvailability(TreeSet<LocalDate> dateAvailability) {
		this.dateAvailability = new TreeSet<>(dateAvailability);
	}
	
	public String viewDateAvailability() {
		return this.getDateAvailability().toString();
	}

	@Override	
	public String viewPerson() {
		StringBuffer descrizioneVolontario = new StringBuffer();
		descrizioneVolontario.append("Nome "+getName()+"\nCognome"+getSurname()+"\nNickname "+getNickname());
		return descrizioneVolontario.toString();
	}
	
	@Override
	public String toString() {	
		return "Nome: " + getName() + " Cognome: " + getSurname() + " Nickname: " + getNickname() + " Disponibile: " + getDateAvailability(); 
	}
	
	@Override	
    public boolean equals(Object obj) {	
        if (this == obj) return true;
        	if (obj == null || getClass() != obj.getClass()) return false;
        	Volunteer volunteer = (Volunteer) obj;
        return Objects.equals(nickname, volunteer.nickname); // Confronto basato sul nickname
    }

    @Override
    public int hashCode() {	
        return Objects.hash(nickname);
    }
    
    @Override
	public String returnRole() {
		return "Volunteer"; 
	}
}

