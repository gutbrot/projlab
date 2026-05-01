package bolt;

import jarmu.Hokotro;
import jatekos.Takarito;

/**
 * A biokerozin fogyóanyagot reprezentáló osztály. 
 * A sárkány kotrófej (SarkanyFej) működéséhez szükséges jégolvasztó anyag.
 */
public class BiokerozinCsomag extends FogyoAnyag {
    
    /**
     * Konstruktor, ami beállítja a kerozin mennyiségét és árát 
     * az ősosztály (FogyoAnyag) konstruktorának segítségével.
     */
    public BiokerozinCsomag(int mennyiseg, int ar) {
        super(mennyiseg, ar);
    }

    /**
     * A tényleges vásárlási tranzakció befejezése. 
     * Itt dől el, hova kerül a megvásárolt anyag a UML diagram alapján.
     */
    @Override
    public void atadVevonek(Takarito v) {
        // 1. Biztonsági ellenőrzés: Létezik-e egyáltalán a vevő?
        if (v != null) {
            
            // 2. A UML alapján a Takarító maga nem tárol anyagot, a Hókotrók igen.
            // Lekérjük a Takarító által jelenleg aktívan irányított hókotrót.
            Hokotro aktivHokotro = v.hokotrotValaszt();
            
            // 3. Ellenőrizzük, hogy van-e aktív hókotrója, és annak van-e eszköztára.
            if (aktivHokotro != null && aktivHokotro.getEszkoztar() != null) {
                
                // 4. Ha minden rendben, a hókotró eszköztárához adjuk a kerozint.
                // Fontos a "biokerozin" string kulcs pontos használata az Eszkoztar miatt!
                aktivHokotro.getEszkoztar().hozzaad("biokerozin", mennyiseg);
                
                // Narratív visszajelzés a játékosnak a konzolon.
                System.out.println(">>> [BOLT] Sikeres vásárlás: " + mennyiseg + " egység biokerozin betöltve a Hókotró tartályába.");
            } else {
                // Ha a játékos úgy vásárol, hogy nincs beállítva hókotrója.
                System.out.println(">>> [BOLT HIBA] A játékosnak nincs aktív hókotrója, amibe tankolhatna!");
            }
        } else {
            // NullPointerException elkerülése, ha hibás a metódushívás a Bolt részéről.
            System.out.println(">>> [BOLT HIBA] Érvénytelen (null) játékos próbált vásárolni!");
        }
    }

    /**
     * Segédmetódus a menürendszer és a Bolt számára, hogy 
     * ki tudja írni a termék nevét a kínálatban.
     */
    public String getNev() {
        return "BiokerozinCsomag";
    }
}