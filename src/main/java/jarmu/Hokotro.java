package jarmu;

import jatekos.Takarito;
import kotrofej.KotroFej;
import terkep.*;

public class Hokotro extends Jarmu{
    private KotroFej felszereltFej;
    
    public Hokotro(Lokacio pozicio, KotroFej felszereltFej) {
        super(pozicio);
        this.felszereltFej = felszereltFej;
    }
    
    public void atadVevonek(Takarito v) {
        if (v != null) {
            v.hozzaadHokotro(this);
        }
    }

    public void fejcsere(KotroFej fej) {
        if (fej != null) {
            this.felszereltFej = fej;
        }
    }
    
    public void takarit(Terkep terkep) {
        if (pozicio == null || felszereltFej == null) return;
        Sav cel = pozicio.getSav();
        Ut ut = pozicio.getUt();
        Sav melle = null;
        if (pozicio.getSzakasz() != null) {
            int idx = pozicio.getSzakasz().indexOf(cel);
            if (idx > 0) melle = pozicio.getSzakasz().get(idx - 1);
            else if (idx + 1 < pozicio.getSzakasz().size()) melle = pozicio.getSzakasz().get(idx + 1);
        }
        felszereltFej.tisztit(cel, melle, ut);
    }

    @Override
    public void utkozos() {
        mozgasKeptelen();
    }

    public Hokotro getHokotro() { return this; }
    public KotroFej getFelszereltFej() { return felszereltFej; }
}
