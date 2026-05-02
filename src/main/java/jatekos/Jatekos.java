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

    protected Jatekos(int akcioPont) {
        this.akcioPont = Math.max(0, akcioPont);
    }

    public void setJatekter(Jatekter jatekter) {
        this.jatekter = jatekter;
    }
    
    public void forduloVege(Jatekos aktivJatekos) {
        this.akcioPont = 0;
        aktivJarmu = null;
        System.out.println(">>> " + aktivJatekos.getNev() + " befejezte a koret.");
        
        aktualisJatekosIndex++;
        if (aktualisJatekosIndex >= jatekosok.size()) {
            jatekter.ujKor();
            aktualisJatekosIndex = 0;
        }
    }

    public void korVege() {
        // Ha a játékosnak volt még AP-ja, de kiadta a parancsot, 
        // akkor is nullázzuk, így skippeli a körét.
        if (this.akcioPont > 0) {
            System.out.println("    >>> [INFO] Játékos skippelte a maradék " + this.akcioPont + " akciópontját.");
        }
        this.akcioPont = 0;
    }
    
    public void akcioPontKezelo() {
        if (this.akcioPont > 0) {
            this.akcioPont--;
            if (this.akcioPont == 0) {
                this.forduloVege(this);
            }
        }
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
}