package kotrofej;

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
}