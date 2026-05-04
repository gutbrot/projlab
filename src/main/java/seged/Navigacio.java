package seged;

import terkep.*;
import java.util.List;

public class Navigacio {

    /**
     * Ez a metódus meghatározza a következő lépést egy autó számára, hogy eljusson egy adott célhoz.
     * A következő lépés egy Sav objektum, amely az autó aktuális pozíciójától egy szomszédos sávra mutat.
     */
    public Sav kovetkezoLepes(Lokacio jelenlegi, Lokacio cel) {
        //Ha bármelyik lokáció null, nincs értelme továbblépni
        if (jelenlegi == null || cel == null) return null;

        Ut aktUt = jelenlegi.getUt();
        Ut celUt = cel.getUt();
        int szakaszIdx = aktUt.getSzakaszok().indexOf(jelenlegi.getSzakasz());

        //Az autó ugyanazon az úton van, mint a célja
        if (aktUt == celUt) {
            int celSzakIdx = celUt.getSzakaszok().indexOf(cel.getSzakasz());
            
            if (szakaszIdx < celSzakIdx) {
                //Előre megyünk: a következő szakasz első (pozitív) sávja
                return aktUt.getSzakaszok().get(szakaszIdx + 1).get(0);
            } else if (szakaszIdx > celSzakIdx) {
                //Hátra megyünk: az előző szakasz első negatív sávja
                int pozSzam = aktUt.getPozSavokSzama();
                if (aktUt.getNegSavokSzama() > 0) {
                    return aktUt.getSzakaszok().get(szakaszIdx - 1).get(pozSzam);
                }
            }
        } 
        //Másik útra kell átmenni (egyszerű szomszéd keresés)
        else {
            //Megnézzük a szomszédokat a kimenő (1) irányban
            for (Ut szomszed : aktUt.getSzomszedok(1)) {
                if (szomszed == celUt || szomszed.getSzomszedok(1).contains(celUt)) {
                    //Átlépünk az új út legelső szakaszára
                    return szomszed.getSzakaszok().get(0).get(0);
                }
            }
        }
        return null;
    }
}