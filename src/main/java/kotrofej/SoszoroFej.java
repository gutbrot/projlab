package kotrofej;

import terkep.*;

/**
 * A SoszoroFej só szórásával indítja el a hó és jég olvadását.
 */
public class SoszoroFej extends KotroFej {
    
    private int solgeny;

    //KONSTRUKTOR
    public SoszoroFej(int ar, int solgeny) {
        super(ar);
        this.solgeny = Math.max(0, solgeny);
    }

    //GETTEREK
    @Override
    public String getNev() { 
        return "SoszoroFej"; 
    }

    public int getSolgeny() { 
        return solgeny; 
    }

    @Override
    public KotroFej getKotroFej() { 
        return new SoszoroFej(getAr(), solgeny); 
    }

    /**
     * A tisztit metódusban a sószórófej bekapcsol, és a megadott sávban elindítja a hó és jég olvadását.
     * A sáv logikája dönti el, hogy ez milyen fizikai hatással jár (pl. csúszás, hó eltávolítása).
     */
    @Override
    public void tisztit(Sav cel, Sav melle, Ut ut) {
        if (cel != null) {
            System.out.println(">>> [HÓKOTRÓ AKCIÓ] Sószórófej bekapcsolva a(z) " + cel.getSavSzama() + ". sávban...");
            
            //Sózás indítása a sávban (A sáv logikája dönti el a fizikai hatást)
            cel.soOlvadas();
            
            System.out.println("    >>> [SIKER] Só kiszórva! Az olvadási folyamat megkezdődött.");
        }
    }
}