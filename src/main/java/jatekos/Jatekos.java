package jatekos;

import java.util.ArrayList;
import java.util.List;

import jarmu.*;
import terkep.*;
import bolt.*;
import kotrofej.*;

/**
 * A Jatekos egy absztrakt osztály, amely a játékban résztvevőket reprezentálja. 
 */
public abstract class Jatekos {
    
    private int akcioPont;
    public List<Jatekos> jatekosok = new ArrayList<>();
    public List<Jarmu> jarmuvek = new ArrayList<>();
    public int aktualisJatekosIndex = 0;
    public Jarmu aktivJarmu = null;

    private String nev = null;

    private Jatekter jatekter;

    //KONSTRUKTOR
    protected Jatekos(int akcioPont) {
        this.akcioPont = Math.max(0, akcioPont);
    }

    //SETTEREK ÉS GETTEREK
    public void setJatekter(Jatekter jatekter) {
        this.jatekter = jatekter;
    }
    
    public int getAkcioPont() { 
        return akcioPont; 
    }

    public void setAkcioPont(int p) {
        this.akcioPont = Math.max(0, p);
    }

    public String getNev() {
        return nev;
    }

    public void setNev(String nev) {
        this.nev = nev;
    }
    
    /**
     * Ez a metódus felelős a kör végének kezeléséért. 
     * Amikor egy játékos befejezi a körét, ez a metódus hívódik meg, 
     * amely nullázza az akciópontokat, és előkészíti a következő játékost a kör megkezdésére.
     */
    public void forduloVege(Jatekos aktivJatekos) {
        this.akcioPont = 0;
        aktivJarmu = null;
        System.out.println(">>> " + aktivJatekos.getNev() + " befejezte a koret.");
        
        aktualisJatekosIndex++;
        //Ha az aktuális játékos indexe eléri a játékosok számát, akkor új fordulót kezdünk.
        if (aktualisJatekosIndex >= jatekosok.size()) {
            jatekter.ujKor();
            aktualisJatekosIndex = 0;
        }
    }

    /**
     * Ez a metódus felelős a kör végének kezeléséért, amikor a játékos úgy dönt, hogy nem használja fel az összes akciópontját. 
     * Ez lehetővé teszi a játékos számára, hogy "skippelje" a maradék akciópontjait, és ezzel gyorsítsa a játék menetét.
     */
    public void korVege() {
        //Ha a játékosnak volt még AP-ja, de kiadta a parancsot, akkor is nullázzuk, így skippeli a körét.
        if (this.akcioPont > 0) {
            System.out.println("    >>> [INFO] Játékos skippelte a maradék " + this.akcioPont + " akciópontját.");
        }
        this.akcioPont = 0;
    }
    
    /**
     * Ez a metódus kezeli az akciópontok csökkenését, amikor a játékos egy akciót hajt végre. 
     * Minden egyes akció végrehajtása után ez a metódus hívódik meg, hogy csökkentse az akciópontokat, és ellenőrizze, hogy a játékosnak maradt-e még akciópontja.
     */
    public void akcioPontKezelo() {
        if (this.akcioPont > 0) {
            this.akcioPont--;
        }
    }
    
}