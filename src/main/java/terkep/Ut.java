package terkep;

import java.util.List;
import jarmu.Jarmu;

/**
 * Absztrakt út osztály, amely a térkép úthálózatának alapeleme.
 * Az utok sávokból állnak.
 */
public abstract class Ut {
    /** Az út megnevezése. */
    protected String nev;
    
    /** Az út hossza egységekben. */
    protected int hossz;
    
    /** * Kétdimenziós lista, amely az utat szakaszokra (első szint) 
     * és azon belüli sávokra (második szint) bontja.
     */
    protected List<List<Sav>> szakaszok;

    /**
     * Irányítja a járművek haladását az úton a sávok között.
     * @param j Az érintett jármű.
     * @param s Az aktuális sáv.
     * @return true, ha a jármű sikeresen továbbhaladt.
     */
    public boolean jarmuEligazito(Jarmu j, Sav s) {
        // Szkeleton logika: alapértelmezetten engedi a haladást, ha a sáv átjárható
        return s.atjarhatoE(j);
    }

    /**
     * Absztrakt metódus az időjárási események kezelésére.
     * @param h A lehullott hó mennyisége.
     */
    public abstract void havazik(int h);
}

/**
 * Normál útszakasz
 */
class SimaUt extends Ut {
    @Override
    public void havazik(int h) {
        if (szakaszok != null) {
            for (List<Sav> szakasz : szakaszok) {
                for (Sav s : szakasz) {
                    s.setHo(h); // A Sav osztályban korábban setHo-ra neveztük át
                }
            }
        }
    }
}

/**
 * Alagút, amely megvédi az úttestet a havazástól.
 */
class Alagut extends Ut {
    @Override
    public void havazik(int h) {
        // Az alagút fedett, így a belső sávok hóvastagsága nem változik.
    }
}

/**
 * Híd, amely a sima úthoz hasonlóan behavazódik, de speciális, mivel van egy exra sávja ami csak arra van fentartva
 * hogy a havat oda lehessen takarítani
 */
class Hid extends Ut {
    @Override
    public void havazik(int h) {
        if (szakaszok != null) {
            for (List<Sav> szakasz : szakaszok) {
                for (Sav s : szakasz) {
                    s.setHo(h);
                }
            }
        }
    }
}