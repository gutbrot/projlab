package terkep;

import java.util.List;

/**
 * A SimaUt osztály az Ut egy speciális típusa.
 */
public class SimaUt extends Ut {
    
    //KONSTRUKTOR
    public SimaUt(String nev, int hossz, int pozSavokSzama, int negSavokSzama) { 
        super(nev, hossz, pozSavokSzama, negSavokSzama); 
    }

    /**
     * A havazik metódus frissíti az út minden sávjának hóvastagságát a megadott érték szerint.
     */
    @Override
    public void havazik(int h) {
        //Minden szakasz minden sávjának hóvastagságát növeljük a megadott értékkel
        for (List<Sav> szakasz : szakaszok) {
            for (Sav sav : szakasz) {
                int jelenlegiHo = sav.getHo();
                sav.setHo(jelenlegiHo + h);
            }
        }
        System.out.println("    >>> [IDŐJÁRÁS] " + nev + " (Sima Út): Havazás történt (+" + h + " cm minden sávban).");
    }
}