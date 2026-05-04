package bolt;

import jarmu.Hokotro;
import jatekos.Takarito;

/**
 * A zúzalékot reprezentáló osztály. 
 * A zúzottkő-szóró (ZuzottFej) fejhez szükséges, hogy az autók ne csússzanak meg a jégen.
 */
public class zuzalekCsomag extends FogyoAnyag {

    /**
     * Konstruktor az ár és a mennyiség beállításához.
     */
    public zuzalekCsomag(int mennyiseg, int ar) {
        super(mennyiseg, ar);
    }

    /**
     * A vásárláskor lefutó átadási logika, amely a zúzalékot betölti a hókotróba.
     */
    @Override
    public void atadVevonek(Takarito v) {
        //Létező vevő ellenőrzése.
        if (v != null) {
            //Először elkérjük a játékos által használt Hókotrót.
            Hokotro aktivHokotro = v.hokotrotValaszt();
            
            //Ha van aktív hókotró, ami rendelkezik eszköztárral...
            if (aktivHokotro != null && aktivHokotro.getEszkoztar() != null) {
                
                //...akkor a "zuzalek" kulcsszóval megnöveljük benne a mennyiséget.
                aktivHokotro.getEszkoztar().hozzaad("zuzalek", mennyiseg);
                
                System.out.println(">>> [BOLT] Sikeres vásárlás: " + mennyiseg + " egység zúzalék betöltve a Hókotró eszköztárába.");
            } else {
                System.out.println(">>> [BOLT HIBA] A játékosnak nincs aktív hókotrója a zúzalék fogadásához!");
            }
        } else {
            System.out.println(">>> [BOLT HIBA] Érvénytelen (null) játékos próbált vásárolni!");
        }
    }

    /**
     * Visszaadja a termék nevét listázáshoz.
     */
    public String getNev() {
        return "ZuzalekCsomag";
    }
}