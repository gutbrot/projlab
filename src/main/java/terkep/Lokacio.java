package terkep;

import java.util.List;

/**
 * A Lokacio osztály felelőssége a helymeghatározás tárolása a térben.
 * Elsődleges feladata az adatszolgáltatás a járművek és a térkép számára.
 * Segítségével pontosan beazonosítható, hogy egy objektum melyik úton, 
 * annak melyik szakaszán és pontosan melyik sávjában helyezkedik el.
 */
public class Lokacio {
    
    /** Az út nevét/referenciáját tárolja */
    private Ut ut;
    
    /** A szakaszokat tárolja. */
    private List<Sav> szakasz;
    
    /** A sávok számát/referenciáját tárolja. */
    private Sav sav;

    /**
     * Konstruktor a helymeghatározási adatok inicializálásához.
     * 
     * @param ut Az út.
     * @param szakasz Az útszakasz (sávok listája).
     * @param sav A konkrét sáv.
     */
    public Lokacio(Ut ut, List<Sav> szakasz, Sav sav) {
        this.ut = ut;
        this.szakasz = szakasz;
        this.sav = sav;
    }

    // --- LEKÉRDEZŐK (SZÜKSÉGESEK A RENDSZER MŰKÖDÉSÉHEZ) ---

    public Ut getUt() { 
        return ut; 
    }

    public List<Sav> getSzakasz() { 
        return szakasz; 
    }

    public Sav getSav() { 
        return sav; 
    }

    public void setSav(Sav sav) {
        this.sav = sav;
    }
}