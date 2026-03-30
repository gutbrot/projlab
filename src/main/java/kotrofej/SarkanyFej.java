package kotrofej;

import terkep.*;

public class SarkanyFej extends KotroFej {
    private int biokerozinIgeny;

    public SarkanyFej(int ar, int biokerozinIgeny) {
        super(ar);
        this.biokerozinIgeny = biokerozinIgeny;
    }

    @Override
    public void tisztit(Sav cel, Sav melle, Ut ut) {
        if (cel != null) {
            cel.setHo(0);
            cel.setJeges(false);
        }
    }

    @Override
    public String getNev() { return "SarkanyFej"; }

    public int getBiokerozinIgeny() { return biokerozinIgeny; }

    @Override
    public KotroFej getKotroFej() { return new SarkanyFej(getAr(), biokerozinIgeny); }
}
