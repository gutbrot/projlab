package kotrofej;

import terkep.*;

public class SoproFej extends KotroFej {

    public SoproFej(int ar) { super(ar); }

    @Override
    public void tisztit(Sav cel, Sav melle, Ut ut) {
        if (cel == null) return;
        int ho = cel.getHo();
        cel.setHo(0);
        if (melle != null) {
            melle.setHo(melle.getHo() + ho);
        }
    }

    @Override
    public String getNev() { return "SoproFej"; }

    @Override
    public KotroFej getKotroFej() { return new SoproFej(getAr()); }
}