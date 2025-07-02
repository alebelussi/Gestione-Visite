package program.registration;

import java.util.Objects;

import program.users.User;
import program.visitistance.Visit;

public class Registration {

	private int totalRegistrations;
	private String code;
	private Visit visit;
	private User user;

	public Registration() {}

	public Registration(int totalRegistration, String code, Visit visit, User user) {
		this.totalRegistrations = totalRegistration;
		this.code = code;
		this.visit = visit;
		this.user = user;
	}

	public int getTotalRegistrations() {
		return totalRegistrations;
	}

	public void setTotalRegistrations(int totalRegistrations) {
		this.totalRegistrations = totalRegistrations;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Visit getVisit() {
		return visit;
	}

	public void setVisit(Visit visit) {
		this.visit = visit;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return this.user;
	}

	@Override
	public boolean equals(Object o) {
	    if (this == o) return true;
	    if (o == null || getClass() != o.getClass()) return false;
	    Registration that = (Registration) o;
	    return Objects.equals(code, that.code); // usa java.util.Objects
	}

	@Override
	public int hashCode() {
	    return Objects.hash(code); // usa java.util.Objects
	}

	@Override
	public String toString() {
		return "\n Codice Prenotazione: " + getCode() + "\n Nome Visita: " + getVisit().getVisitType().getTitle()
				+ "\n Numero di persone: " + getTotalRegistrations() + "\n Fruitore: " + getUser().getUsername();
	}

}
