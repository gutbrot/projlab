package jarmu;

import terkep.Lokacio;

public class Busz extends Jarmu {
    private final Lokacio[] vegallomasok = new Lokacio[2];

    public Busz(Lokacio elso, Lokacio masodik, Lokacio kezdo) {
        super(kezdo);
        vegallomasok[0] = elso;
        vegallomasok[1] = masodik;
    }

    public boolean vegallomasbaErt() {
        return pozicio == vegallomasok[0] || pozicio == vegallomasok[1];
    }

    @Override
    public void utkozos() {
        mozgasKeptelen();
    }

    public Lokacio[] getVegallomasok() { return vegallomasok.clone(); }
}