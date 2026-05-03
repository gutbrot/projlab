package bolt;

import jarmu.Hokotro;
import jatekos.Takarito;

/**
 * A sót reprezentáló osztály. A sószóró fej (SoszoroFej) működéséhez szükséges.
 */
public class SoCsomag extends FogyoAnyag {
    
    /**
     * Konstruktor, ami az ősosztály (FogyoAnyag) segítségével beállítja az árat és mennyiséget.
     */
    public SoCsomag(int mennyiseg, int ar) {
        super(mennyiseg, ar);
    }

    /**
     * Megvalósítja a só átadását a játékos hókotrójának eszköztárába.
     */
    @Override
    public void atadVevonek(Takarito v) {
        // Null check a játékosra.
        if (v != null) {
            // Aktív hókotró lekérdezése (ide kerül a só, nem közvetlenül a játékoshoz).
            Hokotro aktivHokotro = v.hokotrotValaszt();
            
            // Annak ellenőrzése, hogy létezik-e a hókotró és a benne lévő eszköztár.
            if (aktivHokotro != null && aktivHokotro.getEszkoztar() != null) {
                
                // A só tényleges hozzáadása az Eszköztárhoz a "so" kulcsszóval.
                // A maximális kapacitás ellenőrzését az Eszkoztar.hozzaad() metódus fogja elvégezni.
                aktivHokotro.getEszkoztar().hozzaad("so", mennyiseg);
                
                // Narratív logolás
                System.out.println(">>> [BOLT] Sikeres vásárlás: " + mennyiseg + " egység só betöltve a Hókotró eszköztárába.");
            } else {
                System.out.println(">>> [BOLT HIBA] A játékosnak nincs aktív hókotrója a só fogadásához!");
            }
        } else {
            System.out.println(">>> [BOLT HIBA] Érvénytelen (null) játékos próbált vásárolni!");
        }
    }

    /**
     * A termék azonosító neve a menükhöz.
     */
    public String getNev() {
        return "SoCsomag";
    }
}