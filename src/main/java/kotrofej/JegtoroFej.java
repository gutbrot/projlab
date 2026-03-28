package kotrofej;

/**
 * Hókotróra szerelhető kotró fej, amely képes a jeget feltörni. 
 * Felelős a jégpáncél feltöréséért. A feltört jeget nem tudja feltakarítani.
 */
public class JegtoroFej extends KotroFej {

    @Override
    public void tisztit(Object cel, Object melle, Object ut) {
        
    }

    @Override
    public String getNev() { // Visszaadja a kotró fej nevét
        return "JegtoroFej";
    }
}