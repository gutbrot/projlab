package eszkoztar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import kotrofej.KotroFej;
import skeleton.Skeleton;

/**
 * Az Eszkoztar osztály feladata a játékos által vásárolt eszközök tárolása.
 * Kezeli a só- és biokerozin készleteket, valamint nyilvántartja a rendelkezésre 
 * álló különböző kotrófejeket
 */
public class Eszkoztar {
    
    /** A raktáron lévő kotrófej objektumok listája */
    private List<KotroFej> kotroFejek = new ArrayList<>();

    /** A tárolt só mennyisége */
    private int soKeszlet;
    /** A tárolható só maximális mennyisége (100) */
    private static final int soMax = 100;

    /** A tárolt speciális fogyóanyag (biokerozin) mennyisége. */
    private int biokerozinKeszlet;
    /** A tárolható biokerozin maximális mennyisége (100). */
    private static final int biokerozinMax = 100;

    /** A tárolt zúzalék mennyisége. */
    private int zuzalekKeszlet;
    /** A tárolható zúzalék maximális mennyisége (100). */
    private static final int zuzalekMax = 100;

    /**
     * Csökkenti a megadott típusú fogyóanyag mennyiségét.
     * Ha nincs elég készlet, false értékkel tér vissza, egyébként elvégzi a levonást.
     * 
     * @param tipus A fogyóanyag típusa ("so", "biokerozin", "zuzalek").
     * @param mennyiseg A levonni kívánt mennyiség.
     * @return True sikeres levonás esetén, egyébként False.
     */
    public boolean levon(String tipus, int mennyiseg) {
        if (mennyiseg < 0) return false;

        switch (tipus.toLowerCase()) {
            case "so":
                if (soKeszlet >= mennyiseg) {
                    soKeszlet -= mennyiseg;
                    return true;
                }
                break;
            case "biokerozin":
                if (biokerozinKeszlet >= mennyiseg) {
                    biokerozinKeszlet -= mennyiseg;
                    return true;
                }
                break;
            case "zuzalek":
                if (zuzalekKeszlet >= mennyiseg) {
                    zuzalekKeszlet -= mennyiseg;
                    return true;
                }
                break;
        }
        return false; // Nincs elég fogyóanyag vagy ismeretlen típus
    }

    /**
     * Növeli a készletet a megadott típussal és mennyiséggel.
     * Érvényesíti a maximum kapacitást; a felesleg elveszik.
     * 
     * @param tipus A fogyóanyag típusa.
     * @param mennyiseg A hozzáadni kívánt mennyiség.
     */
    public void hozzaad(String tipus, int mennyiseg) {
        if (mennyiseg < 0) return;

        switch (tipus.toLowerCase()) {
            case "so":
                soKeszlet += mennyiseg;
                if (soKeszlet > soMax) soKeszlet = soMax; // A felesleg elveszik
                break;
            case "biokerozin":
                biokerozinKeszlet += mennyiseg;
                if (biokerozinKeszlet > biokerozinMax) biokerozinKeszlet = biokerozinMax;
                break;
            case "zuzalek":
                zuzalekKeszlet += mennyiseg;
                if (zuzalekKeszlet > zuzalekMax) zuzalekKeszlet = zuzalekMax;
                break;
        }
    }

    /**
     * Ellenőrzi, hogy rendelkezésre áll-e a kért mennyiség az adott fogyóanyagból
     * 
     * @param tipus A vizsgált anyag típusa.
     * @param mennyiseg A szükséges mennyiség.
     * @return True, ha van elég, False ha nincs[cite: 1].
     */
    public boolean vanE(String tipus, int mennyiseg) {
        if (mennyiseg < 0) return false;
        switch (tipus.toLowerCase()) {
            case "so": return soKeszlet >= mennyiseg;
            case "biokerozin": return biokerozinKeszlet >= mennyiseg;
            case "zuzalek": return zuzalekKeszlet >= mennyiseg;
            default: return false;
        }
    }

    /**
     * Új kotrófej hozzáadása a raktárhoz (vásárlás után)
     * 
     * @param fej A raktárba kerülő fej.
     */
    public void hozzaadFej(KotroFej fej) {
        if (fej != null) {
            kotroFejek.add(fej);
        }
    }

    /**
     * Kivesz egy kotrófejet a raktárból felszereléshez.
     * 
     * @return Az első elérhető kotrófej vagy null.
     */
    public KotroFej kiveszFej() {
        if (kotroFejek.isEmpty()) return null;
        return kotroFejek.remove(0);
    }

    /**
     * Visszaadja a raktáron lévő kotrófejek módosíthatatlan listáját.
     * 
     * @return A kotrófejek listája.
     */
    public List<KotroFej> getKotroFejek() {
        return Collections.unmodifiableList(kotroFejek);
    }

    // --- GETTEREK ---
    public int getSoKeszlet() { return soKeszlet; }
    public int getBiokerozinKeszlet() { return biokerozinKeszlet; }
    public int getZuzalekKeszlet() { return zuzalekKeszlet; }
}