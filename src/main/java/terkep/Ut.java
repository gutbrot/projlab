package terkep;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jarmu.*;

/**
 * Absztrakt út osztály, amely a térkép úthálózatának alapeleme.
 * Az utok sávokból állnak.
 */
public abstract class Ut {
    protected final String nev;
    protected final int hossz;
    protected final List<List<Sav>> szakaszok = new ArrayList<>();

    protected Ut(String nev, int hossz, int savokSzama) {
        this.nev = nev;
        this.hossz = Math.max(1, hossz);
        for (int i = 0; i < this.hossz; i++) {
            List<Sav> szakasz = new ArrayList<>();
            for (int j = 0; j < Math.max(1, savokSzama); j++) {
                szakasz.add(new Sav(j));
            }
            szakaszok.add(szakasz);
        }
    }

    public boolean jarmuEligazito(Jarmu jarmu, Sav sav) {
        return sav != null && sav.atjarhatoE(jarmu);
    }

    public abstract void havazik(int h);

    public String getNev() { return nev; }
    public int getHossz() { return hossz; }
    public List<List<Sav>> getSzakaszok() { return Collections.unmodifiableList(szakaszok); }
    public Sav getSav(int szakaszIndex, int savIndex) {
        return szakaszok.get(szakaszIndex).get(savIndex);
    }
}
