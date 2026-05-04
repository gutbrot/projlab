package jarmu;

import terkep.Lokacio;
import terkep.Sav;
import jatekos.Buszvezeto;

/**
 * A Busz osztály egy járművet reprezentál.
 * Elsődleges felelőssége a kijelölt megállóhelyek közötti közlekedés.
 */
public class Busz extends Jarmu {
    
    private final String id;
    private final Lokacio[] vegallomasok = new Lokacio[2];
    private Buszvezeto vezeto;

    //Konstruktor a busz létrehozásához, megadva az azonosítót, kezdő pozíciót és a két végállomást.
    public Busz(String id, Lokacio pozicio, Lokacio v1, Lokacio v2) {
        super(pozicio);
        this.id = id;
        this.vegallomasok[0] = v1;
        this.vegallomasok[1] = v2;
    }

    /**
     * A busz mozgását kezeli a megadott irányban.
     * Először megpróbálja a kívánt irányba mozogni, majd ellenőrzi, hogy elérte-e valamelyik végállomást.
     * Ha igen, akkor pontot ad a vezetőnek.
     */
    public boolean vegallomasbaErt() {
        Lokacio aktualis = getPozicio();

        boolean match = false;
        // Ellenőrizzük, hogy a busz aktuális pozíciója megegyezik-e valamelyik végállomás pozíciójával.
        for (Lokacio v : vegallomasok) {
            if (v != null && aktualis != null && v.getSav() == aktualis.getSav()) {
                match = true;
                break;
            }
        }

        // Ha elérte valamelyik végállomást, akkor pontot adunk a vezetőnek.
        if (match) {
            System.out.println(">>> [JÁRMŰ AKCIÓ] A(z) " + id + " busz sikeresen elérte a végállomást!");
            if (vezeto != null) {
                vezeto.pontotKap();
            }
            return true;
        }

        return false;
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

    
    public Buszvezeto getVezeto() {
        return vezeto;
    }

    public void setVezeto(Buszvezeto vezeto) {
        this.vezeto = vezeto;
    }

}