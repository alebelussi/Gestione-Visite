package utilities;

import java.util.ArrayList;

public class MenuLayout {

	private String titolo;
	private ArrayList<String> voci;

	public MenuLayout(String titolo, ArrayList<String>voci){
		this.titolo = titolo;
		this.voci = voci;
	}

	public String getTitolo() {
		return titolo;
	}
	public ArrayList<String> getVoci() {
		return voci;
	}

	public String visualizzaMenu() {
	    StringBuffer visualizzaMenu = new StringBuffer();
	    visualizzaMenu.append(getTitolo() + "\n");

	    for (int i = 0; i < voci.size()-1; i++) {
	        visualizzaMenu.append((i + 1) + ")" + getVoci().get(i) + "\n");
	    }
	    visualizzaMenu.append("0"+")"+  getVoci().getLast()+"\n");

	    return visualizzaMenu.toString()+"---> ";
	}

	public int getNumVoci() {
		return voci.size();
	}
}

