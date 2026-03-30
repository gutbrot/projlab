package eszkoztar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kotrofej.KotroFej;

/**
 * Az Eszkoztar osztály feladata a játékos által vásárolt eszközök tárolása[cite: 398].
 * Kezeli a só- és biokerozin készleteket, valamint nyilvántartja a rendelkezésre álló különböző kotrófejeket[cite: 399].
 * Biztosítja a metódusokat a készletek módosításához és ellenőrzéséhez[cite: 400].
 */
public class Eszkoztar {
    /** A tárolt só mennyisége[cite: 404]. */
    private int soKeszlet;
    /** A tárolt speciális fogyóanyag (biokerozin) mennyisége[cite: 405]. */
    private int biokerozinKeszlet;
    /** A raktáron lévő kotrófej objektumok listája[cite: 406]. */
    private List<KotroFej> kotroFejek = new ArrayList<>();

    /**
     * Csökkenti a megadott típusú fogyóanyag mennyiségét[cite: 412].
     * @param tipus A fogyóanyag típusa ("so" vagy "biokerozin").
     * @param mennyiseg A levonni kívánt mennyiség.
     * @return Ha nincs elég készlet, false értékkel tér vissza, egyébként elvégzi a levonást és true-t ad[cite: 413].
     */
    public boolean levon(String tipus, int mennyiseg){
        if (mennyiseg < 0) return false;
        switch (tipus.toLowerCase()) {
            case "so":
                if (soKeszlet >= mennyiseg) { soKeszlet -= mennyiseg; return true; }
                return false;
            case "biokerozin":
                if (biokerozinKeszlet >= mennyiseg) { biokerozinKeszlet -= mennyiseg; return true; }
                return false;
            default:
                return false;
        }
    }

    /**
     * Növeli a készletet a megadott típussal és mennyiséggel[cite: 414].
     * @param tipus A fogyóanyag típusa.
     * @param mennyiseg A hozzáadni kívánt mennyiség.
     */
    public void hozzaad(String tipus, int mennyiseg){
        if (mennyiseg < 0) return;
        switch (tipus.toLowerCase()) {
            case "so": soKeszlet += mennyiseg; break;
            case "biokerozin": biokerozinKeszlet += mennyiseg; break;
            default: break;
        }
    }
    
    /**
     * Ellenőrzi, hogy rendelkezésre áll-e a kért mennyiség az adott fogyóanyagból anélkül, hogy levonná azt[cite: 415].
     * @param tipus A vizsgált fogyóanyag típusa.
     * @param mennyiseg A szükséges mennyiség.
     * @return True, ha van elég készlet, egyébként false.
     */
    public boolean vanE(String tipus, int mennyiseg){
        if (mennyiseg < 0) return false;
        switch (tipus.toLowerCase()) {
            case "so": return soKeszlet >= mennyiseg;
            case "biokerozin": return biokerozinKeszlet >= mennyiseg;
            default: return false;
        }
    }
    
    /**
     * Új kotrófej hozzáadása az eszköztárhoz.
     * @param fej A hozzáadandó kotrófej objektum.
     */
    public void hozzaadFej(KotroFej fej) {
        if (fej != null) kotroFejek.add(fej);
    }
    
    /**
     * Kiveszi a listában lévő első kotrófejet.
     * @return A kivett kotrófej, vagy null, ha az eszköztár üres.
     */
    public KotroFej kiveszFej() {
        if (kotroFejek.isEmpty()) return null;
        return kotroFejek.remove(0);
    }

    /**
     * Visszaadja a raktáron lévő kotrófejek listáját.
     * @return Nem módosítható lista a kotrófejekről.
     */
    public List<KotroFej> getKotroFejek() {
        return Collections.unmodifiableList(kotroFejek);
    }
    
    /** @return A sókészlet aktuális értéke[cite: 404]. */
    public int getSoKeszlet() { return soKeszlet; }
    /** @return A biokerozin készlet aktuális értéke[cite: 405]. */
    public int getBiokerozinKeszlet() { return biokerozinKeszlet; }
}