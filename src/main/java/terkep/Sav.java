package terkep;

import jarmu.Jarmu;
import skeleton.Skeleton;

/**
 * A Sav osztály felelős az úttest egy adott részének kezeléséért.
 * Nyilvántartja az aktuális hóvastagságot, a kiszórt só mennyiségét, 
 * valamint azt, hogy található-e az adott szakaszon roncs.
 */
public class Sav {
    
    /** Tárolja a havazás mértékét az adott útszakaszon */
    private int hoVastagsag = 0;
    
    /** Azt jelzi, hogy tartózkodik-e jármű a sávon. */
    private boolean vanEJarmu = false;
    
    /** A sávok sorszáma az adott úton belül. */
    private int savSzama;
    
    /** Statisztikai adat, amely a sávon áthaladó járművek számát számolja. */
    private int athaladokSzama = 0;
    
    /** Az útra kiszórt só mennyiségét tárolja. */
    private int soMennyiseg = 0;
    
    /** Jelzi, hogy a sáv jeges-e. */
    private boolean jegesE = false;
    
    /** Jelzi, hogy a sávon van-e zúzalék. */
    private boolean zuzalekosE = false;

    /**
     * Konstruktor a sáv példányosításához.
     * @param savSzama A sáv azonosító száma.
     */
    public Sav(int savSzama) {
        this.savSzama = savSzama;
    }

    /**
     * Egy logikai függvény, amely eldönti, hogy egy jármű képes-e áthaladni a sávon.
     * A hóvastagságtól és roncs jelenlététől függ.
     * 
     * @param j A belépni kívánó jármű.
     * @return True, ha a sáv szabad és járható.
     */
    public boolean atjarhatoE(Jarmu j) {
        Skeleton.functionCalled("atjarhatoE", this, "boolean", j);

        // Dokumentáció diagram: Van jármű az úton?
        if (vanEJarmu) {
            System.out.println("    [Sáv " + savSzama + "] Foglalt a sáv.");
            return Skeleton.functionReturn(false);
        }

        // Dokumentáció diagram: Túl vastag a hó? (>= 30 cm)
        if (hoVastagsag >= 30) {
            System.out.println("    [Sáv " + savSzama + "] Túl nagy a hó, nem lehet áthajtani.");
            return Skeleton.functionReturn(false);
        }

        System.out.println("    [Sáv " + savSzama + "] A sáv szabad és járható.");
        return Skeleton.functionReturn(true);
    }
    
    /**
     * A só hatására bekövetkező hóolvadást szimulálja.
     * Csökkenti a hó vastagságát az adott szakaszon.
     * 
     * A dokumentáció diagramja szerint: soMennyiseg > 0 ÉS hoVastagsag > 0 esetén fut le.
     */
    public void soOlvadas() {
        Skeleton.functionCalled("soOlvadas", this, "void");

        if (soMennyiseg > 0 && hoVastagsag > 0) {
            // hoVastagsag csökkentése a soMennyiseg-gel
            int regiHo = hoVastagsag;
            hoVastagsag = Math.max(0, hoVastagsag - soMennyiseg);
            
            // soMennyiseg csökkentése a hoVastagsag-gal[cite: 1]
            soMennyiseg = Math.max(0, soMennyiseg - regiHo);

            if (hoVastagsag == 0) {
                System.out.println("    [Sáv " + savSzama + "] Hó megszűntetése.");
            }
        } else {
            System.out.println("    [Sáv " + savSzama + "] Nincs teendő (nincs só vagy hó).");
        }
        
        Skeleton.voidReturn();
    }

    // --- MÓDOSÍTÓ ÉS LEKÉRDEZŐ METÓDUSOK A DOKUMENTÁCIÓ ALAPJÁN ---

    /** Beállítja vagy növeli a hó mennyiségét a sávon. */
    public void setHo(int h) { 
        this.hoVastagsag = Math.max(0, h); 
    }
    
    /** Visszaadja az aktuális hóvastagságot. */
    public int getHo() { 
        return hoVastagsag; 
    }
    
    /** Visszaadja, hogy a sáv jelenleg jeges-e. */
    public boolean jegesE() { 
        return jegesE; 
    }
    
    public void setJeges(boolean jeges) { 
        this.jegesE = jeges; 
    }

    /** A sávot zúzalékossá teszi[cite: 1]. */
    public void setZuzalekos(boolean zuzalekos) { 
        this.zuzalekosE = zuzalekos; 
    }
    
    public boolean isZuzalekos() { 
        return zuzalekosE; 
    }

    public boolean isVanEJarmu() { 
        return vanEJarmu; 
    }
    
    public void setVanEJarmu(boolean vanEJarmu) { 
        this.vanEJarmu = vanEJarmu; 
    }

    public int getSavSzama() { 
        return savSzama; 
    }
    
    /** Regisztrálja a sikeres áthaladást (statisztika növelése a jegesedéshez). */
    public void novelAthaladok() { 
        athaladokSzama++; 
    }
    
    public int getAthaladokSzama() { 
        return athaladokSzama; 
    }

    /** Só hozzáadása a sávhoz. */
    public void hozzaadSo(int mennyiseg) { 
        this.soMennyiseg += Math.max(0, mennyiseg); 
    }
    
    public int getSoMennyiseg() { 
        return soMennyiseg; 
    }
}