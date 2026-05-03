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
    
    public boolean jarmuMozgatas(Jarmu jarmu, Sav celSav) {
        if (jarmu == null || celSav == null) {
            return false;
        }
        
        boolean siker = jarmu.mozgas(celSav);
        return siker;
    }

    public void idojarasFrissites() {
        System.out.println("\n>>> [RENDSZER] Időjárás frissítése: Elkezdett esni a hó!");
        
        for (Ut ut : utak) {
            ut.havazik(5); 
        }
    }

    public List<Ut> getTeljesHalozat() {
        return Collections.unmodifiableList(utak);
    }

    public Lokacio getRandomLokacio() {
        if (utak.isEmpty()) return null;

        Random rand = new Random();
        // 1. Választunk egy véletlen utat
        Ut randomUt = utak.get(rand.nextInt(utak.size()));
        
        // 2. Választunk az úton belül egy véletlen szakaszt
        List<List<Sav>> szakaszok = randomUt.getSzakaszok();
        int szakaszIdx = rand.nextInt(szakaszok.size());
        List<Sav> randomSzakasz = szakaszok.get(szakaszIdx);
        
        // 3. Választunk a szakaszon belül egy véletlen sávot
        Sav randomSav = randomSzakasz.get(rand.nextInt(randomSzakasz.size()));

        // Létrehozzuk a lokációt (feltételezve a Lokacio konstruktorát)
        return new Lokacio(randomUt, randomSzakasz, randomSav);
    }
}