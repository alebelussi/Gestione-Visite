package program.users;

import java.util.Objects;

public class Volunteer extends Person {
	
	private String nickname; 
	private String password;
	private boolean availability = true; //disponibilità dei volontari per il mese i+1 (VERSIONE 1= sempre)
	
	public Volunteer() {}; //necessario per Jackson
		
	public Volunteer(String nome, String cognome, String nickname, String password, boolean availability) {
		super(nome, cognome);
		this.nickname = nickname;
		this.password = password;
		this.availability = availability;
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
	
	public boolean getAvailability() {
		return this.availability;
	}
	
	public void setAvailability(boolean availability) {
		this.availability = availability;
	}
	
	@Override	
	public String viewPerson() {
		StringBuffer descrizioneVolontario = new StringBuffer();
		descrizioneVolontario.append("Nome "+getName()+"\nCognome"+getSurname()+"\nNickname "+getNickname());
		return descrizioneVolontario.toString();
	}
	
	@Override
	public String toString() {	
		return "Nome: " + getName() + " Cognome: " + getSurname() + " Nickname: " + getNickname() + " Disponibile: "
				+ (getAvailability() ? "SI" : "NO"); 
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
}

