package seged;

import terkep.*;
import java.util.List;

public class Navigacio {

    public Sav kovetkezoLepes(Lokacio jelenlegi, Lokacio cel) {
        if (jelenlegi == null || cel == null) return null;

        Ut aktUt = jelenlegi.getUt();
        Ut celUt = cel.getUt();
        int szakaszIdx = aktUt.getSzakaszok().indexOf(jelenlegi.getSzakasz());

        // 1. ESET: Az autó ugyanazon az úton van, mint a célja
        if (aktUt == celUt) {
            int celSzakIdx = celUt.getSzakaszok().indexOf(cel.getSzakasz());
            
            if (szakaszIdx < celSzakIdx) {
                // ELŐRE megyünk: a következő szakasz első (pozitív) sávja
                return aktUt.getSzakaszok().get(szakaszIdx + 1).get(0);
            } else if (szakaszIdx > celSzakIdx) {
                // HÁTRA megyünk: az előző szakasz első NEGATÍV sávja
                // (Feltételezzük, hogy a negatív sávok a pozitívak után kezdődnek)
                int pozSzam = aktUt.getPozSavokSzama();
                if (aktUt.getNegSavokSzama() > 0) {
                    return aktUt.getSzakaszok().get(szakaszIdx - 1).get(pozSzam);
                }
            }
        } 
        // 2. ESET: Másik útra kell átmenni (egyszerű szomszéd keresés)
        else {
            // Megnézzük a szomszédokat a kimenő (1) irányban
            for (Ut szomszed : aktUt.getSzomszedok(1)) {
                if (szomszed == celUt || szomszed.getSzomszedok(1).contains(celUt)) {
                    // Átlépünk az új út legelső szakaszára
                    return szomszed.getSzakaszok().get(0).get(0);
                }
            }
        }
        return null;
    }
}