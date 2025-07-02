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
	
	/* ++++++++++++++++++ METODO  DI AVANZAMENTO DEI GIORNI  ++++++++++++++++++
	 Questo metodo avanza la data di un numero specificato di giorni
	 */
	public void advance(int number) {
		this.currentDate = this.currentDate.plusDays(number);
	}
}
