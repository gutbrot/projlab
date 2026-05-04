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
    
    /**A raktáron lévő kotrófej objektumok listája */
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
     */
    public boolean levon(String tipus, int mennyiseg) {
        if (mennyiseg < 0) return false;

        // A típus alapján csökkentjük a megfelelő készletet, ha van elég
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
        return false; //Nincs elég fogyóanyag vagy ismeretlen típus
    }

    /**
     * Növeli a készletet a megadott típussal és mennyiséggel.
     * Érvényesíti a maximum kapacitást; a felesleg elveszik.
     */
    public void hozzaad(String tipus, int mennyiseg) {
        if (mennyiseg < 0) return;

        //A típus alapján növeljük a megfelelő készletet, de nem léphetjük túl a maximumot
        switch (tipus.toLowerCase()) {
            case "so":
                soKeszlet += mennyiseg;
                if (soKeszlet > soMax) soKeszlet = soMax; //A felesleg elveszik
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
     */
    public void hozzaadFej(KotroFej fej) {
        if (fej != null) {
            kotroFejek.add(fej);
        }
    }

    /**
     * Kivesz egy kotrófejet a raktárból felszereléshez.
     */
    public KotroFej kiveszFej(String fejNev) {
        if (fejNev == null) return null;

        //Keresünk egy olyan kotrófejet, amelynek a neve megegyezik a keresett névvel
        for (int i = 0; i < kotroFejek.size(); i++) {
            KotroFej fej = kotroFejek.get(i);

            //A fej osztályneve alapján hasonlítjuk össze
            if (fej.getClass().getSimpleName().equalsIgnoreCase(fejNev)) {
                //Ha megtaláltuk, eltávolítjuk a raktárból és visszaadjuk
                return kotroFejek.remove(i);        
            }
        }

        return null;
    }

    /**
     * Kiírja a raktáron lévő kotrófejek listáját a konzolra.
     */
    public void listazKotroFejek() {
        //Ha nincs egyetlen kotrófej sem, jelezzük a játékosnak
        if (kotroFejek.isEmpty()) {
            System.out.println(">>> Nincs elerheto kotrofej az eszkoztarban.");
            return;
        }

        //Kiírjuk a rendelkezésre álló kotrófejek listáját
        System.out.println(">>> Elerheto kotrofejek:");
        for (KotroFej fej : kotroFejek) {
            System.out.println("    - " + fej.getClass().getSimpleName());
        }
    }

    /**
     * Visszaadja a raktáron lévő kotrófejek módosíthatatlan listáját.
     */
    public List<KotroFej> getKotroFejek() {
        return Collections.unmodifiableList(kotroFejek);
    }

    // --- GETTEREK ---
    public int getSoKeszlet() { return soKeszlet; }
    public int getBiokerozinKeszlet() { return biokerozinKeszlet; }
    public int getZuzalekKeszlet() { return zuzalekKeszlet; }
}