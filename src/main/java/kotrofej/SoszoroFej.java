package kotrofej;

/**
 * Hókotróra szerelhető kotró fej, amely képes a havat és a jeget 
 * felolvasztani megfelelő fogyóanyag mennyiség birtokában. 
 * Felelős a jégpáncél és a hó felolvasztásáért.
 */
public class SoszoroFej extends KotroFej {
    
    /** A sószóráshoz szükséges alapvető sómennyiség egységenként. */
    private int soIgeny;

    @Override
    public void tisztit(Object cel, Object melle, Object ut) {  // Sószórási logika, amely figyelembe veszi a sóigényt és a fogyóanyag mennyiségét
        
    }

    @Override
    public String getNev() { // Visszaadja a kotró fej nevét
        return "SoszoroFej";
    }
}