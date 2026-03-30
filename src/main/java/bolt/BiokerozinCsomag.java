package bolt;

import jatekos.Takarito;

public class BiokerozinCsomag extends FogyoAnyag {
    public BiokerozinCsomag(int mennyiseg, int ar) {
        super(mennyiseg, ar);
    }

    @Override
    public void atadVevonek(Takarito v) {
        if (v != null) {
            v.getEszkoztar().hozzaad("biokerozin", mennyiseg);
        }
    }

    @Override
    public String getNev() {
        return "BiokerozinCsomag";
    }
}