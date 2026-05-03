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

    public Takarito(int akcioPont, int penz) {
        super(akcioPont);
        this.penz = penz;
        //iranyitottHokotrok.add(new Hokotro("H_alap_" + alapHokotroSzamlalo++, null, new SoproFej(0)));
    }
    
    public Takarito(int akcioPont) {
        this(akcioPont, 100); // Alapértelmezett kezdőtőke
    }

    public Hokotro hokotrotValaszt() {
        if (iranyitottHokotrok.isEmpty()) {
            System.out.println("    [KUDARC] Nincs iranyithato hokotro a listaban!");
            return null;
        }

        System.out.println(">>> Elerheto hokotrok:");
        for (int i = 0; i < iranyitottHokotrok.size(); i++) {
            System.out.println("    [" + i + "] " + iranyitottHokotrok.get(i).getId());
        }

        System.out.print("? Valasszon indexet: ");
        try {
            int index = Integer.parseInt(scanner.nextLine());
            if (index >= 0 && index < iranyitottHokotrok.size()) {
                return iranyitottHokotrok.get(index);
            }
        } catch (Exception e) {
            System.out.println(">>> Ervenytelen valasztas.");
        }

        return null;
    }

    public void hokotrotMozgat(Hokotro h) {
        if (getAkcioPont() > 0 && h != null) {
            akcioPontKezelo();
        }
    }

    public boolean kotrofejValt(Hokotro h, String fejNev) {
        if (getAkcioPont() <= 0) {
            return false;
        }

        if (h == null || fejNev == null) {
            return false;
        }

        if (!iranyitottHokotrok.contains(h)) {
            return false;
        }

        KotroFej ujFej = eszkoztar.kiveszFej(fejNev);

        if (ujFej == null) {
            return false;
        }

        KotroFej regiFej = h.getFelszereltFej();
        if (regiFej != null) {
            eszkoztar.hozzaadFej(regiFej);
        }

        h.fejcsere(ujFej);
        akcioPontKezelo();

        return true;
    }

    public void vasarol(Bolt bolt, String termekNev) {
        if (getAkcioPont() > 0 && bolt != null) {
            boolean siker = bolt.vasarlas(this, termekNev);
            if (siker) { akcioPontKezelo(); }
        }
    }

    public void penztKap(int p) {
        this.penz += p;
        System.out.println(">>> [FIZETES] A takaríto " + p + " penzt kapott! (Osszesen: " + this.penz + ")");
    }
    
    public void penztLevon(int osszeg) {
        this.penz -= osszeg;
    }

    public void hozzaadHokotro(Hokotro h) {
        if (h != null) iranyitottHokotrok.add(h);
    }

    public int getPenz() { return penz; }
    public Eszkoztar getEszkoztar() { return eszkoztar; }
    public List<Hokotro> getIranyitottHokotrok() { return iranyitottHokotrok; }
}