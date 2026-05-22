package bolt;

import jarmu.Hokotro;
import jatekos.Takarito;

/**
 * A biokerozin fogyóanyagot reprezentáló osztály. 
 * A sárkány kotrófej (SarkanyFej) működéséhez szükséges jégolvasztó anyag.
 */
public class BiokerozinCsomag extends FogyoAnyag {
    
    // Konstruktor, ami beállítja a kerozin mennyiségét és árát 
    public BiokerozinCsomag(int mennyiseg, int ar) {
        super(mennyiseg, ar);
    }

    // A vevőnek átadja a vásárolt terméket
    @Override
    public void atadVevonek(Takarito v) {
        //Létezik-e egyáltalán a vevő?
        if (v != null) {
            
            //Lekérjük a Takarító által jelenleg aktívan irányított hókotrót.
            Hokotro aktivHokotro = v.hokotrotValaszt();
            
            //Ellenőrizzük, hogy van-e aktív hókotrója, és annak van-e eszköztára.
            if (aktivHokotro != null && aktivHokotro.getEszkoztar() != null) {
                
                //Ha minden rendben, a hókotró eszköztárához adjuk a kerozint.
                aktivHokotro.getEszkoztar().hozzaad("biokerozin", mennyiseg);
                
                //Visszajelzés a játékosnak a konzolon.
                System.out.println(">>> [BOLT] Sikeres vásárlás: " + mennyiseg + " egység biokerozin betöltve a Hókotró tartályába.");
            } else {
                //Ha a játékos úgy vásárol, hogy nincs beállítva hókotrója.
                System.out.println(">>> [BOLT HIBA] A játékosnak nincs aktív hókotrója, amibe tankolhatna!");
            }
        } else {
            //NullPointerException elkerülése, ha hibás a metódushívás a Bolt részéről.
            System.out.println(">>> [BOLT HIBA] Érvénytelen (null) játékos próbált vásárolni!");
        }
    }

    // Név getterje
    public String getNev() {
        return "BiokerozinCsomag";
    }
}