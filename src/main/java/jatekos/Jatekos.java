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

    private Jatekter jatekter;

    protected Jatekos(int akcioPont) {
        this.akcioPont = Math.max(0, akcioPont);
    }

    public void setJatekter(Jatekter jatekter) {
        this.jatekter = jatekter;
    }
    
    public void korVege(Jatekos aktivJatekos) {
        this.akcioPont = 0;
        aktivJarmu = null;
        System.out.println(">>> [JÁTÉKOS] Befejezte a körét.");
        
        aktualisJatekosIndex++;
        if (aktualisJatekosIndex >= jatekosok.size()) {
            jatekter.ujKor();
            aktualisJatekosIndex = 0;
        }
    }
    
    public void akcioPontKezelo() {
        if (this.akcioPont > 0) {
            this.akcioPont--;
            if (this.akcioPont == 0) {
                this.korVege(this);
            }
        }
    }
    
    public int getAkcioPont() { 
        return akcioPont; 
    }

    public void setAkcioPont(int p) {
        this.akcioPont = Math.max(0, p);
    }
}