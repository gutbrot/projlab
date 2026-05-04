package terkep;

import java.util.List;

/**
 * A Hid osztály az Ut egy speciális típusa.
 */
public class Hid extends Ut {
    
    public Hid(String nev, int hossz, int pozSavokSzama, int negSavokSzama) { 
        super(nev, hossz, pozSavokSzama, negSavokSzama); 
    }

    @Override
    public void havazik(int h) {
        for (List<Sav> szakasz : szakaszok) {
            for (Sav sav : szakasz) {
                int jelenlegiHo = sav.getHo();
                sav.setHo(jelenlegiHo + h);
            }
        }
        System.out.println("    >>> [IDŐJÁRÁS] " + nev + " (Híd): Havazás történt (+" + h + " cm minden sávban). Fokozott fagyásveszély!");
    }
}