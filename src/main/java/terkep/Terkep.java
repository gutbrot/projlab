package terkep;

import java.util.List;
public class Terkep {
    private List<Ut> utak;

    public void idojarasFrissites(){
        for (Ut ut : utak) {
            ut.havazik(5);
        }
    }
}

