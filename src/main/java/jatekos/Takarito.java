package jatekos;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import eszkoztar.Eszkoztar;
import jarmu.Hokotro;
import kotrofej.*;
import bolt.Bolt;

/**
 * A hókotrókat irányító játékosokat reprezentáló osztály.
 */
public class Takarito extends Jatekos {
    private List<Hokotro> iranyitottHokotrok = new ArrayList<>();
    private Eszkoztar eszkoztar = new Eszkoztar();
    private int penz;
    
    // Scanner a konzolos beolvasáshoz a választáshoz
    private Scanner scanner = new Scanner(System.in);

    public Takarito(int akcioPont, int penz) {
        super(akcioPont);
        this.penz = penz;
        System.out.println(">>> Takarító játékos létrehozva " + akcioPont + " akcióponttal és " + penz + " pénzzel.");
    }

    public Takarito() {
        this(3, 0);
    }
    
    /**
     * Kilistázza a hókotrókat a konzolra, majd bekéri a választott indexét.
     * @return A választott hókotró példány vagy null.
     */
    public Hokotro hokotrotValaszt() {
        if (iranyitottHokotrok.isEmpty()) {
            System.out.println(">>> Hiba: A takarítónak nincs irányítható hókotrója.");
            return null;
        }

        System.out.println(">>> Elérhető hókotrók listája:");
        for (int i = 0; i < iranyitottHokotrok.size(); i++) {
            Hokotro h = iranyitottHokotrok.get(i);
            String fejNev = (h.getFelszereltFej() != null) ? h.getFelszereltFej().getNev() : "nincs fej";
            System.out.println("    [" + i + "] Hókotró - Pozíció: " + h.getPozicio().getUt().getNev() + " - Felszerelés: " + fejNev);
        }

        System.out.print(">>> Válasszon egy hókotrót (írja be az indexet): ");
        try {
            int valasztas = Integer.parseInt(scanner.nextLine());
            if (valasztas >= 0 && valasztas < iranyitottHokotrok.size()) {
                Hokotro kivalasztott = iranyitottHokotrok.get(valasztas);
                System.out.println(">>> Takarító kiválasztotta a(z) " + valasztas + ". sorszámú hókotrót.");
                return kivalasztott;
            } else {
                System.out.println(">>> Hiba: Érvénytelen index.");
            }
        } catch (NumberFormatException e) {
            System.out.println(">>> Hiba: Kérjük, számot adjon meg!");
        }
        return null;
    }

    public boolean hokotroMozgat(Hokotro h) {
        if (h == null || !iranyitottHokotrok.contains(h)) {
            System.out.println(">>> Hiba: Érvénytelen hókotró a mozgatáshoz.");
            return false;
        }
        if (akcioPont <= 0) {
            System.out.println(">>> Mozgatás sikertelen: Elfogyott a takarító akciópontja.");
            return false;
        }
        
        akcioPont--;
        System.out.println(">>> Hókotró mozgatása kezdeményezve. Felhasznált akciópont: 1. Maradék: " + akcioPont);
        return true;
    }

    public boolean kotrofejValt(Hokotro h) {
        if (h == null || akcioPont <= 0) {
            System.out.println(">>> Fejcsere sikertelen: Nincs elég akciópont vagy érvénytelen jármű.");
            return false;
        }
        
        KotroFej uj = eszkoztar.kiveszFej();
        if (uj == null) {
            System.out.println(">>> Fejcsere sikertelen: Nincs elérhető kotrófej az eszköztárban.");
            return false;
        }
        
        h.fejcsere(uj);
        akcioPont--;
        System.out.println(">>> Sikeres fejcsere a hókotrón. Új fej: " + uj.getNev() + ". Maradék AP: " + akcioPont);
        return true;
    }

    public boolean vasarol(Bolt bolt, String termekNev) {
        if (bolt == null || akcioPont <= 0) {
            System.out.println(">>> Vásárlás sikertelen: Nincs elég akciópont.");
            return false;
        }
        
        int penzElotte = penz;
        boolean siker = bolt.vasarlas(this, termekNev);
        
        if (siker) {
            akcioPont--;
            System.out.println(">>> Sikeres vásárlás a boltban: " + termekNev + ". Kifizetve: " + (penzElotte - penz) + ". Maradék AP: " + akcioPont);
        } else {
            System.out.println(">>> Vásárlás sikertelen: Nincs elég pénz vagy a termék (" + termekNev + ") nem érhető el.");
        }
        return siker;
    }

    public void penztKap(int p){
        this.penz += p;
        System.out.println(">>> Takarító pénzt kapott (" + p + "). Új egyenleg: " + penz);
    }
    
    public void hozzaadHokotro(Hokotro h) {
        if (h != null && !iranyitottHokotrok.contains(h)) {
            iranyitottHokotrok.add(h);
            System.out.println(">>> Új hókotró hozzáadva a takarítóhoz.");
        }
    }
    
    public List<Hokotro> getIranyitottHokotrok() { return iranyitottHokotrok; }
    public Eszkoztar getEszkoztar() { return eszkoztar; }
    public int getPenz() { return penz; }
}