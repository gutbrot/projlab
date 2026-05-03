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

    public Takarito(int akcioPont, int penz) {
        super(akcioPont);
        this.penz = penz;
    }

    public Takarito(int akcioPont) {
        this(akcioPont, 100); // Alapértelmezett kezdőtőke
    }

    public Hokotro hokotrotValaszt() {
        if (iranyitottHokotrok.isEmpty()) {
            System.out.println("    [KUDARC] Nincs irányítható hókotró a listában!");
            return null;
        }

        System.out.println(">>> Elérhető hókotrók:");
        for (int i = 0; i < iranyitottHokotrok.size(); i++) {
            System.out.println("    [" + i + "] " + iranyitottHokotrok.get(i).getId());
        }

        System.out.print("? Válasszon indexet: ");
        try {
            int index = Integer.parseInt(scanner.nextLine());
            if (index >= 0 && index < iranyitottHokotrok.size()) {
                Hokotro kivalasztott = iranyitottHokotrok.get(index);
                akcioPontKezelo(); 
                return kivalasztott;
            }
        } catch (Exception e) {
            System.out.println(">>> Érvénytelen választás.");
        }
        
        return null;
    }

    public void hokotrotMozgat(Hokotro h) {
        if (getAkcioPont() > 0 && h != null) {
            akcioPontKezelo();
        }
    }

    public void kotrofejValt(Hokotro h) {
        if (getAkcioPont() > 0 && h != null) {
            KotroFej ujFej = eszkoztar.kiveszFej();
            if (ujFej != null) {
                h.fejcsere(ujFej);
                akcioPontKezelo();
            }
        }
    }

    public void vasarol(Bolt bolt, String termekNev) {
        if (getAkcioPont() > 0 && bolt != null) {
            boolean siker = bolt.vasarlas(this, termekNev);
            if (siker) {
                akcioPontKezelo();
            }
        }
    }

    public void penztKap(int p) {
        this.penz += p;
        System.out.println(">>> [FIZETÉS] A takarító " + p + " pénzt kapott! (Összesen: " + this.penz + ")");
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