package kotrofej;

import terkep.*;

/**
 * A SoproFej osztály felelős a takarítási folyamat egy alapvető típusának megvalósításáért.
 * Ez a leggyakoribb, hókotróra szerelhető alapértelmezett kotrófej, amely képes a havat 
 * közvetlenül a hókotró nyomvonala mellé (a közvetlen szomszédos sávba) tolni.
 * 
 * Nagyon fontos tulajdonsága , hogy a Hányófejhez hasonlóan 
 * ez az eszköz is tehetetlen a jéggel szemben: ha az út le van fagyva, nem tudja 
 * felszedni a havat sem.
 */
public class SoproFej extends KotroFej {

    /**
     * Konstruktor a SoproFej létrehozásához.
     * @param ar A kotrófej ára, amennyiért a boltban megvásárolható a Takarító által.
     */
    public SoproFej(int ar) { 
        // Az ősosztály (KotroFej) konstruktorának hívása az ár beállításához
        super(ar); 
    }

    /**
     * Feladata a takarítás implementálása, amely meghatározza, hogy a hókotró 
     * az aktuális sávból a közvetlenül mellette lévő sávba mozgatja át a havat.
     * 
     * @param cel A sáv, amelyet a hókotró éppen takarít.
     * @param melle A közvetlenül mellette lévő sáv, ahová a havat tolja (a Hokotro számolja ki).
     * @param ut Az útszakasz, amelyen a takarítás zajlik.
     */
    @Override
    public void tisztit(Sav cel, Sav melle, Ut ut) {
        // Biztonsági ellenőrzés a null pointerek elkerülésére
        if (cel == null) return;
        
        // 1. Jég ellenőrzése a 24. teszteset ("Söprő fej sikertelen használata") alapján.
        // Ha az út jeges, a söprű nem tudja letolni a havat, a takarítás meghiúsul.
        if (cel.jegesE()) {
            System.out.println(">>> Takarítás sikertelen (24. teszteset): A Söprőfej nem tudja letakarítani a havat, mert a(z) " + cel.getSavSzama() + ". sáv jeges!");
            return;
        }
        
        // Lekérdezzük az aktuális hómennyiséget a célsávból
        int ho = cel.getHo();
        
        if (ho == 0) {
            System.out.println(">>> A(z) " + cel.getSavSzama() + ". sávban nincs hó, a Söprőfej üresen járt.");
            return;
        }
        
        // A célsávot megtisztítjuk (a hóvastagság 0 lesz a sikeres söprés után)
        cel.setHo(0);
        System.out.println(">>> Söprőfej (23. teszteset): A(z) " + cel.getSavSzama() + ". sávból " + ho + " egység hó sikeresen letolva.");
        
        // Ha van szomszédos sáv (melle), közvetlenül oda toljuk át a havat
        if (melle != null) {
            melle.setHo(melle.getHo() + ho);
            System.out.println(">>> Söprőfej: A hó át lett tolva a közvetlenül mellette lévő (" + melle.getSavSzama() + ".) sávba.");
        } else {
            // Ha a sáv az út legszéle volt, és a Hokotro nem talált 'melle' sávot, a hó eltűnik az útról
            System.out.println(">>> Söprőfej: A hó az út szélére (a pályán kívülre) lett tolva, mert nincs szomszédos sáv.");
        }
    }

    /**
     * Visszaadja a kotrófej típusának megnevezését.
     * Ezt az azonosítót használja a Bolt és a Takarító a konzolos kiírásoknál.
     * @return A fej neve: "SoproFej".
     */
    @Override
    public String getNev() { 
        return "SoproFej"; 
    }

    /**
     * Visszaadja a kotrófej aktuális állapotát egy új példány formájában (Factory módszer).
     * @return Egy új SoproFej objektum a jelenlegi árral, ami az Eszkoztarba kerül.
     */
    @Override
    public KotroFej getKotroFej() { 
        return new SoproFej(getAr()); 
    }
}