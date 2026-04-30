package bolt;

import jatekos.Takarito;

/**
 * A BiokerozinCsomag osztály a rendszerben fellelhető egyik fogyóanyagot reprezentálja.
 * Fő felelőssége a biokerozin tárolása és biztonságos átadása a takarító járművek (pontosabban a játékosok) számára
 * Ez a fogyóanyag elengedhetetlen üzemanyagul szolgál a speciális sárkány kotrófej (SarkanyFej) működéséhez
 */
public class BiokerozinCsomag extends FogyoAnyag {
    
    /**
     * Konstruktor a BiokerozinCsomag létrehozásához és inicializálásához.
     * Az osztály ezen keresztül biztosítja, hogy a vásárlási folyamat során a készlet (mennyiség) 
     * és a vételár adatai a rendszerben megfelelőek és konzisztensek legyenek.
     * 
     * @param mennyiseg A csomagban tárolt biokerozin mennyisége (pl. literben vagy egységben).
     * @param ar A termék aktuális ára, amelyet a Bolt fog levonni a Takarítótól.
     */
    public BiokerozinCsomag(int mennyiseg, int ar) {
        // Meghívjuk az ősosztály (FogyoAnyag) konstruktorát, amely beállítja 
        // és egyben validálja is a mennyiség és az ár alapértékeit (pl. ne lehessen negatív).
        super(mennyiseg, ar);
    }

    /**
     * Megvalósítja az eladási folyamat fizikai átadását
     * Átadja a csomagban lévő biokerozin mennyiséget a paraméterként kapott takarító játékosnak
     * Ezen a metóduson keresztül lép kapcsolatba az objektum a takarítóval a sikeres tranzakció (fizetés) után
     * 
     * @param v A vásárlást végző Takarító játékos, aki az üzemanyagot az eszköztárába kapja.
     */
    @Override
    public void atadVevonek(Takarito v) {
        // Biztonsági ellenőrzés: Megbizonyosodunk róla, hogy a vásárló (Takarító) objektum nem null.
        // Ezzel elkerüljük a futásidejű NullPointerException hibákat, ha a Bolt rossz paramétert adna át.
        if (v != null) {
            // A vásárlás befejezéseként a biokerozin ténylegesen bekerül a Takarító saját eszköztárába[cite: 36].
            // A "biokerozin" string kulcs azonosítja a fogyóanyag típusát az Eszkoztar osztály switch-case logikájában.
            v.getEszkoztar().hozzaad("biokerozin", mennyiseg);
            
            // Konzol alapú visszajelzés a prototípushoz, hogy nyomon követhető legyen a játék állapota.
            System.out.println(">>> Sikeres tranzakció: A(z) " + mennyiseg + " egységnyi biokerozin bekerült a Takarító eszköztárába.");
        } else {
            // Ha valamilyen oknál fogva a vevő null paraméterként érkezik, hibaüzenetet dobunk a konzolra.
            System.out.println(">>> Hiba az átadásnál: Érvénytelen (null) Takarító próbálta átvenni a BiokerozinCsomagot!");
        }
    }

    /**
     * Visszaadja a termék pontos megnevezését
     * Ez az azonosító használható a Bolt kínálatának listázásakor, illetve a vásárlási 
     * folyamat (vasarlas parancs) során a termék név szerinti kereséséhez.
     * 
     * @return A termék hivatalos neve a rendszerben: "BiokerozinCsomag".
     */
    @Override
    public String getNev() {
        // Szimpla konstans visszatérési érték, ami megegyezik a termék típusával.
        return "BiokerozinCsomag";
    }
}