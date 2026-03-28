package kotrofej;

/**
 * Hókotróra szerelhető kotró fej, amely képes a havat és a jeget felolvasztani  
 * megfelelő fogyóanyag mennyiség birtokában. 
 * Felelős a hó és jég útról való eltakarításáért.
 */
public class SarkanyFej extends KotroFej {
    
    /** A működéshez szükséges üzemanyag mennyisége. */
    private int biokerozinIgeny;

    @Override
    public void tisztit(Object cel, Object melle, Object ut) { // Olvasztási logika biokerozin felhasználásával
    }

    @Override
    public String getNev() { // Visszaadja a kotró fej nevét
        return "SarkanyFej";
    }
}