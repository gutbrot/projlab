package jatekos;

import skeleton.Skeleton;

/**
 * A Jatekos egy absztrakt osztály, amely a játékban résztvevőket reprezentálja. 
 * Kezeli a játékosok akciópontjait, amelyek korlátozzák az egy körben 
 * elvégezhető műveletek számát. 
 * Biztosítja a körök váltását és a pontszámok nyilvántartását.
 */
public abstract class Jatekos {
    
    /** Tárolja, hogy egy játékosnak hány akciópontja van */
    private int akcioPont;
    
    /**
     * Konstruktor a játékos példányosításához.
     * 
     * @param akcioPont A játékos induló akciópontjainak száma.
     */
    protected Jatekos(int akcioPont) {
        this.akcioPont = Math.max(0, akcioPont);
    }
    
    /**
     * Ez a metódus hívódik meg, amikor a játékos befejezte a 
     * tevékenységét vagy elfogytak az akciópontjai.
     */
    public void korVege() {
        // A kör végén a maradék akciópontok elvesznek
        this.akcioPont = 0;
        Skeleton.voidReturn();
    }
    
    /**
     * Kezeli az akciópont rendszert. 
     * Minden cselekvésnél levon a játékostól 1 pontot.
     * 
     * A dokumentáció diagramja alapján ellenőrzi, hogy elfogyott-e 
     * az akciópont, és ha igen, jelzi a kör végét.
     */
    public void akcioPontKezelo() {
        if (this.akcioPont > 0) {
            this.akcioPont--;
            
            // Ha elfogyott az akciópont, a kör véget ér
            if (this.akcioPont == 0) {
                this.korVege();
            }
        }
    }
    
    /**
     * Visszaadja a játékos aktuálisan felhasználható akciópontjainak számát.
     * 
     * @return Az aktuális akciópontok mennyisége.
     */
    public int getAkcioPont() { 
        return akcioPont; 
    }

    /**
     * Lehetővé teszi az akciópontok beállítását (pl. új kör indításakor).
     * 
     * @param p Az új akciópont érték.
     */
    public void setAkcioPont(int p) {
        this.akcioPont = Math.max(0, p);
    }
}