package eszkoztar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kotrofej.KotroFej;

public class Eszkoztar {
    private int soKeszlet;
    private int biokerozinKeszlet;
    private List<KotroFej> kotroFejek = new ArrayList<>();

    public boolean levon(String tipus, int mennyiseg){
    	if (mennyiseg < 0) return false;
    	switch (tipus.toLowerCase()) {
    	    case "so":
    	        if (soKeszlet >= mennyiseg) { soKeszlet -= mennyiseg; return true; }
    	        return false;
    	    case "biokerozin":
    	        if (biokerozinKeszlet >= mennyiseg) { biokerozinKeszlet -= mennyiseg; return true; }
    	        return false;
    	    default:
    	        return false;
    	}
    }
    public void hozzaad(String tipus, int mennyiseg){
    	if (mennyiseg < 0) return;
    	switch (tipus.toLowerCase()) {
    	    case "so": soKeszlet += mennyiseg; break;
    	    case "biokerozin": biokerozinKeszlet += mennyiseg; break;
    	    default: break;
    	}
    }
    
    public boolean vanE(String tipus, int mennyiseg){
    	if (mennyiseg < 0) return false;
    	switch (tipus.toLowerCase()) {
    	    case "so": return soKeszlet >= mennyiseg;
    	    case "biokerozin": return biokerozinKeszlet >= mennyiseg;
    	    default: return false;
    	}
    }
    
    public void hozzaadFej(KotroFej fej) {
        if (fej != null) kotroFejek.add(fej);
    }
    
    public KotroFej kiveszFej() {
        if (kotroFejek.isEmpty()) return null;
        return kotroFejek.remove(0);
    }

    public List<KotroFej> getKotroFejek() {
        return Collections.unmodifiableList(kotroFejek);
    }
    
    public int getSoKeszlet() { return soKeszlet; }
    public int getBiokerozinKeszlet() { return biokerozinKeszlet; }
}
