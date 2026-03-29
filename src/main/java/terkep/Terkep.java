package terkep;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jarmu.*;

public class Terkep {
	private final List<Ut> utak = new ArrayList<>();
	
	public void addUt(Ut ut) {
	    if (ut != null) utak.add(ut);
	}
	
	public List<Ut> getTeljesHalozat() {
	    return Collections.unmodifiableList(utak);
	}
	
    public void idojarasFrissites(){
        for (Ut ut : utak) {
            ut.havazik(5);
        }
    }
    
    public boolean jarmuMozgatas(Jarmu jarmu, Sav sav) {
        return jarmu != null && jarmu.mozgas(sav);
    }
}

