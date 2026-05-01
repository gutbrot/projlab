package bolt;

import jatekos.Takarito;

/**
 * Az IBoltiCikk interfész felelőssége egy egységes felület biztosítása a Bolt kínálatához.
 * Meghatározza az ár lekérdezését és a termék átadását a vásárlónak. 
 * Ezzel lehetővé teszi a rendszer számára, hogy újabb eladható elemekkel bővüljön.
 */
public interface IBoltiCikk {
    
    /**
     * Megadja az átadási folyamatot, amit a megfelelő osztályok 
     * felüldefiniálhatnak a saját igényeiknek megfelelően.
     * 
     * A metódus hívásakor a termék (legyen az KotroFej vagy FogyoAnyag) 
     * ténylegesen bekerül a játékos birtokába.
     * 
     * @param v A vásárlást végző Takarító játékos
     */
    void atadVevonek(Takarito v);

    /**
     * Visszaadja a termék aktuális vételárát.
     * A Bolt osztály ezen érték alapján ellenőrzi a vásárló egyenlegét.
     * 
     * @return A termék ára
     */
    int getAr();
}