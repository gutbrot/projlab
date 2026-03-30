package bolt;

import jatekos.Takarito;

public abstract class FogyoAnyag implements IBoltiCikk {
    protected int mennyiseg;
    protected int ar;

    protected FogyoAnyag(int mennyiseg, int ar) {
        this.mennyiseg = Math.max(0, mennyiseg);
        this.ar = Math.max(0, ar);
    }

    public abstract String getNev();

    @Override
    public abstract void atadVevonek(Takarito v);

    @Override
    public int getAr() {
        return ar;
    }

    public FogyoAnyag getFogyo() {
        return this;
    }

    public int getMennyiseg() {
        return mennyiseg;
    }
}