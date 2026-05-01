package jatekos;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import jarmu.Busz;

/**
 * A Buszvezeto osztály felelős a rendszerben lévő buszok irányításáért.
 */
public class Buszvezeto extends Jatekos {
    
    private final List<Busz> iranyithatoBuszok = new ArrayList<>();
    private int pont = 0;
    private Scanner scanner = new Scanner(System.in);
    
    public Buszvezeto(int akcioPont) {
        super(akcioPont);
    }

    public Buszvezeto() {
        this(3); 
    }
    
    public Busz busztValaszt() {
        if (iranyithatoBuszok.isEmpty()) {
            System.out.println("    [KUDARC] Nincs irányítható busz a listában!");
            return null;
        }

        System.out.println(">>> Irányítható buszok:");
        for (int i = 0; i < iranyithatoBuszok.size(); i++) {
            System.out.println("    [" + i + "] " + iranyithatoBuszok.get(i).getId());
        }

        System.out.print("? Válasszon indexet: ");
        try {
            int index = Integer.parseInt(scanner.nextLine());
            if (index >= 0 && index < iranyithatoBuszok.size()) {
                Busz kivalasztott = iranyithatoBuszok.get(index);
                return kivalasztott;
            }
        } catch (Exception e) {
            System.out.println(">>> Érvénytelen választás.");
        }
        
        return null;
    }

    public void busztMozgat(Busz busz) {
        if (getAkcioPont() > 0 && busz != null) {
            boolean siker = busz.mozgas(null); // A paramétert a tényleges vezérlő adja
            if (siker) {
                akcioPontKezelo();
            } else {
                System.out.println(">>> A busz mozgása sikertelen volt, AP nem került levonásra.");
            }
        }
    }

    public void pontotKap() {
        this.pont++;
        System.out.println(">>> [PONT] A buszvezető pontot kapott! (Összesen: " + this.pont + ")");
    }
    
    public void hozzaadBusz(Busz busz) {
        if (busz != null && !iranyithatoBuszok.contains(busz)) {
            iranyithatoBuszok.add(busz);
        }
    }

    public int getPont() { return pont; }
    public List<Busz> getIranyithatoBuszok() { return iranyithatoBuszok; }
}