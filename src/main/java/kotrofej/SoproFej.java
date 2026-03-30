package kotrofej;

<<<<<<< HEAD
/**
 * Hókotróra szerelhető kotró fej, amely képes a havat 
 * és a feltört jeget közvetlenül a hókotró nyomvonala mellé tolni. 
 * Felelős a hó, illetve feltört jég útról való eltakarításáért
 */
public class SoproFej extends KotroFej {

    @Override
    public void tisztit(Object cel, Object melle, Object ut) {
        
    }

    @Override
    public String getNev() {
        return "SoproFej";
    }
=======
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
>>>>>>> main
}