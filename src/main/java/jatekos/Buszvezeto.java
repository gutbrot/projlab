package jatekos;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import jarmu.Busz;

/**
 * A Buszvezeto osztály felelős a rendszerben lévő buszok irányításáért.
 * Interaktív módon engedi a választást a buszok közül és kezeli a pontszerzést.
 */
public class Buszvezeto extends Jatekos {
    /** A rendelkezésre álló buszok listája. */
    private final List<Busz> iranyithatoBuszok = new ArrayList<>();
    
    /** Az adott fordulók száma a játékban. */
    private int pont = 0;
    
    /** Scanner a konzolos beolvasáshoz a választáshoz. */
    private Scanner scanner = new Scanner(System.in);
    
    /**
     * Konstruktor a Buszvezető létrehozásához.
     * @param akcioPont A játékos induló akciópontjainak száma.
     */
    public Buszvezeto(int akcioPont) {
        super(akcioPont);
        System.out.println(">>> Buszvezető játékos létrehozva " + akcioPont + " akcióponttal.");
    }

    /** Alapértelmezett konstruktor 3 ponttal. */
    public Buszvezeto() {
        this(3); 
    }
    
    /**
     * Új busz hozzáadása a játékoshoz.
     */
    public void hozzaadBusz(Busz busz) {
        if (busz != null && !iranyithatoBuszok.contains(busz)) {
            iranyithatoBuszok.add(busz);
            System.out.println(">>> Új busz hozzáadva a buszvezetőhöz.");
        }
    }

    /**
     * Kilistázza a buszokat a konzolra, majd bekéri a választott indexét.
     * @return A választott busz példány vagy null.
     */
    public Busz buszValaszt() {
        if (iranyithatoBuszok.isEmpty()) {
            System.out.println(">>> Hiba: A buszvezetőnek nincs irányítható busza.");
            return null;
        }

        System.out.println(">>> Elérhető buszok listája:");
        for (int i = 0; i < iranyithatoBuszok.size(); i++) {
            Busz b = iranyithatoBuszok.get(i);
            // Kiírjuk a busz aktuális pozícióját (út neve) a könnyebb választáshoz
            String helyszin = (b.getPozicio() != null && b.getPozicio().getUt() != null) 
                              ? b.getPozicio().getUt().getNev() : "ismeretlen";
            System.out.println("    [" + i + "] Busz - Pozíció: " + helyszin);
        }

        System.out.print(">>> Válasszon egy buszt (írja be az indexet): ");
        try {
            int valasztas = Integer.parseInt(scanner.nextLine());
            if (valasztas >= 0 && valasztas < iranyithatoBuszok.size()) {
                Busz kivalasztott = iranyithatoBuszok.get(valasztas);
                System.out.println(">>> Buszvezető kiválasztotta a(z) " + valasztas + ". sorszámú buszt.");
                return kivalasztott;
            } else {
                System.out.println(">>> Hiba: Érvénytelen index.");
            }
        } catch (NumberFormatException e) {
            System.out.println(">>> Hiba: Kérjük, számot adjon meg!");
        }
        return null;
    }

    /**
     * A buszvezető mozgatja a buszt 1 akciópontért.
     * @param busz A mozgatni kívánt busz.
     * @return True, ha a mozgatás kezdeményezése sikeres.
     */
    public boolean buszMozgat(Busz busz) {
        if (busz == null || !iranyithatoBuszok.contains(busz)) {
            System.out.println(">>> Hiba: Érvénytelen busz a mozgatáshoz.");
            return false;
        }

        if (akcioPont <= 0) {
            System.out.println(">>> Mozgatás sikertelen: Elfogyott a buszvezető akciópontja.");
            return false;
        }
        
        akcioPontKezelo(1); // Egységes kezelés az ősosztályon keresztül
        System.out.println(">>> Busz mozgatása kezdeményezve. Felhasznált akciópont: 1. Maradék: " + akcioPont);
        return true;
    }

    /**
     * Pontszerzés a végállomás elérésekor.
     */
    public void pontotKap() {
        pont++;
        System.out.println(">>> A buszvezető pontot kapott a forduló teljesítéséért! Összpontszám: " + pont);
    }
    
    public int getPont() { return pont; }
    
    /** Visszaadja az irányítható buszok listáját. */
    public List<Busz> getIranyithatoBuszok() { 
        return iranyithatoBuszok; 
    }
}