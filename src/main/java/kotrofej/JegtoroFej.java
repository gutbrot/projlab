package kotrofej;

import terkep.*;

/**
 * A JegtoroFej osztály felelős a takarítási folyamat egy speciális típusának megvalósításáért
 * Hókotróra szerelhető kotró fej, amely kifejezetten a jégpáncél feltörésére szolgál
 * Felelős a jég megszüntetéséért, azonban a feltört jeget és a havat nem tudja eltakarítani az útról 
 * (ahhoz más fejre, pl. Söprőre vagy Hányóra van szükség)
 */
public class JegtoroFej extends KotroFej {

    /**
     * Konstruktor a JegtoroFej létrehozásához és alapadatainak beállításához
     * @param ar A kotrófej ára, amennyiért a boltban megvásárolható a Takarító által
     */
    public JegtoroFej(int ar) { 
        super(ar); 
    }

    /**
     * Ttakarítás implementálása, amely meghatározza, hogy a hókotró 
     * az aktuális sávban feltöri-e a jeget
     * 
     * @param cel A sáv, amelyen a jégpáncél feltörése történik
     * @param melle A mellette lévő sáv (ebben a megvalósításban nem érintett, mert csak a saját sávban dolgozik)
     * @param ut Az útszakasz, amelyen a takarítás zajlik
     */
    @Override
    public void tisztit(Sav cel, Sav melle, Ut ut) {
        // Biztonsági ellenőrzés a váratlan hibák (NullPointerException) elkerülésére
        if (cel == null) return;
        
        // Ha a sáv nem jeges, a jégtörő fej használata értelmetlen és sikertelen lesz
        if (!cel.jegesE()) {
            System.out.println(">>> Takarítás sikertelen (28. teszteset): A Jégtörő fejet nem lehet használni, mert a(z) " + cel.getSavSzama() + ". sáv nem jeges!");
            return;
        }
        
        // Ha a célsáv jeges volt, a jégtörő fej sikeresen feltöri azt, megszüntetve a jeges állapotot
        cel.setJeges(false);
        System.out.println(">>> Takarítás SIKERES (27. teszteset): A Jégtörő fej feltörte a jégpáncélt a(z) " + cel.getSavSzama() + ". sávban.");
    }

    /**
     * Visszaadja a kotrófej típusának hivatalos megnevezését
     * @return A fej neve: "JegtoroFej" Ezt a Bolt osztály is használja a listázáshoz.
     */
    @Override
    public String getNev() { 
        return "JegtoroFej"; 
    }

    /**
     * Visszaadja a kotrófej aktuális állapotát egy új példány formájában
     * Ezt hívja meg az IBoltiCikk interfész vásárlási folyamata, amikor az eszköztárba másolja a fejet.
     * @return Egy új JegtoroFej objektum a jelenlegi árral
     */
    @Override
    public KotroFej getKotroFej() { 
        return new JegtoroFej(getAr()); 
    }
}