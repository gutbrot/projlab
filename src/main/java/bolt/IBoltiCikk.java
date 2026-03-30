package bolt;

import jatekos.*;

/**
 * Közös interfész minden olyan tárgy vagy szolgáltatás számára, 
 * amely a Bolt kínálatában szerepelhet és megvásárolható.
 * Biztosítja az egységes kezelést a vásárlási tranzakciók során.
 */
public interface IBoltiCikk {
    
    /**
     * Meghatározza a termék átadásának módját a vásárló számára.
     * A konkrét megvalósítás felelős azért, hogy a termék (pl. kotrófej vagy üzemanyag) 
     * bekerüljön a Takarító eszköztárába vagy felszerelésre kerüljön.
     * * @param t A vásárlást végző Takarító játékos, aki megkapja a cikket.
     */
    void atadVevonek(Takarito t);

    /**
     * Visszaadja a termék aktuális vételárát.
     * A Bolt ezen érték alapján ellenőrzi a fedezetet és vonja le a költséget.
     * * @return A termék ára játékbeli fizetőeszközben.
     */
    int getAr();
}