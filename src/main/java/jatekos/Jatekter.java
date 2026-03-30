package jatekos;

import java.util.ArrayList;
import java.util.List;

import jarmu.*;
import terkep.*;

/**
 * A Jatekter osztály feladata a környezet globális kezelése és a játékmenet vezérlése. 
 * Felelős a térkép, a játékosok és a bolt objektumainak tárolásáért és inicializálásáért. 
 * Ez az osztály indítja el és menedzseli a köröket, valamint felügyeli a játék állapotát. 
 */
public class Jatekter {
    /** Tárolja a játékban résztvevő játékosok listáját. */
    private final List<Jatekos> jatekosok = new ArrayList<>();
    /** Tárolja a játékban résztvevő járművek listáját.  */
    private final List<Jarmu> jarmuvek = new ArrayList<>();
    /** Implementálja a térképet, amin a játék folyik.  */
    private final Terkep terkep;

    /**
     * Konstruktor a játéktér létrehozásához.
     * A Terkep osztály kompozícióval kapcsolódik hozzá: amennyiben a Jatekter megszűnik, 
     * a hozzá tartozó térkép objektum is megszűnik. 
     * @param terkep A pályát és az úthálózatot tároló objektum. 
     */
    public Jatekter(Terkep terkep) {
        this.terkep = terkep;
    }

    /**
     * Ez a függvény valósítja meg a játék indítását. 
     * A játékosok ilyenkor kapják meg az akciópontjaikat a kezdéshez.
     */
    public void jatekStart() {
        for (Jatekos j : jatekosok) {
            j.akcioPontKezelo(2);
        }
    }

    /**
     * Az autók (NPC járművek) mozgatásáért felelős függvény. 
     */
    public void autoMozgo() {
        for (Jarmu j : jarmuvek) {
            if (j instanceof Auto) {
                // Az autók mozgási logikájának helye
            }
        }
    }

    /**
     * A játékosok (takarító vagy buszvezető) különböző lépéseit kezeli a saját körükben. 
     */
    public void jatekosLep() {
        for (Jatekos j : jatekosok) {
            j.korVege();
        }
    }

    /**
     * Minden játékban résztvevő fél után új kört indít. 
     * Ilyenkor frissülnek a környezeti változók (időjárás) és visszaállítódnak az akciópontok. 
    public void ujKor() {
        terkep.idojarasFrissites();
        for (Jatekos j : jatekosok) {
            j.akcioPontKezelo(2);
        }
    }

    /**
     * Új játékos hozzáadása a játéktérhez.
     * @param j A regisztrálandó játékos objektum.
     */
    public void hozzaadJatekos(Jatekos j) { if (j != null) jatekosok.add(j); }
    
    /**
     * Új jármű hozzáadása a játéktérhez.
     * @param j A regisztrálandó jármű objektum.
     */
    public void hozzaadJarmu(Jarmu j) { if (j != null) jarmuvek.add(j); }
    
    /** @return A játékban részt vevő összes irányítható szereplő listája.  */
    public List<Jatekos> getJatekosok() { return jatekosok; }
    
    /** @return A játéktéren tartózkodó összes jármű listája. */
    public List<Jarmu> getJarmuvek() { return jarmuvek; }
    
    /** @return A játéktérhez tartozó térkép objektum. */
    public Terkep getTerkep() { return terkep; }
}