package bolt;

import jatekos.Takarito;

/**
 * A zuzalekCsomag osztály a rendszerben fellelhető egyik specifikus fogyóanyagot reprezentálja
 * A FogyoAnyag ősosztályból származik, így beilleszthető a Bolt egységes kínálatába
 * 
 * Fő felelőssége a csúszásgátló zúzalék tárolása és a vásárlási tranzakció során
 * történő fizikai átadása a takarító járművek (Takarító játékos) számára
 * Ez az alapanyag elengedhetetlenül szükséges a zúzottkő-szóró (ZuzottFej) kotrófej 
 * működéséhez
 */
public class zuzalekCsomag extends FogyoAnyag {

    /**
     * Konstruktor a zuzalekCsomag létrehozásához és inicializálásához
     * Beállítja az objektum alapvető paramétereit a példányosítás pillanatában.
     * 
     * @param mennyiseg A csomagban tárolt zúzalék mennyisége (pl. egységben).
     * @param ar A termék aktuális ára, amelyet a Bolt levon a játékostól vásárláskor
     */
    public zuzalekCsomag(int mennyiseg, int ar) {
        // Az ősosztály (FogyoAnyag) konstruktorának hívása, amely biztosítja
        // a mennyiség és az ár adatainak biztonságos (nem negatív) tárolását
        super(mennyiseg, ar);
    }

    /**
     * Megvalósítja az eladási folyamatot az IBoltiCikk interfész és a 
     * dokumentációban rögzített "Sikeres zuzalék vásárlás" szekvenciadiagram alapján
     * 
     * Ez a metódus felelős azért, hogy ezt a konkrét zúzalékcsomagot odaadja a vásárlónak
     * Átadja a zúzalék mennyiségét a paraméterként kapott takarító játékos eszköztárának
     * 
     * @param v A vásárlást végző Takarító játékos, aki a tranzakció végén megkapja az árut
     */
    @Override
    public void atadVevonek(Takarito v) {
        // Biztonsági ellenőrzés: garantáljuk, hogy csak érvényes, létező 
        // Takarító játékos kaphatja meg az árut a NullPointerException elkerülése végett.
        if (v != null) {
            // A zúzalékot hozzáadjuk a takarító eszköztárához
            // A "zuzalek" String azonosító alapján az Eszkoztar switch-case logikája
            // a zuzalekKeszlet változót fogja megnövelni
            // Megjegyzés: A zuzalekMax = 100 korlátozást az Eszkoztar.hozzaad() fogja érvényesíteni
            v.getEszkoztar().hozzaad("zuzalek", mennyiseg);
            
            // Konzolos visszajelzés a prototípus teszteléséhez, hogy a fejlesztők
            // és tesztelők pontosan lássák a tranzakció fizikai kimenetelét a képernyőn.
            System.out.println(">>> Sikeres tranzakció: A(z) " + mennyiseg + " egységnyi zúzalék bekerült a Takarító eszköztárába.");
        } else {
            // Hibakezelés és naplózás a konzolra, ha a metódust hibás paraméterrel (null) hívták meg.
            System.out.println(">>> Hiba az átadásnál: Érvénytelen (null) Takarító próbálta átvenni a ZuzalekCsomagot!");
        }
    }

    /**
     * Lekérdezi és visszaadja a csomag (termék) hivatalos megnevezését
     * A Bolt osztály ezt a nevet használja az árucikkek konzolos listázásánál, 
     * illetve a vásárlás során az áru azonosításánál
     * 
     * @return A termék hivatalos neve a boltban: "ZuzalekCsomag"
     */
    @Override
    public String getNev() {
        // Konstans visszatérési érték a termék egyértelmű azonosításához.
        return "ZuzalekCsomag";
    }
}