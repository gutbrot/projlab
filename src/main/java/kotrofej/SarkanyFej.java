package kotrofej;

import terkep.*;

/**
 * A SarkanyFej biokerozin segítségével képes a havat és a jeget is egyszerre felolvasztani.
 */
public class SarkanyFej extends KotroFej {
    
    private int biokerozinIgeny;

    //KONSTRUKTOR
    public SarkanyFej(int ar, int biokerozinIgeny) {
        super(ar);
        this.biokerozinIgeny = Math.max(0, biokerozinIgeny);
    }

    //GETTEREK
    @Override
    public String getNev() { 
        return "SarkanyFej"; 
    }

    public int getBiokerozinIgeny() { 
        return biokerozinIgeny; 
    }

    @Override
    public KotroFej getKotroFej() { 
        return new SarkanyFej(getAr(), biokerozinIgeny); 
    }

    /**
     * A SarkanyFej használatakor a megadott sávban lévő havat és jeget is eltávolítja, ha van.
     * Ha a sáv már tiszta, akkor csak a biokerozin fogy el.
     */
    @Override
    public void tisztit(Sav cel, Sav melle, Ut ut) {
        //A sáv, amelyre a sárkányfej hatással lesz
        if (cel != null) {
            System.out.println(">>> [HÓKOTRÓ AKCIÓ] Sárkányfej aktiválva a(z) " + cel.getSavSzama() + ". sávban! Extrém hőhatás...");
            
            boolean voltHo = cel.getHo() > 0;
            boolean voltJeg = cel.jegesE();
            
            //Hó és jég azonnali megszüntetése
            cel.setHo(0);
            cel.setJeges(false);
            
            //Biokerozin fogyasztása
            if (voltHo || voltJeg) {
                System.out.println("    >>> [SIKER] A lángok eltüntették a " + (voltHo ? "havat " : "") + (voltHo && voltJeg ? "és a " : "") + (voltJeg ? "jeget " : "") + "a sávból.");
            } else {
                System.out.println("    >>> [INFO] A sáv eleve tiszta volt, elpazaroltunk " + biokerozinIgeny + " egység biokerozint.");
            }
        }
    }

}