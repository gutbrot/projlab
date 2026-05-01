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

    public Busz(String id, Lokacio pozicio, Lokacio v1, Lokacio v2) {
        super(pozicio);
        this.id = id;
        this.vegallomasok[0] = v1;
        this.vegallomasok[1] = v2;
    }

    public boolean vegallomasbaErt() {
        Lokacio aktualis = getPozicio();

        boolean match = false;
        for (Lokacio v : vegallomasok) {
            if (v != null && aktualis != null && v.getSav() == aktualis.getSav()) {
                match = true;
                break;
            }
        }

        if (match) {
            System.out.println(">>> [JÁRMŰ AKCIÓ] A(z) " + id + " busz sikeresen elérte a végállomást!");
            if (vezeto != null) {
                vezeto.pontotKap();
            }
            return true;
        }

        return false;
    }

    @Override
    public void utkozos() {
        System.out.println(">>> [BALESET] A(z) " + id + " busz megcsúszott és balesetet szenvedett!");
        mozgasKeptelen();
    }

    public String getId() { return id; }

    public void setVezeto(Buszvezeto vezeto) {
        this.vezeto = vezeto;
    }

    public Buszvezeto getVezeto() {
        return vezeto;
    }
}