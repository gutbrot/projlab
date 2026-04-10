package jatekos;

import java.util.ArrayList;
import java.util.List;

import jarmu.*;
import terkep.*;

/**
 * A Jatekter osztály feladata a környezet globális kezelése és a játékmenet vezérlése. 
 * Felelős a térkép, a játékosok és a járművek tárolásáért. 
 */
public class Jatekter {
    /** Tárolja a játékban résztvevő játékosokat (Takarito, Buszvezeto). */
    private List<Jatekos> jatekosok = new ArrayList<>();
    /** Tárolja az összes járművet (Auto, Busz, Hokotro). */
    private List<Jarmu> jarmuvek = new ArrayList<>();
    /** A játék terepe, úthálózata. */
    private Terkep terkep;

    /**
     * Konstruktor a játéktér létrehozásához.
     * @param terkep A pályát és az úthálózatot tároló objektum. 
     */
    public Jatekter(Terkep terkep) {
        this.terkep = terkep;
    }

    /**
     * Elindítja a játékot és kiosztja a kezdő akciópontokat.
     */
    public void jatekStart() {
        System.out.println(">>> Jatek inditasa: akcioPontok kiosztasa.");
        for (Jatekos j : jatekosok) {
            // A Jatekos osztályban lévő akcioPontKezelo levonásra szolgál,
            // de mivel az akcioPont mező protected, a csomagon belül 
            // közvetlenül beállíthatjuk a kezdőértéket.
            j.akcioPont = 3; 
        }
    }

    /**
     * Az NPC autók automatikus mozgatása a körök végén.
     */
    public void autoMozgo() {
        System.out.println(">>> NPC jarmuvek mozgasa...");
        for (Jarmu j : jarmuvek) {
            if (j instanceof Auto) {
                Auto auto = (Auto) j;
                // Az Auto két végállomással rendelkezik.
                // Itt hívhatjuk meg a Terkep.jarmuMozgatas-t, 
                // ami ellenőrzi az útviszonyokat (hó, jég) és a foglaltságot.
                
                // Prototípus szinten egy egyszerűbb MI-t implementálunk, 
                // ami a jelenlegi sávja utáni következő sávot próbálja megszerezni.
                Lokacio akt = auto.getPozicio();
                if (akt != null && akt.getSzakasz() != null) {
                    // Itt a Navigacio.legrovidebbUt(hova) használható a jövőben.
                }
            }
        }
    }

    /**
     * Lezárja az aktuális játékos körét és nullázza a pontjait.
     */
    public void jatekosLep() {
        for (Jatekos j : jatekosok) {
            System.out.println(">>> Jatekos koranak lezarasa...");
            j.korVege(); // Nullázza a maradék pontokat.
        }
    }

    /**
     * Új kör indítása: időjárás frissítése és akciópontok újratöltése.
     */
    public void ujKor() {
        System.out.println("\n--- UJ KOR KEZDODIK ---");
        
        // 1. Környezeti hatások frissítése (havazás minden nem-alagút úton)
        terkep.idojarasFrissites();
        System.out.println(">>> Az idojaras frissult, a ho esett az utakra.");

        // 2. Akciópontok visszaállítása a játékosoknak
        for (Jatekos j : jatekosok) {
            j.akcioPont = 3; 
        }

        // 3. NPC-k léptetése (mivel ők nem játékosok, a kör végén automatikusan mozognak)
        autoMozgo();
    }

    /** Új játékos hozzáadása. */
    public void hozzaadJatekos(Jatekos j) { if (j != null) jatekosok.add(j); }
    
    /** Új jármű hozzáadása. */
    public void hozzaadJarmu(Jarmu j) { if (j != null) jarmuvek.add(j); }
    
    /** @return Jatekosok listaja. */
    public List<Jatekos> getJatekosok() { return jatekosok; }
    
    /** @return Jarmuvek listaja. */
    public List<Jarmu> getJarmuvek() { return jarmuvek; }
    
    /** @return Terkep objektum sávjaival és útjaival. */
    public List<Ut> getPalyaterv() { return terkep.getTeljesHalozat(); }
}