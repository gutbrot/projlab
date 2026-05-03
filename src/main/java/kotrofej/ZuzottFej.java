package kotrofej;

import terkep.*;

/**
 * A ZuzottFej zúzalék szórásával biztosítja a tapadást a jeges utakon.
 */
public class ZuzottFej extends KotroFej {
    
    private int zuzalekIgeny;

    public ZuzottFej(int ar, int zuzalekIgeny) {
        super(ar);
        this.zuzalekIgeny = Math.max(0, zuzalekIgeny);
    }

    @Override
    public void tisztit(Sav cel, Sav melle, Ut ut) {
        if (cel != null) {
            System.out.println(">>> [HÓKOTRÓ AKCIÓ] Zúzottkő-szóró bekapcsolva a(z) " + cel.getSavSzama() + ". sávban...");
            
            // Sáv zúzalékos állapotának beállítása
            cel.setZuzalekos(true);
            
            System.out.println("    >>> [SIKER] Zúzalék kiszórva! A járművek ezen a szakaszon már nem csúsznak meg a jégen.");
        }
    }

    @Override
    public String getNev() { 
        return "ZuzottFej"; 
    }

    public int getZuzalekIgeny() { 
        return zuzalekIgeny; 
    }

    @Override
    public KotroFej getKotroFej() { 
        return new ZuzottFej(getAr(), zuzalekIgeny); 
    }
}