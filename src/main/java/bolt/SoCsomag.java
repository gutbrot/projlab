package bolt;

import jatekos.Takarito;

/**
 * A SoCsomag osztály a rendszerben fellelhető egyik specifikus fogyóanyagot reprezentálja
 * A FogyoAnyag absztrakt osztályból származik, ezáltal beilleszkedik a Bolt kínálatába
 * 
 * Fő felelőssége a só tárolása és az eladási tranzakció során történő átadása a takarító játékosok számára
 * Ez az alapanyag elengedhetetlen üzemanyagul szolgál a sószóró kotrófej (SoszoroFej) 
 * működéséhez
 */
public class SoCsomag extends FogyoAnyag {
    
    /**
     * Konstruktor a SoCsomag létrehozásához és alapadatainak beállításához
     * Az osztály biztosítja, hogy a vásárlási folyamat során a készlet (mennyiség) 
     * és az ár adatai megfelelőek legyenek. A tényleges validációt az ősosztály végzi.
     * 
     * @param mennyiseg A csomagban tárolt só mennyisége (pl. egységben).
     * @param ar A termék aktuális ára, ami a vásárláskor levonásra kerül a játékostól
     */
    public SoCsomag(int mennyiseg, int ar) {
        // Az ősosztály (FogyoAnyag) konstruktorának hívása, amely rögzíti
        // és biztonságosan eltárolja (negatív értékeket kiszűrve) az árat és mennyiséget.
        super(mennyiseg, ar);
    }

    /**
     * Megvalósítja az eladási folyamatot, az IBoltiCikk interfész szerződése alapján
     * Ezen a metóduson keresztül lép kapcsolatba az objektum a takarítóval (aki a vásárlást indította), 
     * és fizikai értelemben átadja a megvásárolt fogyóanyagot
     * 
     * @param v A vásárlást végző Takarító játékos, akinek az eszköztára bővülni fog
     */
    @Override
    public void atadVevonek(Takarito v) {
        // Biztonsági ellenőrzés: csak valós (nem null) játékos kaphatja meg az árut.
        if (v != null) {
            // A vásárlás eredményeként a só mennyiséget a paraméterként kapott 
            // takarító jármű (pontosabban a játékos) eszköztárába töltjük
            // A "so" kulcsszó biztosítja, hogy az Eszkoztar megfelelő rekeszébe kerüljön.
            // Megjegyzés: A 100-as soMax korlátot (dokumentáció alapján) az Eszkoztar kezeli
            v.getEszkoztar().hozzaad("so", mennyiseg);
            
            // Konzolos visszajelzés a prototípus teszteléséhez, hogy a felhasználó
            // lássa a tranzakció fizikai kimenetelét.
            System.out.println(">>> Sikeres tranzakció: A(z) " + mennyiseg + " egységnyi só bekerült a Takarító eszköztárába.");
        } else {
            // Hibakezelés és naplózás a konzolra, ha érvénytelen játékos hívta meg.
            System.out.println(">>> Hiba az átadásnál: Érvénytelen (null) Takarító próbálta átvenni a SoCsomagot!");
        }
    }

    /**
     * Visszaadja a termék egyértelmű megnevezését
     * A Bolt osztály a listázásnál és a keresésnél használja ezt a stringet.
     * 
     * @return A termék hivatalos neve a boltban: "SoCsomag"
     */
    @Override
    public String getNev() {
        // Konstans visszatérési érték a termék azonosításához.
        return "SoCsomag";
    }
}