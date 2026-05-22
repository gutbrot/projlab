package jatekos;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import eszkoztar.Eszkoztar;
import jarmu.Hokotro;
import kotrofej.*;
import bolt.Bolt;

/**
 * A Takarito osztály felelős a rendszerben lévő hókotrók irányításáért.
 */
public class Takarito extends Jatekos {

    private List<Hokotro> iranyitottHokotrok = new ArrayList<>();
    private int penz;
    private Eszkoztar eszkoztar = new Eszkoztar();
    private Scanner scanner = new Scanner(System.in);
    private static int alapHokotroSzamlalo = 1;

    //KONSTRUKTOROK
    public Takarito(int akcioPont, int penz) {
        super(akcioPont);
        this.penz = penz;
    }
    
    public Takarito(int akcioPont) {
        this(akcioPont, 100); // Alapértelmezett kezdőtőke
    }

    //GETTEREK
    public int getPenz() { return penz; }
    public Eszkoztar getEszkoztar() { return eszkoztar; }
    public List<Hokotro> getIranyitottHokotrok() { return iranyitottHokotrok; }

    /**
     * Lehetővé teszi a játékos számára, hogy kiválasszon egy irányítható hókotrót.
     */
    public Hokotro hokotrotValaszt() {
        
        if (this.aktivJarmu instanceof Hokotro) {
            return (Hokotro) this.aktivJarmu;
        }

        if (iranyitottHokotrok.size() == 1) {
            this.aktivJarmu = iranyitottHokotrok.get(0);
            return iranyitottHokotrok.get(0);
        }

        //Csak azokat a hókotrókat listázza, amelyek irányíthatók
        if (iranyitottHokotrok.isEmpty()) {
            System.out.println("    [KUDARC] Nincs iranyithato hokotro a listaban!");
            return null;
        }

        System.out.println(">>> Elerheto hokotrok:");
        //Csak az irányítható hókotrókat listázza
        for (int i = 0; i < iranyitottHokotrok.size(); i++) {
            System.out.println("    [" + i + "] " + iranyitottHokotrok.get(i).getId());
        }

        //Kiválasztás index alapján
        String sor = skeleton.Console.readLine("? Valasszon indexet: ");
        try {
            int index = Integer.parseInt(sor.trim());
            if (index >= 0 && index < iranyitottHokotrok.size()) {
                return iranyitottHokotrok.get(index);
            }
        } catch (Exception e) {
            System.out.println(">>> Ervenytelen valasztas.");
        }
        return null;
        }

    /**
     * Akciópontot használ a hókotró mozgatására.
     */
    public void hokotrotMozgat(Hokotro h) {
        //Csak akkor engedélyezi a mozgást, ha van elég akciópont és a hókotró nem null
        if (getAkcioPont() > 0 && h != null) {
            akcioPontKezelo();
        }
    }

    /**
     * Lehetővé teszi a játékos számára, hogy egy új kotrófejet szereljen fel egy irányítható hókotróra.
     */
    public boolean kotrofejValt(Hokotro h, String fejNev) {
        //Csak akkor engedélyezi a fejváltást, ha van elég akciópont, a hókotró és a fej neve nem null, és a hókotró irányítható
        if (getAkcioPont() <= 0) {
            return false;
        }

        //Csak akkor engedélyezi a fejváltást, ha a hókotró és a fej neve nem null
        if (h == null || fejNev == null) {
            return false;
        }

        //Csak akkor engedélyezi a fejváltást, ha a hókotró irányítható
        if (!iranyitottHokotrok.contains(h)) {
            return false;
        }

        //Megpróbálja kivenni a kívánt fej nevű kotrófejet az eszköztárból
        KotroFej ujFej = eszkoztar.kiveszFej(fejNev);

        if (ujFej == null) {
            return false;
        }

        //Ha a hókotró már rendelkezik felszerelt fejjel, akkor visszahelyezi azt az eszköztárba
        KotroFej regiFej = h.getFelszereltFej();
        if (regiFej != null) {
            eszkoztar.hozzaadFej(regiFej);
        }

        //Felszereli az új fejet a hókotróra és kezeli az akciópontot
        h.fejcsere(ujFej);
        akcioPontKezelo();

        return true;
    }


    /**
     * Lehetővé teszi a játékos számára, hogy egy terméket vásároljon egy boltból.
     */
    public void vasarol(Bolt bolt, String termekNev) {
        //Csak akkor engedélyezi a vásárlást, ha van elég akciópont, a bolt nem null, és a termék neve nem null
        if (getAkcioPont() > 0 && bolt != null) {
            boolean siker = bolt.vasarlas(this, termekNev);
            if (siker) { akcioPontKezelo(); }
        }
    }

    /**
     * Lehetővé teszi a játékos számára, hogy egy terméket eladjon egy boltnak.
     */
    public void penztKap(int p) {
        this.penz += p;
        System.out.println(">>> [FIZETES] A takaríto " + p + " penzt kapott! (Osszesen: " + this.penz + ")");
    }
    
    /**
     * Lehetővé teszi a játékos számára, hogy egy terméket eladjon egy boltnak.
     */
    public void penztLevon(int osszeg) {
        this.penz -= osszeg;
    }

    /**
     * Lehetővé teszi a játékos számára, hogy egy terméket eladjon egy boltnak.
     */
    public void hozzaadHokotro(Hokotro h) {
        if (h != null) iranyitottHokotrok.add(h);
    }

}