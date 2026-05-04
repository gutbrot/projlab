package terkep;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import jarmu.Jarmu;

/**
 * A Terkep osztály felelős az úthálózat gráfjának globális kezeléséért.
 */
public class Terkep {
    
    private List<Ut> utak = new ArrayList<>();
    
    public void addUt(Ut ut) {
        if (ut != null) {
            utak.add(ut);
        }
    }

    //GETTEREK
    public List<Ut> getTeljesHalozat() {
        return Collections.unmodifiableList(utak);
    }
    
    /**
     * Megpróbálja megmozgatni a járművet a cél sávba.
     */
    public boolean jarmuMozgatas(Jarmu jarmu, Sav celSav) {
        //Ellenőrizzük, hogy a jármű és a cél sáv nem null értékű-e
        if (jarmu == null || celSav == null) {
            return false;
        }
        
        //Megpróbáljuk megmozgatni a járművet a cél sávba
        boolean siker = jarmu.mozgas(celSav);
        return siker;
    }

    /**
     * Frissíti az időjárást, és ennek hatására minden úton havazik.
     */
    public void idojarasFrissites() {
        System.out.println("\n>>> [RENDSZER] Időjárás frissítése: Elkezdett esni a hó!");
        
        //Minden úton havazik, ami befolyásolhatja a járművek mozgását
        for (Ut ut : utak) {
            ut.havazik(5); 
        }
    }

    public Lokacio getRandomLokacio() {
        if (utak.isEmpty()) return null;

        Random rand = new Random();
        //Választunk egy véletlen utat
        Ut randomUt = utak.get(rand.nextInt(utak.size()));
        
        //Választunk az úton belül egy véletlen szakaszt
        List<List<Sav>> szakaszok = randomUt.getSzakaszok();
        int szakaszIdx = rand.nextInt(szakaszok.size());
        List<Sav> randomSzakasz = szakaszok.get(szakaszIdx);
        
        //Választunk a szakaszon belül egy véletlen sávot
        Sav randomSav = randomSzakasz.get(rand.nextInt(randomSzakasz.size()));

        //Létrehozzuk a lokációt (feltételezve a Lokacio konstruktorát)
        return new Lokacio(randomUt, randomSzakasz, randomSav);
    }
}