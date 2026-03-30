package kotrofej;

import terkep.*;

public class JegtoroFej extends KotroFej {

    public JegtoroFej(int ar) { super(ar); }

    @Override
    public void tisztit(Sav cel, Sav melle, Ut ut) {
        if (cel != null) cel.setJeges(false);
    }

    @Override
    public String getNev() { return "JegtoroFej"; }

    @Override
    public KotroFej getKotroFej() { return new JegtoroFej(getAr()); }
}