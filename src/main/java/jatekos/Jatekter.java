package jatekos;

import java.util.ArrayList;
import java.util.List;
import jarmu.*;
import terkep.*;
import skeleton.Skeleton;

/**
 * A Jatekter osztály felelős a játékmenet globális vezérléséért.
 * Kezeli a játékosok akciópontjait, a körök váltását és a pontszámok nyilvántartását
 */
public class Jatekter {
    
    /** Tárolja a játékban résztvevő játékosok listáját. */
    private List<Jatekos> jatekosok = new ArrayList<>();
    
    /** Tárolja a játékban résztvevő járművek listáját. */
    private List<Jarmu> jarmuvek = new ArrayList<>();
    
    /** Implementálja a térképet, amin a játék folyik. */
    private Terkep terkep;

    /**
     * Konstruktor a játéktér létrehozásához.
     * @param terkep A játék térképe.
     */
    public Jatekter(Terkep terkep) {
        this.terkep = terkep;
    }

    /**
     * Ez a függvény valósítja meg a játék indítását
     */
    public void jatekStart() {
        Skeleton.functionCalled("jatekStart", this, "void");
        
        // Játékosok AP-jának inicializálása az indításkor
        for (Jatekos j : jatekosok) {
            j.setAkcioPont(3);
        }
        
        Skeleton.voidReturn();
    }

    /**
     * Az autók (NPC) mozgatásáért felelős függvény.
     * A dokumentáció aktivitásdiagramja alapján működik.
     */
    public void autoMozog() {
        Skeleton.functionCalled("autoMozog", this, "void");
        
        // Ciklus indítása a 'jarmuvek' listán
        for (Jarmu j : jarmuvek) {
            // A jármű 'Auto' típusú?
            if (j instanceof Auto) {
                Auto auto = (Auto) j;
                
                // Tud mozogni? (Nincs büntetőkörben?)
                if (auto.getMozgaskeptelenKorokSzama() == 0) {
                    // Navigáció alapján a következő lépés meghatározása
                    // Átmozgatás a célja felé
                    System.out.println("    [Jatekter] NPC Auto (" + auto.getId() + ") mozgatása...");
                    // Itt hívódna a navigáció és a j.mozgas(sav)
                } else {
                    // Büntetőkör csökkentése
                    auto.ujKor();
                }
            }
            // Ha nem NPC, nincs teendő
        }
        
        Skeleton.voidReturn();
    }

    /**
     * A játékosok (takarító vagy buszvezető) különböző lépéseit kezeli a saját körükben
     */
    public void jatekosLep() {
        Skeleton.functionCalled("jatekosLep", this, "void");
        // A játékosok interakcióinak vezérlése (Skeleton tesztek hívják)
        Skeleton.voidReturn();
    }

    /**
     * Minden játékban résztvevő fél után új kört indít.
     * Visszaállítódnak az akciópontok és kezdődik előről a játékmenet.
     */
    public void ujKor() {
        Skeleton.functionCalled("ujKor", this, "void");
        
        // 1. Időjárási események generálása
        if (terkep != null) {
            terkep.idojarasFrissites();
        }

        // 2. Autók mozgatása
        autoMozog();

        // 3. Ciklus indítása a 'jatekosok' listán
        for (Jatekos j : jatekosok) {
            // Játékos akciópontjainak újraosztása
            j.setAkcioPont(3);
        }

        // 4. Vezérlés átadása az első játékosnak
        System.out.println(">>> Vezérlés átadva az első játékosnak.");

        Skeleton.voidReturn();
    }

    // --- LISTAKEZELŐ ÉS GETTER METÓDUSOK ---

    public void hozzaadJatekos(Jatekos j) { 
        if (j != null) jatekosok.add(j); 
    }
    
    public void hozzaadJarmu(Jarmu j) { 
        if (j != null) jarmuvek.add(j); 
    }
    
    public List<Jatekos> getJatekosok() { return jatekosok; }
    public List<Jarmu> getJarmuvek() { return jarmuvek; }
}