package jatekos;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import jarmu.Busz;

// A Buszvezeto osztály felelős a rendszerben lévő buszok irányításáért.
public class Buszvezeto extends Jatekos {
    
    private final List<Busz> iranyithatoBuszok = new ArrayList<>();
    private int pont = 0;
    private Scanner scanner = new Scanner(System.in);
    
    //KONSTRUKTOROK
    public Buszvezeto(int akcioPont) {
        super(akcioPont);
    }

    public Buszvezeto() {
        this(3); 
    }
    
     // Lehetővé teszi a buszvezető számára, hogy kiválasszon egy irányítható buszt a listából.
    public Busz busztValaszt() {
        //Ellenőrizzük, hogy van-e irányítható busz a listában
        if (iranyithatoBuszok.isEmpty()) {
            System.out.println("    [KUDARC] Nincs irányítható busz a listában!");
            return null;
        }

        System.out.println(">>> Irányítható buszok:");
        //Kiírjuk az irányítható buszokat indexekkel együtt
        for (int i = 0; i < iranyithatoBuszok.size(); i++) {
            System.out.println("    [" + i + "] " + iranyithatoBuszok.get(i).getId());
        }

        //Kérjük a játékost, hogy válasszon egy buszt index alapján
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

    /**
     * Lehetővé teszi a buszvezető számára, hogy megpróbálja mozgatni a kiválasztott buszt.
     * Ha a mozgás sikeres, akkor levon egy akciópontot.
     */
    public void busztMozgat(Busz busz) {
        //Ellenőrizzük, hogy van-e elég akciópont és a busz nem null
        if (getAkcioPont() > 0 && busz != null) {
            boolean siker = busz.mozgas(null); //A paramétert a tényleges vezérlő adja
            if (siker) {
                akcioPontKezelo();
            } else {
                System.out.println(">>> A busz mozgása sikertelen volt, AP nem került levonásra.");
            }
        }
    }

    // Növeli a buszvezető pontjait, és kiírja az aktuális pontszámot.
    public void pontotKap() {
        this.pont++;
        System.out.println(">>> [PONT] A buszvezető pontot kapott! (Összesen: " + this.pont + ")");
    }
    
    // Hozzáad egy buszt az irányítható buszok listájához, ha az még nincs benne.
    public void hozzaadBusz(Busz busz) {
        //Ellenőrizzük, hogy a busz nem null és még nincs benne a listában
        if (busz != null && !iranyithatoBuszok.contains(busz)) {
            iranyithatoBuszok.add(busz);
        }
    }

    //GETTEREK
    public int getPont() { return pont; }
    public List<Busz> getIranyithatoBuszok() { return iranyithatoBuszok; }
}