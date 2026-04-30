package kotrofej;

import terkep.*;
import java.util.List;

/**
 * A HanyoFej osztály felelős a takarítási folyamat egy speciális típusának megvalósításáért.
 * Ez egy nagy teljesítményű hókotróra szerelhető kotrófej (ún. hómaró vagy hóhányó), 
 * amely képes a havat a hókotró nyomvonalától több sávval arrébb (a szabály szerint 2 sávval) szórni
 * 
 * Nagyon fontos korlátozása (a 26. teszteset alapján), hogy a jeges úttal nem tud mit kezdeni:
 * ha a sáv le van fagyva, a hányófej használata sikertelen lesz
 */
public class HanyoFej extends KotroFej {

    /**
     * Konstruktor a HanyoFej létrehozásához.
     * @param ar A kotrófej ára, amennyiért a boltban megvásárolható a Takarító által.
     */
    public HanyoFej(int ar) {
        // Az ősosztály konstruktorának hívása az ár beállításához
        super(ar);
    }

    /**
     * Megvalósítja a hányófej tisztító metódusát a 25. és 26. tesztesetek alapján
     * Meghatározza, hogy a hókotró az aktuális sávból 2 sávval arrébb (vagy az út szélére) mozgatja át a havat.
     * 
     * @param cel A sáv, amelyet a hókotró éppen takarít.
     * @param melle A közvetlen szomszédos sáv (ezt a HanyoFej a 2 sávos szabály miatt figyelmen kívül hagyja).
     * @param ut Az útszakasz, amelyen a takarítás zajlik (ebből keressük ki a 2 sávval arrébb lévő célt).
     */
    @Override
    public void tisztit(Sav cel, Sav melle, Ut ut) {
        // Biztonsági ellenőrzés
        if (cel == null || ut == null) return;
        
        // 1. Jég ellenőrzése a 26. teszteset ("Hányó fej sikertelen használata") alapján
        // Ha az út jeges, a hányófej nem tudja felszedni a havat, a takarítás meghiúsul.
        if (cel.jegesE()) {
            System.out.println(">>> Takarítás sikertelen (26. teszteset): A Hányófej nem tudja letakarítani a havat, mert a(z) " + cel.getSavSzama() + ". sáv jeges!");
            return;
        }
        
        // Lekérdezzük az aktuális hómennyiséget a célsávból
        int ho = cel.getHo();
        if (ho == 0) {
            System.out.println(">>> A(z) " + cel.getSavSzama() + ". sávban nincs hó, a Hányófej üresen járt.");
            return;
        }
        
        // A célsávot megtisztítjuk (a hóvastagság 0 lesz a sikeres takarítás után)
        cel.setHo(0);
        System.out.println(">>> Hányófej (25. teszteset): A(z) " + cel.getSavSzama() + ". sávból " + ho + " egység hó sikeresen felszedve.");
        
        // 2. A 2 sávval arrébb lévő célpont megkeresése
        List<Sav> aktSzakasz = null;
        
        // Megkeressük az úton belül azt a keresztmetszetet (szakaszt), amiben épp állunk
        for (List<Sav> szakasz : ut.getSzakaszok()) {
            if (szakasz.contains(cel)) {
                aktSzakasz = szakasz;
                break;
            }
        }
        
        // Ha megvan a szakasz, kiszámoljuk a 2 sávval arrébb lévő indexet
        if (aktSzakasz != null) {
            int aktIndex = aktSzakasz.indexOf(cel);
            Sav hobaDobasCelpontja = null;
            
            // Megpróbáljuk jobbra (index + 2) szórni a havat
            if (aktIndex + 2 < aktSzakasz.size()) {
                hobaDobasCelpontja = aktSzakasz.get(aktIndex + 2);
            } 
            // Ha jobbra nincs hely, megpróbáljuk balra (index - 2) szórni
            else if (aktIndex - 2 >= 0) {
                hobaDobasCelpontja = aktSzakasz.get(aktIndex - 2);
            }
            
            // Ha találtunk érvényes sávot 2 pozícióval arrébb, oda tesszük a havat
            if (hobaDobasCelpontja != null) {
                hobaDobasCelpontja.setHo(hobaDobasCelpontja.getHo() + ho);
                System.out.println(">>> Hányófej: A hó át lett dobva a(z) " + hobaDobasCelpontja.getSavSzama() + ". sávba (2 sávval arrébb).");
            } else {
                // Ha az út nem elég széles (nincs 2 sávval arrébb lévő sáv), 
                // a havat az út szélére (a rendszerből kivezetve) szórja.
                System.out.println(">>> Hányófej: A hó az út szélére (a pályán kívülre) lett szórva.");
            }
        }
    }

    /**
     * Visszaadja a kotrófej típusának megnevezését.
     * @return A fej neve: "HanyoFej".
     */
    @Override
    public String getNev() {
        return "HanyoFej";
    }

    /**
     * Visszaadja a kotrófej aktuális állapotát egy új példány formájában.
     * Ezt hívja meg az Eszkoztar, amikor új fejet rak a raktárba.
     * @return Egy új HanyoFej objektum a jelenlegi árral.
     */
    @Override
    public KotroFej getKotroFej() {
        return new HanyoFej(getAr());
    }
}