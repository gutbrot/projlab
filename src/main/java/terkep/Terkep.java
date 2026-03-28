package terkep;

import java.util.List;
import jarmu.Jarmu;
public class Terkep {
    private List<Ut> utak;

    public void idojarasFrissites(){
        for (Ut ut : utak) {
            ut.havazik(5);
        }
    }

    public List<Ut> getTeljesHalozat() {
        return utak;
        
    }

    public void jarmuMozgas(Jarmu j, Sav s) {
        // A jármű mozgatásáért felelős metódus
    }

}

