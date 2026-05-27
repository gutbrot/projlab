package jarmu;

import terkep.Lokacio;
import jatekos.Buszvezeto;

/**
 * A Busz osztály egy járművet reprezentál.
 * Elsődleges felelőssége a kijelölt megállóhelyek közötti közlekedés.
 */
public class Busz extends Jarmu {
    
    private final String id;
    private final Lokacio[] vegallomasok = new Lokacio[2];
    private Buszvezeto vezeto;
    private static int szamlalo = 1;
    private int celVegallomasIndex = 1; // 0 = A végállomás, 1 = B végállomás

    public static String kovetkezoId() { return "Busz_" + szamlalo++; }

    //Konstruktor a busz létrehozásához, megadva az azonosítót, kezdő pozíciót és a két végállomást.
    public Busz(String id, Lokacio pozicio, Lokacio v1, Lokacio v2) {
        super(pozicio);
        this.id = id;
        this.vegallomasok[0] = v1;
        this.vegallomasok[1] = v2;
    }

    /**
     * Ellenőrzi, hogy a busz elérte-e az aktuális célvégállomást.
     * Ha igen, pontot ad a vezetőnek és a másik végállomást veszi célba.
     */
    public boolean vegallomasbaErt() {
        Lokacio aktualis = getPozicio();
        Lokacio cel = vegallomasok[celVegallomasIndex];

        if (cel == null || aktualis == null || cel.getSav() != aktualis.getSav()) return false;

        System.out.println(">>> [JÁRMŰ AKCIÓ] A(z) " + id + " busz sikeresen elérte a végállomást!");
        if (vezeto != null) vezeto.pontotKap();
        toggleCelVegallomasIndex();
        return true;
    }

    /**
     * A busz ütközését figyelő metódus, amely kiírja a baleset tényét és a busz azonosítóját, 
     * majd mozgásképtelenné teszi a járművet.
     */
    @Override
    public void utkozos() {
        System.out.println(">>> [BALESET] A(z) " + id + " busz megcsúszott és balesetet szenvedett!");
        mozgasKeptelen();
    }

    //--- GETTEREK ÉS SETTEREK ---
    public String getId() { return id; }

    public Lokacio[] getVegallomasok() { return vegallomasok.clone(); }

    public int getCelVegallomasIndex() { return celVegallomasIndex; }

    public void toggleCelVegallomasIndex() {
        celVegallomasIndex = (celVegallomasIndex == 0) ? 1 : 0;
    }

    public Buszvezeto getVezeto() { return vezeto; }

    public void setVezeto(Buszvezeto vezeto) { this.vezeto = vezeto; }

}