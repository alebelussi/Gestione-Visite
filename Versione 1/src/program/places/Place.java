package program.places;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Place {
	
	private String name; 
	private String description; 
	private String address; 
	private static String region = "null";
	
	public Place() {}; 
	/* --- Motivo del costruttore vuoto ---
	 * necessario per Jackson in quanto crea un'istanza dell'oggetto 
	 * e poi la istanzia 
	 */
	
	public Place(String name, String description, String address, String region) {
		this.name = name;
		this.description = description; 
		this.address = address; 
		Place.region = region;
	}
	
	public Place(String name, String description, String address) {
		this.name = name;
		this.description = description; 
		this.address = address; 
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	@JsonGetter("region")
	public static String getRegion() {
		return region;
	}
	
	@JsonSetter("region")
	public static void setRegion(String region) {
		if(Place.region == "null")
			Place.region = region;
	} 
	
	@Override
	public String toString() {
		return "  Nome: " + getName() + "\n  Descrizione: "+ getDescription() +"\n  Indirizzo: " + getAddress()
				+"\n  Ambito Territoriale: " + getRegion() + "\n";
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o)
			return true;
		if(o == null || o.getClass() != getClass())
			return false;
			
		Place luogo = (Place) o;
		return Objects.equals(name, luogo.getName());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

}
