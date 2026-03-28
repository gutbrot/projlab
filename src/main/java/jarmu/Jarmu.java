package jarmu;

import terkep.*;

public abstract class Jarmu {
    //protected Lokacio pozicio;

    public abstract void mozgas(); // A jármű mozgásának absztrakt metódusa
    public abstract void utkozos(); // A jármű ütközésének absztrakt metódusa
    
    public void csuszasKezeles(Sav s, Terkep t) { // A járművek megcsúszása esetén meghívott metódus
       
    }
}
