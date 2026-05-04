package terkep;

import java.util.*;
import jarmu.Jarmu;

public abstract class Ut {
    protected String nev;
    protected int hossz;
    protected int pozSavokSzama;
    protected int negSavokSzama;
    protected List<List<Sav>> szakaszok = new ArrayList<>();
    protected Map<Integer, List<Ut>> szomszedok = new HashMap<>();

    protected Ut(String nev, int hossz, int pozSavokSzama, int negSavokSzama) {
        this.nev = nev;
        this.hossz = Math.max(1, hossz);
        this.pozSavokSzama = pozSavokSzama;
        this.negSavokSzama = negSavokSzama;
        
        int osszSav = pozSavokSzama + negSavokSzama;
        for (int i = 0; i < this.hossz; i++) {
            List<Sav> szakasz = new ArrayList<>();
            for (int j = 0; j < osszSav; j++) {
                // A sávokat sorban tároljuk: először a pozitívak, utána a negatívak
                szakasz.add(new Sav(j)); 
            }
            szakaszok.add(szakasz); 
        }
        szomszedok.put(1, new ArrayList<>());
        szomszedok.put(-1, new ArrayList<>());
    }

    public abstract void havazik(int h);
    
    public void addSzomszed(Ut szomszed, int irany) {
        if (szomszed != null && szomszedok.containsKey(irany)) {
            szomszedok.get(irany).add(szomszed);
        }
    }

    // --- Gettermek a logikához ---
    public String getNev() { return nev; }
    public int getHossz() { return hossz; }
    public int getPozSavokSzama() { return pozSavokSzama; }
    public int getNegSavokSzama() { return negSavokSzama; }
    public List<List<Sav>> getSzakaszok() { return Collections.unmodifiableList(szakaszok); }
    public List<Ut> getSzomszedok(int irany) { return Collections.unmodifiableList(szomszedok.getOrDefault(irany, new ArrayList<>())); }
}