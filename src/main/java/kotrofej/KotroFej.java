package kotrofej;

import terkep.*;
import jatekos.*;
import bolt.*;
import eszkoztar.*;


public abstract class KotroFej implements IBoltiCikk {
    private int ar;

    protected KotroFej(int ar) {
        this.ar = ar;
    }

    public abstract void tisztit(Sav cel, Sav melle, Ut ut);
    public abstract String getNev();

    @Override
    public void atadVevonek(Takarito v) {
        if (v != null) {
            v.getEszkoztar().hozzaadFej(getKotroFej());
        }
    }

    @Override
    public int getAr() {
        return ar;
    }

    public abstract KotroFej getKotroFej();
}
