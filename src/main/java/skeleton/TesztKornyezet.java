package skeleton;

import java.util.List;
import bolt.*;
import jarmu.*;
import terkep.*;
import jatekos.*;
import kotrofej.*;

/**
 * A TesztKornyezet osztály felelős a teszteléshez szükséges objektumstruktúra (világ) felépítéséért
 * és a konkrét tesztforgatókönyvek (test cases) futtatásáért.
 */
public abstract class TesztKornyezet {

    // --- A TESZTVILÁG OBJEKTUMAI (Globális statikus változók) ---
    static Terkep terkep;         // Az úthálózatot tároló objektum
    static Jatekter jatekter;     // A játékmenetet vezérlő osztály (körök, jatekosok kezelése)
    static Bolt bolt;             // A vásárláshoz használt bolt példány

    // Szereplők
    static Takarito takarito;     // Hókotrókat irányító játékos
    static Buszvezeto buszvezeto; // Buszokat irányító játékos

    // Járművek
    static Hokotro hokotro1, hokotro2;
    static Busz busz1;
    static Auto auto1;

    // Térkép elemek
    static Ut ut1, ut2;           // Út típusú objektumok
    static Sav sav1_1, sav1_2, sav2_1; // Konkrét forgalmi sávok
    static Lokacio lok1, lok2, lok3;   // Járművek pontos helyzetét leíró objektumok

    /**
     * Inicializálja a teszteléshez használt világot.
     * Létrehozza az utakat, sávokat, a járműveket és a játékosokat,
     * majd összekapcsolja őket (pl. járművek elhelyezése a térképen).
     */
    public static void initializeWorld() {
        // Alaprendszerek létrehozása
        terkep = new Terkep();
        jatekter = new Jatekter(terkep);
        bolt = new Bolt();

        // --- Úthálózat felépítése ---
        // Létrehozunk egy 2 sávos, 2 szakasz hosszú sima utat és egy 1 sávos hidat
        ut1 = new SimaUt("FoUt", 2, 2);
        ut2 = new Hid("Hid", 1, 1);
        terkep.addUt(ut1);
        terkep.addUt(ut2);

        // Konkrét sávok kinyerése az utakból az indexek alapján
        sav1_1 = ut1.getSav(0, 0); // FoUt első szakasz, első sáv
        sav1_2 = ut1.getSav(0, 1); // FoUt első szakasz, második sáv
        sav2_1 = ut2.getSav(0, 0); // Hid első sávja

        // Lokációk (koordináták) létrehozása a sávokhoz
        lok1 = new Lokacio(ut1, ut1.getSzakaszok().get(0), sav1_1);
        lok2 = new Lokacio(ut1, ut1.getSzakaszok().get(0), sav1_2);
        lok3 = new Lokacio(ut2, ut2.getSzakaszok().get(0), sav2_1);

        // --- Szereplők és járművek inicializálása ---
        takarito = new Takarito(3, 1000); // 3 akciópont, 1000 egység pénz
        buszvezeto = new Buszvezeto(3);   // 3 akciópont

        // Járművek létrehozása kezdőhelyzettel és alapértelmezett felszereléssel
        hokotro1 = new Hokotro(lok1, new SoproFej(100));
        hokotro2 = new Hokotro(lok2, new JegtoroFej(150));
        busz1 = new Busz(lok1, lok3, lok1); // Végállomások: lok1 és lok3, aktuális: lok1
        auto1 = new Auto(lok1, lok3, lok3);

        // Járművek hozzárendelése a megfelelő irányítóhoz
        takarito.hozzaadHokotro(hokotro1);
        buszvezeto.hozzaadBusz(busz1);

        // Regisztráció a játéktérbe a körök kezeléséhez
        jatekter.hozzaadJatekos(takarito);
        jatekter.hozzaadJatekos(buszvezeto);
        jatekter.hozzaadJarmu(hokotro1);
        jatekter.hozzaadJarmu(busz1);
        jatekter.hozzaadJarmu(auto1);

        // --- Bolt kínálatának beállítása ---
        bolt.felveszTermek("so", new SoCsomag(10, 50));
        bolt.felveszTermek("jégtörő", new JegtoroFej(200));
    }

    /**
     * Test101: Alapvető mozgás tesztelése.
     * Ellenőrzi, hogy a takarító képes-e akciópont levonása mellett mozgatni a hókotrót egy szomszédos sávra.
     */
    public static void Test101() {
        takarito.hokotroMozgat(hokotro1); // Akciópont ellenőrzés és levonás
        hokotro1.mozgas(sav1_2);         // Tényleges helyváltoztatás a térképen
    }

    /**
     * Test201: Sikeres vásárlás folyamata.
     * A takarító kifizeti a terméket, levonódik az akciópontja, és az áru az eszköztárába kerül.
     */
    public static void Test201() {
        takarito.vasarol(bolt, "so");
    }

    /**
     * Test202: Sikertelen vásárlás (fedezethiány).
     * Ellenőrzi, hogy a rendszer megakadályozza-e a vásárlást, ha a takarítónak nincs elég pénze.
     */
    public static void Test202() {
        Takarito szegenyTakarito = new Takarito(1, 10);
        szegenyTakarito.vasarol(bolt, "jégtörő"); // A jégtörő 200-ba kerül, a jatekosnak csak 10 van
    }

    /**
     * Test301: Hóeltakarítási funkció.
     * A hókotró a felszerelt fejével (pl. SoproFej) módosítja az aktuális sáv hóvastagságát.
     */
    public static void Test301() {
        hokotro1.takarit(terkep);
    }

    /**
     * Test302: Eszközváltás (kotrófej csere).
     * A takarító az eszköztárában lévő egyik fejet felszereli a hókotróra.
     */
    public static void Test302() {
        KotroFej ujFej = new JegtoroFej(200);
        takarito.getEszkoztar().hozzaadFej(ujFej); // Először betesszük a raktárba
        takarito.kotrofejValt(hokotro1);           // Majd átszereljük a járműre
    }

    /**
     * Test401: Busz mozgatása.
     * A buszvezető utasítására a busz megpróbál átmenni a szomszédos sávba.
     */
    public static void Test401() {
        buszvezeto.buszMozgat(busz1);
        busz1.mozgas(sav1_2);
    }

    /**
     * Test501: Körváltás mechanizmusa.
     * Minden játékos visszakapja az akciópontjait, és a térképen frissülnek az időjárási viszonyok.
     */
    public static void Test501() {
        jatekter.ujKor();
    }

    /**
     * Test502: Speciális időjárási hatások (alagút).
     * Ellenőrzi, hogy a havazás minden úttípusra hat-e, kivéve az alagutat, ahol nem szabadna nőnie a hónak.
     */
    public static void Test502() {
        Alagut alagut = new Alagut("M0-alagut", 1, 1);
        terkep.addUt(alagut);
        terkep.idojarasFrissites(); // Itt dől el, hogy az alagútban nő-e a hóvastagság
    }
}