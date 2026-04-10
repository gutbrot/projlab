package jatekos;

/**
 * A Jatekos egy absztrakt osztály, amely a játékban résztvevőket reprezentálja.
 * Kezeli a játékosok akciópontjait, amelyek korlátozzák az egy körben elvégezhető műveletek számát.
 */
public abstract class Jatekos {
    /** Tárolja, hogy egy játékosnak aktuálisan hány akciópontja van. */
    protected int akcioPont;
    
    /**
     * @param akcioPont A játékos induló akciópontjainak száma.
     */
    protected Jatekos(int akcioPont) {
        this.akcioPont = akcioPont;
    }
    
    /**
     * Ez a metódus hívódik meg, amikor a játékos befejezte a tevékenységét 
     * vagy elfogytak az akciópontjai. Nullázza a maradék pontokat.
     */
    public void korVege() {
        akcioPont = 0;
    }
    
    /**
     * Kezeli az akciópont rendszert. 
     * Levonja a megadott mennyiségű pontot a játékostól, de az érték nem mehet nulla alá.
     * @param p A levonandó akciópontok száma.
     */
    public void akcioPontKezelo(int p){
        akcioPont -= p;
        if (akcioPont < 0) akcioPont = 0;
    }
    
    /**
     * Visszaadja a játékos aktuálisan felhasználható akciópontjainak számát.
     * @return Az aktuális akciópontok mennyisége.
     */
    public int getAkcioPont() { return akcioPont; }

    /**
     * Prototípus kiegészítés: Lehetővé teszi az akciópontok közvetlen beállítását 
     * az új körök indításakor (Jatekter által használt metódus).
     * @param p Az új akciópont érték.
     */
    public void setAkcioPont(int p) {
        this.akcioPont = p;
    }
}