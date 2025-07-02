package utilities;

import java.time.LocalDate;

public class TimeSimulator {

	private LocalDate currentDate;

	public TimeSimulator(LocalDate currentDate) {
		this.currentDate = currentDate;
	}

	public LocalDate getCurrentDate() {
		return this.currentDate;
	}

	public int getNumberOfDay() {
		return this.currentDate.getDayOfMonth();
	}

	//metodo che incrementa di un giorno la data
	public void newDay() {
		this.currentDate = this.currentDate.plusDays(1);
	}

	//metodo che incrementa di n giorni la data
	public void newDay(int numberOfDays) {
		this.currentDate = this.currentDate.plusDays(numberOfDays);
	}
	
	/* ++++++++++++++++++ METODO  DI AVANZAMENTO DEI GIORNI  ++++++++++++++++++
	 Questo metodo avanza la data di un numero specificato di giorni
	 -> se il giorno raggiunge il 16 del mese ritorna true altrimenti ritorna false
	 */
	public boolean advance(int number) {

		boolean generate = false; 
		
		for(int i = 0; i < number; i++) {
			this.newDay();
			if(this.getNumberOfDay() == 16) {
				generate = true;
				return generate;	
			}
				
		}
		return generate;
	}
}
