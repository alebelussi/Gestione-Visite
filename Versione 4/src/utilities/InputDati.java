package utilities;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class InputDati
{
	  private static Scanner lettore = creaScanner();

	  private final static String ERRORE_FORMATO = "Attenzione: il dato inserito non e' nel formato corretto";
	  private final static String ERRORE_MINIMO= "Attenzione: e' richiesto un valore maggiore o uguale a ";
	  private final static String ERRORE_STRINGA_VUOTA= "Attenzione: non hai inserito alcun carattere";
	  private final static String ERRORE_MASSIMO= "Attenzione: e' richiesto un valore minore o uguale a ";
	  private final static String MESSAGGIO_AMMISSIBILI= "Attenzione: i caratteri ammissibili sono: ";

	  private final static char RISPOSTA_SI='S';
	  private final static char RISPOSTA_NO='N';



	  private static Scanner creaScanner () {
	   Scanner creato = new Scanner(System.in);
	   creato.useDelimiter(System.getProperty("line.separator"));
	   return creato;
	  }

	  public static String leggiStringa (String messaggio) {
		  System.out.print(messaggio);
		  return lettore.next();
	  }

	  public static String leggiStringaNonVuota(String messaggio) {
	   boolean finito=false;
	   String lettura = null;
	   do {
		 lettura = leggiStringa(messaggio);
		 lettura = lettura.trim();
		 if(lettura.length() > 0)
		  finito=true;
		 else
		  System.out.println(ERRORE_STRINGA_VUOTA);
	   } while (!finito);

	   return lettura;
	  }

	  public static String leggiDataStringa(String messaggio) {
		  String lettura;
		  boolean finito = false;

		  do {
			  lettura = leggiStringa(messaggio);

			  try {
				  LocalDate date = LocalDate.parse(lettura);
				  finito = true;
			  }catch(DateTimeParseException e) {
				  System.out.println("Errore...formato della data non valido!");
				  finito = false;
			  }

		  }while(!finito);

		  return lettura;
	  }

	  public static DayOfWeek leggiGiornoSettimana(String messaggio) {
		  DayOfWeek day = null;
		  boolean finito = false;

		  do {
			  String lettura = leggiStringa(messaggio);

			  try {
				  day = DayOfWeek.valueOf(lettura.toUpperCase());
				  finito = true;
			  }catch(IllegalArgumentException e) {
				  System.out.println("Errore...giorno non corretto!");
				  finito = false;
			  }

		  }while(!finito);

		  return day;

	  }

	  public static String leggiOrarioStringa(String messaggio) {
		  String lettura;
		  boolean finito = false;

		  do {
			  lettura = leggiStringa(messaggio);

			  try {
				  LocalTime time = LocalTime.parse(lettura);
				  finito = true;
			  }catch(DateTimeParseException e) {
				  System.out.println("Errore...formato dell'orario non corretto!");
				  finito = false;
			  }
		  }while(!finito);
		  return lettura;
	  }

	  public static char leggiChar (String messaggio) {
	   boolean finito = false;
	   char valoreLetto = '\0';
	   do {
	     System.out.print(messaggio);
	     String lettura = lettore.next();
	     if (lettura.length() > 0) {
	       valoreLetto = lettura.charAt(0);
	       finito = true;
	     }
	     else {
	       System.out.println(ERRORE_STRINGA_VUOTA);
	      }
	    }while(!finito);
	   return valoreLetto;
	  }

	  public static char leggiUpperChar (String messaggio, String ammissibili) {
	   boolean finito = false;
	   char valoreLetto = '\0';
	   do {
	    valoreLetto = leggiChar(messaggio);
	    valoreLetto = Character.toUpperCase(valoreLetto);
	    if (ammissibili.indexOf(valoreLetto) != -1)
		 finito  = true;
	    else
	     System.out.println(MESSAGGIO_AMMISSIBILI + ammissibili);
	   }while (!finito);
	   return valoreLetto;
	  }


	  public static int leggiIntero (String messaggio) {
	   boolean finito = false;
	   int valoreLetto = 0;
	   do {
	     System.out.print(messaggio);
	     try
	      {
	       valoreLetto = lettore.nextInt();
	       finito = true;
	      }
	     catch (InputMismatchException e)
	      {
	       System.out.println(ERRORE_FORMATO);
	       String daButtare = lettore.next();
	      }
	    } while (!finito);
	   return valoreLetto;
	  }

	  public static int leggiInteroPositivo(String messaggio) {
		  return leggiInteroConMinimo(messaggio,1);
	  }

	  public static int leggiInteroNonNegativo(String messaggio) {
		  return leggiInteroConMinimo(messaggio,0);
	  }


	  public static int leggiInteroConMinimo(String messaggio, int minimo) {
	   boolean finito = false;
	   int valoreLetto = 0;
	   do {
	     valoreLetto = leggiIntero(messaggio);
	     if (valoreLetto >= minimo)
	      finito = true;
	     else
	      System.out.println(ERRORE_MINIMO + minimo);
	    } while (!finito);

	   return valoreLetto;
	  }

	  public static int leggiIntero(String messaggio, int minimo, int massimo) {
	   boolean finito = false;
	   int valoreLetto = 0;
	   do {
	     valoreLetto = leggiIntero(messaggio);
	     if (valoreLetto >= minimo && valoreLetto<= massimo)
	      finito = true;
	     else
	      if (valoreLetto < minimo)
	         System.out.println(ERRORE_MINIMO + minimo);
	      else
	    	 System.out.println(ERRORE_MASSIMO + massimo);
	    } while (!finito);

	   return valoreLetto;
	  }


	  public static double leggiDouble (String messaggio) {
	   boolean finito = false;
	   double valoreLetto = 0;
	   do {
	     System.out.print(messaggio);
	     try {
	       valoreLetto = lettore.nextDouble();
	       finito = true;
	     }catch (InputMismatchException e) {
	       System.out.println(ERRORE_FORMATO);
	       String daButtare = lettore.next();
	      }
	    } while (!finito);
	   return valoreLetto;
	  }

	  public static double leggiDoubleConMinimo (String messaggio, double minimo) {
	   boolean finito = false;
	   double valoreLetto = 0;
	   do {
	     valoreLetto = leggiDouble(messaggio);
	     if (valoreLetto >= minimo)
	      finito = true;
	     else
	      System.out.println(ERRORE_MINIMO + minimo);
	    }while(!finito);

	   return valoreLetto;
	  }


	  public static boolean yesOrNo(String messaggio) {
		  String mioMessaggio = messaggio + "("+RISPOSTA_SI+"/"+RISPOSTA_NO+") ";
		  char valoreLetto = leggiUpperChar(mioMessaggio,String.valueOf(RISPOSTA_SI)+String.valueOf(RISPOSTA_NO));

		  if (valoreLetto == RISPOSTA_SI)
			return true;
		  else
			return false;
	  }

	  public static String leggiUpperString(String messaggio, String ammissibili) {
		  boolean finito = false;
		  String valore;

		  do {
			  valore = leggiStringaNonVuota(messaggio);
			  valore = valore.toUpperCase();
			  if(ammissibili.contains(valore))
				  finito = true;
			  else
				  System.out.println("Risposta non valida: scegli tra (SI/NO)");
		  }while(!finito);

		  return valore;
	  }

	  public static boolean yesNo(String messaggio) {
		  String mioMessaggio = messaggio + " (SI/NO) ";
		  String valoreLetto = leggiUpperString(mioMessaggio, "SI, NO");

		  if(valoreLetto.equals("SI"))
			  return true;
		  else
			  return false;
	  }

	  public static final Map<String, DayOfWeek> giorniMappa = new HashMap<>();

	    static {
	        giorniMappa.put("lunedì", DayOfWeek.MONDAY);
	        giorniMappa.put("martedì", DayOfWeek.TUESDAY);
	        giorniMappa.put("mercoledì", DayOfWeek.WEDNESDAY);
	        giorniMappa.put("giovedì", DayOfWeek.THURSDAY);
	        giorniMappa.put("venerdì", DayOfWeek.FRIDAY);
	        giorniMappa.put("sabato", DayOfWeek.SATURDAY);
	        giorniMappa.put("domenica", DayOfWeek.SUNDAY);
	        giorniMappa.put("lunedi", DayOfWeek.MONDAY);
	        giorniMappa.put("martedi", DayOfWeek.TUESDAY);
	        giorniMappa.put("mercoledi", DayOfWeek.WEDNESDAY);
	        giorniMappa.put("giovedi", DayOfWeek.THURSDAY);
	        giorniMappa.put("venerdi", DayOfWeek.FRIDAY);
	        giorniMappa.put("lunedi'", DayOfWeek.MONDAY);
	        giorniMappa.put("martedi'", DayOfWeek.TUESDAY);
	        giorniMappa.put("mercoledi'", DayOfWeek.WEDNESDAY);
	        giorniMappa.put("giovedi'", DayOfWeek.THURSDAY);
	        giorniMappa.put("venerdi'", DayOfWeek.FRIDAY);
	    }

	    public static DayOfWeek getDayOfWeekFromItalian(String giornoItaliano) {
	        return giorniMappa.get(giornoItaliano.toLowerCase().trim());
	    }

	    public static DayOfWeek leggiGiornoSettimanaItaliano(String messaggio) {
	        DayOfWeek day = null;

	        do {
	            String lettura = leggiStringa(messaggio);

	            day = getDayOfWeekFromItalian(lettura);

	            if (day == null) {
	                System.out.println("Errore: Giorno non corretto. Riprova.");
	            }

	        } while (day == null);

	        return day;
	    }
}
