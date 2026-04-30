package jarmu;

import jatekos.Takarito;
import eszkoztar.Eszkoztar;
import kotrofej.KotroFej;
import bolt.IBoltiCikk;
import terkep.*;
import skeleton.Skeleton;
import java.util.List;

/**
 * A Hokotro osztály egy járművet reprezentál. 
 * Elsődleges feladata az úthálózat tisztán tartása a rá felszerelt eszközök segítségével. 
 * Felelős a saját pozíciójának kezeléséért, valamint a tisztítási folyamat végrehajtásáért. 
 * A takarító játékosok által irányított jármű
 */
public class Hokotro extends Jarmu implements IBoltiCikk {
    
    /** A hókotró egyedi azonosítója a tesztkörnyezetben. */
    private final String id;
    
    /** A hókotró ára*/
    private int ar = 50;
    
    /** Eltárolja, hogy a hókotrón jelenleg milyen fej van */
    private KotroFej felszereltFej;
    
    /** A hókotróhoz tartozó eszköztár */
    private Eszkoztar eszkoztar;
    
    /**
     * Konstruktor a Hokotro példányosításához.
     * @param id Egyedi azonosító.
     * @param pozicio Kezdőpozíció.
     * @param felszereltFej Kezdő kotrófej.
     */
    public Hokotro(String id, Lokacio pozicio, KotroFej felszereltFej) {
        super(pozicio);
        this.id = id;
        this.felszereltFej = felszereltFej;
        this.eszkoztar = new Eszkoztar();
    }

    /**
     * A vásárlás lebonyolításáért és a jármű takarító játékosnak való átadásáért felel
     * @param v A vásárlást végző Takarító játékos
     */
    @Override
    public void atadVevonek(Takarito v) {
        Skeleton.functionCalled("atadVevonek", this, "void", v);
        if (v != null) {
            v.hozzaadHokotro(this);
        }
        Skeleton.voidReturn();
    }

    /**
     * A játékos kiválasztja a használt felszerelhető kotró fejet az eszközei közül
     * A fejcsere műveletét valósítja meg
     * 
     * @param ujFej Az újonnan felszerelendő fej
     */
    public void fejcsere(KotroFej ujFej) {
        Skeleton.functionCalled("fejcsere", this, "void", ujFej);
        
        // Dokumentáció diagram: Rendelkezésre áll az 'ujFej'?
        // (A Takarito osztály ellenőrzi az eszköztárat a hívás előtt)
        if (ujFej != null) {
            this.felszereltFej = ujFej;
            System.out.println(">>> Sikeres fejcsere történt.");
        }
        
        Skeleton.voidReturn();
    }
    
    /**
     * Meghívja a kotrófej tisztító metódusát, amely módosítja az 
     * érintett útszakasz állapotát a térképen
     * 
     * @param terkep A játéktér térképe
     */
    public void takarit(Terkep terkep) {
        Skeleton.functionCalled("takarit", this, "void", terkep);

        // 1. Aktuális pozícióhoz tartozó sáv lekérdezése
        Sav cel = pozicio.getSav();
        Ut ut = pozicio.getUt();

        // 2. A sávon lévő csapadék típusának azonosítása
        // 3. A 'felszereltFej' képes eltakarítani az adott csapadéktípust?
        // (Ezt a KotroFej leszármazottai döntik el a tisztit hívásakor)

        // 4. A térkép segítségével a szomszédos sáv lekérdezése
        Sav melle = null;
        List<Sav> szakasz = pozicio.getSzakasz();
        if (szakasz != null && szakasz.size() > 1) {
            int idx = szakasz.indexOf(cel);
            if (idx + 1 < szakasz.size()) melle = szakasz.get(idx + 1);
            else if (idx - 1 >= 0) melle = szakasz.get(idx - 1);
        }

        // 5. Tisztítási logika függvényének hívása
        if (felszereltFej != null) {
            felszereltFej.tisztit(cel, melle, ut);
        }

        Skeleton.voidReturn();
    }

    /**
     * Megvalósítja az ütközéskezelést
     */
    @Override
    public void utkozos() {
        Skeleton.functionCalled("utkozos", this, "void");
        // Meghívja a Jarmu ősosztály büntetés-kezelőjét
        mozgasKeptelen();
        Skeleton.voidReturn();
    }

    // --- GETTEREK ---
    public String getId() { return id; }
    
    @Override
    public int getAr() { return ar; }
    
    public KotroFej getFelszereltFej() { return felszereltFej; }
    
    public Eszkoztar getEszkoztar() { return eszkoztar; }
    
    /** Visszaadja a jármű aktuális állapotát */
    public Hokotro getHokotro() { 
        return this; 
    }
}