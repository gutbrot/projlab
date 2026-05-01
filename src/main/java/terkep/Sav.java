package terkep;

import jarmu.Jarmu;

/**
 * A Sav osztály felelős az úttest egy adott részének kezeléséért.
 * Nyilvántartja az aktuális hóvastagságot, a kiszórt só mennyiségét, 
 * valamint azt, hogy található-e az adott szakaszon roncs vagy jég.
 */
public class Sav {
    
    private int hoVastagsag = 0;
    private boolean vanEJarmu = false;
    private int savSzama;
    private int athaladokSzama = 0;
    private int soMennyiseg = 0;
    private boolean jegesE = false;
    private boolean zuzalekosE = false;

    public Sav(int savSzama) {
        this.savSzama = savSzama;
    }

    /**
     * Egy logikai függvény, amely eldönti, hogy egy jármű képes-e áthaladni a sávon.
     * @param j A belépni kívánó jármű.
     * @return True, ha a sáv szabad és járható.
     */
    public boolean atjarhatoE(Jarmu j) {
        if (vanEJarmu) {
            System.out.println("    >>> [SÁV " + savSzama + "] Akadály: Foglalt a sáv, egy másik jármű áll itt.");
            return false;
        }

        // Ha 30 cm vagy annál nagyobb a hó, a sáv áthatolhatatlan.
        if (hoVastagsag >= 30) {
            System.out.println("    >>> [SÁV " + savSzama + "] Akadály: Túl nagy a hó (" + hoVastagsag + " cm), a jármű nem tud áthajtani.");
            return false;
        }

        return true;
    }
    
    /**
     * A só hatására bekövetkező hóolvadást szimulálja.
     * Csökkenti a hó vastagságát az adott szakaszon.
     */
    public void soOlvadas() {
        if (soMennyiseg > 0 && hoVastagsag > 0) {
            int regiHo = hoVastagsag;
            
            // A hó mennyisége csökken a kiszórt só mennyiségével
            hoVastagsag = Math.max(0, hoVastagsag - soMennyiseg);
            
            // A folyamat során a só "elhasználódik"
            soMennyiseg = Math.max(0, soMennyiseg - regiHo);

            System.out.println("    >>> [SÁV " + savSzama + " FIZIKA] Az olvadás befejeződött. Új hóvastagság: " + hoVastagsag + " cm.");
            
            if (hoVastagsag == 0) {
                System.out.println("    >>> [SÁV " + savSzama + " FIZIKA] A sáv teljesen hómentessé vált a sótól!");
            }
        } else if (soMennyiseg > 0 && jegesE) {
            // Extra: Ha nincs hó, de jég van, a só a jeget is olvasztja
            jegesE = false;
            soMennyiseg--; // A jég felolvasztása felemészt 1 egység sót
            System.out.println("    >>> [SÁV " + savSzama + " FIZIKA] A kiszórt só felolvasztotta a jégpáncélt!");
        } else {
            System.out.println("    >>> [SÁV " + savSzama + " FIZIKA] Nincs látható hatása a sónak (nincs se hó, se jég).");
        }
    }

    // --- MÓDOSÍTÓ ÉS LEKÉRDEZŐ METÓDUSOK ---

    public void setHo(int h) { 
        this.hoVastagsag = Math.max(0, h); 
    }
    
    public int getHo() { 
        return hoVastagsag; 
    }
    
    public boolean jegesE() { 
        return jegesE; 
    }
    
    public void setJeges(boolean jeges) { 
        this.jegesE = jeges; 
        if (jeges) System.out.println("    >>> [IDŐJÁRÁS] A(z) " + savSzama + ". sáv lefagyott!");
    }

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
    
    public void novelAthaladok() { 
        athaladokSzama++; 
    }
    
    public int getAthaladokSzama() { 
        return athaladokSzama; 
    }

    public void hozzaadSo(int mennyiseg) { 
        this.soMennyiseg += Math.max(0, mennyiseg); 
    }
    
    public int getSoMennyiseg() { 
        return soMennyiseg; 
    }
}