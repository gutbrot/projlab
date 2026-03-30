package kotrofej;

import terkep.*;

public class SoszoroFej extends KotroFej {
    private int solgeny;

    public SoszoroFej(int ar, int solgeny) {
        super(ar);
        this.solgeny = solgeny;
    }

    @Override
    public void tisztit(Sav cel, Sav melle, Ut ut) {
    	if (cel != null) {
    	    cel.soOlvadas();
    	}
    }

    @Override
    public String getNev() { return "SoszoroFej"; }

    public int getSolgeny() { return solgeny; }

    @Override
    public KotroFej getKotroFej() { return new SoszoroFej(getAr(), solgeny); }
}