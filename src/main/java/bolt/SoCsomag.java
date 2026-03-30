package bolt;

import jatekos.Takarito;

public class SoCsomag extends FogyoAnyag {
    public SoCsomag(int mennyiseg, int ar) {
        super(mennyiseg, ar);
    }

    @Override
    public void atadVevonek(Takarito v) {
        if (v != null) {
            v.getEszkoztar().hozzaad("so", mennyiseg);
        }
    }

    @Override
    public String getNev() {
        return "SoCsomag";
    }
}