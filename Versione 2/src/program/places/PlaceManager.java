package program.places;

import java.util.ArrayList;
import java.util.Arrays;

import utilities.*;

public class PlaceManager {

	private static final String MSG_CONFERMA_AMBITO = "Vuoi inserire l'ambito territoriale? ";
	private static final String MSG_INSERIMENTO_AMBITO_TERRITORIALE = "Inserisci l'ambito territoriale: ";
	private static final String MSG_MODIFICA_ANNULLATA = "Modifica annullata!";
	private static final String MSG_MODIFICA_CONFERMATA = "Modifica completata con successo!";
	private static final String MSG_CONFERMA_MODIFICA = "Vuoi modificare i dati? ";
	private static final String MSG_INSERIMENTO_CONFERMATO = "Inserimento completato con successo!";
	private static final String MSG_INSERIMENTO_NDIRIZZO_LUOGO = "Inserisci l'indirizzo del luogo: ";
	private static final String MSG_INSERIMENTO_DESCRIZIONE_LUOGO = "Inserisci la descrizione del luogo: ";
	private static final String MSG_ERRORE_LUOGO_NON_PRESENTE = "Errore...luogo non presente!";
	private static final String MSG_ERRORE_NOME_NON_UNIVOCO = "Errore...luogo già presente!";
	private static final String MSG_CONFERMA_INSERIMENTO = "Vuoi inserire i dati? ";
	private static final String MSG_INSERIMENTO_LUOGO = "Inserisci il nome del luogo: ";
	private static final String MSG_INS_VOCI_DA_MODIFICARE="Inserisci quale delle seguenti voci vuoi modificare: ";
	private static final ArrayList<String> VOCI_MENU_MODIFICA = new ArrayList<>(Arrays.asList(
		    "Descrizione",
		    "Indirizzo",
		    "Annulla"
		));
	
	
	private static final String FILE_LUOGHI = "json/luoghi.json";
	private PlaceList placeList;
	
	public PlaceManager() {
		this.placeList= new PlaceList(FILE_LUOGHI);
	}

	/*@
		requires placeList != null;
		requires Place.GetRegion() != null;
	 	
	 	ensures \result != null;
	 	ensures placeList.findPlace(\result.getName());
	 	ensures placeList.size() == \old(placeList.size()) + 1;
	 @*/
	public Place add() {
		String name, description, address; 
		
		do {
			do {
				name = InputDati.leggiStringaNonVuota(MSG_INSERIMENTO_LUOGO);
				
				if(placeList.findPlace(name))
					System.out.println(MSG_ERRORE_NOME_NON_UNIVOCO);
				
			}while(placeList.findPlace(name));
			
			description = InputDati.leggiStringa(MSG_INSERIMENTO_DESCRIZIONE_LUOGO);
			address = InputDati.leggiStringa(MSG_INSERIMENTO_NDIRIZZO_LUOGO);
	
		}while(!InputDati.yesNo(MSG_CONFERMA_INSERIMENTO));
		Place placeAdd= new Place(name, description, address, Place.getRegion());
		placeList.addPlace(placeAdd);
		System.out.println(MSG_INSERIMENTO_CONFERMATO);
		return placeAdd;
	}
	
	public void view() {
		this.placeList.viewPlace();
	}
	
	/*@
		requires placeList != null
		
		invariant placeList.size() == \old(placeList.size()); 
		
		assignable placeSearched.description, placeSearched.address;
		
		ensures placeList.findPlace(placeSearched.getName());
	 @*/
	public void modify() {
		String name; 
		String confirmInsert = "";
		Place placeSearched;
		
		do {
			do {
				name = InputDati.leggiStringaNonVuota(MSG_INSERIMENTO_LUOGO);
				placeSearched= placeList.searchPlace(name);
				if(placeSearched==null)
					System.out.println(MSG_ERRORE_LUOGO_NON_PRESENTE);
			}while(placeSearched==null);
			
			MenuLayout menu= new MenuLayout(MSG_INS_VOCI_DA_MODIFICARE, VOCI_MENU_MODIFICA);
			
			//lettura campi che si vogliono modificare
			int opz= InputDati.leggiIntero(menu.visualizzaMenu(), 0, menu.getNumVoci()-1);
			
			//selezione operazione di modifica
			switch (opz) {
				case 1: { //MODIFICO LA DESCRIZIONE
					placeSearched.setDescription(InputDati.leggiStringa(MSG_INSERIMENTO_DESCRIZIONE_LUOGO));
					break;
				}
				
				case 2: { //MODIFICO L'INDIRIZZO
					placeSearched.setAddress(InputDati.leggiStringa(MSG_INSERIMENTO_NDIRIZZO_LUOGO));
					break;
				}
				
				default: //ANNULLA MODIFICA
					confirmInsert="NOT";
					break;
			}
			
			if(!confirmInsert.equals("NOT")) {
				confirmInsert = InputDati.leggiStringa(MSG_CONFERMA_MODIFICA);
			
				if(confirmInsert.equalsIgnoreCase("SI")) {
					placeList.modifyPlace(placeSearched);
					System.out.println(MSG_MODIFICA_CONFERMATA);
				}
			}else
				System.out.println(MSG_MODIFICA_ANNULLATA);
		}while(confirmInsert.equalsIgnoreCase("NO"));
	}

	public static void setRegion() {
		String region;
		
		do {
			region = InputDati.leggiStringaNonVuota(MSG_INSERIMENTO_AMBITO_TERRITORIALE);
		}while(!InputDati.yesNo(MSG_CONFERMA_AMBITO));
		
		//operazione di inserimento dell'ambito territoriale
		JsonManager jsAmbito= new JsonManager(FILE_LUOGHI);
		//modifico l'ambito territoriale
		jsAmbito.modifyObject("region", "null", region);
		//definisco l'ambito territoriale a livello di classe Place
		Place.setRegion(region);
	}
	
	public void searchPlace() {
		String name;
		Place placeSearched;
		name = InputDati.leggiStringaNonVuota(MSG_INSERIMENTO_LUOGO);
		placeSearched=placeList.searchPlace(name);
		if(placeSearched==null)
			System.out.println(MSG_ERRORE_LUOGO_NON_PRESENTE);
		else
			System.out.println(placeSearched.toString());
	}
}

