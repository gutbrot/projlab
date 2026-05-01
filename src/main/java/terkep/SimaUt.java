package terkep;

import java.util.List;

/**
 * A SimaUt osztály az Ut egy speciális típusa.
 */
public class SimaUt extends Ut {
    
    public SimaUt(String nev, int hossz, int savokSzama) { 
        super(nev, hossz, savokSzama); 
    }

    @Override
    public void havazik(int h) {
        for (List<Sav> szakasz : szakaszok) {
            for (Sav sav : szakasz) {
                int jelenlegiHo = sav.getHo();
                sav.setHo(jelenlegiHo + h);
            }
        }
        System.out.println("    >>> [IDŐJÁRÁS] " + nev + " (Sima Út): Havazás történt (+" + h + " cm minden sávban).");
    }
}