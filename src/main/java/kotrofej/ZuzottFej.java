package kotrofej;

import terkep.*;

/**
 * A ZuzottFej zúzalék szórásával biztosítja a tapadást a jeges utakon.
 */
public class ZuzottFej extends KotroFej {
    
    private int zuzalekIgeny;

    //KONSTRUKTOR
    public ZuzottFej(int ar, int zuzalekIgeny) {
        super(ar);
        this.zuzalekIgeny = Math.max(0, zuzalekIgeny);
    }

    //GETTEREK
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

    /**
     * A tisztit metódus felelős a zúzottkő szórásáért a megadott sávban.
     * Ezáltal javítja a tapadást és csökkenti a balesetek kockázatát a jeges útszakaszokon.
     */
    @Override
    public void tisztit(Sav cel, Sav melle, Ut ut) {
        //Ellenőrizzük, hogy a cél sáv nem null-e
        if (cel != null) {
            System.out.println(">>> [HÓKOTRÓ AKCIÓ] Zúzottkő-szóró bekapcsolva a(z) " + cel.getSavSzama() + ". sávban...");
            
            //Sáv zúzalékos állapotának beállítása
            cel.setZuzalekos(true);
            
            System.out.println("    >>> [SIKER] Zúzalék kiszórva! A járművek ezen a szakaszon már nem csúsznak meg a jégen.");
        }
    }

}