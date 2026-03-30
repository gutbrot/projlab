package kotrofej;

import terkep.*;

public class HanyoFej extends KotroFej {

    public HanyoFej(int ar) {
        super(ar);
    }

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
    public String getNev() {
        return "HanyoFej";
    }

    @Override
    public KotroFej getKotroFej() {
        return new HanyoFej(getAr());
    }
}