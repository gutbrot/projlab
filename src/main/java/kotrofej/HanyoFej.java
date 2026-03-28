package kotrofej;

/**
 * Hókotróra szerelhető kotró fej, amely képes havat és a feltört jeget a 
 * hókotró nyomvonalától több sávval arrébb szórni. Felelős a hó, 
 * illetve feltört jég útról való eltakarításáért.
 */
public class HanyoFej extends KotroFej {

    @Override
    public void tisztit(Object cel, Object melle, Object ut) {
        
    }

    @Override
    public String getNev() { // Visszaadja a kotró fej nevét
        return "HanyoFej";
    }
}